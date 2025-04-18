package pl.lordtricker.ltbpvp.client.mixin;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.entity.attribute.EntityAttributes;
import net.minecraft.sound.SoundEvents;
import net.minecraft.util.Hand;
import net.minecraft.util.hit.EntityHitResult;
import net.minecraft.util.hit.HitResult;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import pl.lordtricker.ltbpvp.client.config.ModSettings;
import pl.lordtricker.ltbpvp.client.hud.AttackDelayTutorHUD;

@Mixin(ClientPlayerEntity.class)
public abstract class AttackDelayTutorMixin {

    @Unique
    private long lastSwingTime = 0;
    @Unique
    private boolean hasAttackedOnce = false;

    /**
     * Wstrzyknięcie do metody swingHand.
     * Sprawdzamy, czy gracz celuje w encję. Jeśli tak, obliczamy cooldown na podstawie atrybutu ATTACK_SPEED.
     * Jeśli odstęp między swingami jest mniejszy niż wymagany cooldown, wysyłamy komunikat HUD oraz odtwarzamy dźwięk.
     */
    @Inject(method = "swingHand", at = @At("HEAD"))
    private void onSwingHand(Hand hand, CallbackInfo ci) {
        ClientPlayerEntity player = (ClientPlayerEntity)(Object)this;
        if (!ModSettings.attackDelayTutorEnabled) {
            return;
        }

        HitResult hitResult = MinecraftClient.getInstance().crosshairTarget;
        if (!(hitResult instanceof EntityHitResult)) {
            return;
        }

        long currentTime = System.currentTimeMillis();
        if (!hasAttackedOnce) {
            hasAttackedOnce = true;
            lastSwingTime = currentTime;
            return;
        }

        long delta = currentTime - lastSwingTime;
        lastSwingTime = currentTime;

        float attackSpeed = (float) player.getAttributeValue(EntityAttributes.GENERIC_ATTACK_SPEED);
        float cooldownTicks = 20.0F / attackSpeed;
        long cooldownMs = (long)(cooldownTicks * 50);

        if (delta < cooldownMs) {
            if (ModSettings.attackDelayTutorTextEnabled) {
                AttackDelayTutorHUD.setMessage("Uderzyłeś za szybko!", 500);
            }
            if (ModSettings.attackDelayTutorSoundEnabled) {
                player.playSound(SoundEvents.BLOCK_ANVIL_PLACE, 0.15F, 1.0F);
            }
        }
    }
}
