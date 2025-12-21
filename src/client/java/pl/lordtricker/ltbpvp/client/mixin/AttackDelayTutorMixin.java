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
import pl.lordtricker.ltbpvp.core.config.CoreSettings;
import pl.lordtricker.ltbpvp.core.hud.AttackDelayTutorHUD;
import pl.lordtricker.ltbpvp.core.logic.AttackDelayTutorLogic;

@Mixin(ClientPlayerEntity.class)
public abstract class AttackDelayTutorMixin {
    @Unique
    private final AttackDelayTutorLogic ltbpvp$logic = new AttackDelayTutorLogic();

    @Inject(method = "swingHand", at = @At("HEAD"))
    private void onSwingHand(Hand hand, CallbackInfo ci) {
        if (!CoreSettings.attackDelayTutorEnabled) {
            return;
        }

        long currentTime = System.currentTimeMillis();

        ClientPlayerEntity player = (ClientPlayerEntity)(Object)this;
        float attackSpeed = (float) player.getAttributeValue(EntityAttributes.ATTACK_SPEED);

        HitResult hit = MinecraftClient.getInstance().crosshairTarget;
        boolean hitEntity = hit instanceof EntityHitResult;
        AttackDelayTutorLogic.Result result = ltbpvp$logic.onSwing(
                currentTime,
                hitEntity,
                attackSpeed,
                CoreSettings.attackDelayTutorTextEnabled,
                CoreSettings.attackDelayTutorSoundEnabled
        );
        if (result != null) {
            if (result.showText()) {
                AttackDelayTutorHUD.setMessage(result.message(), result.durationMs());
            }
            if (result.playSound()) {
                player.playSound(SoundEvents.BLOCK_ANVIL_PLACE, 0.15F, 1.0F);
            }
        }
    }
}

