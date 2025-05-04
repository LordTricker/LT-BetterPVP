package pl.lordtricker.ltbpvp.client.mixin;

import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.item.ItemStack;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import pl.lordtricker.ltbpvp.client.config.ModSettings;
import pl.lordtricker.ltbpvp.client.hud.AttackDelayTutorHUD;

/** Jeden alert na 5s na slot – kropka. */
@Mixin(ClientPlayerEntity.class)
public abstract class ArmorStatusMixin {

    @Unique private static final long COOLDOWN_MS = 5_000;

    /** Znacznik czasu ostatniego alarmu per slot. */
    @Unique private final long[] lastAlert = {0, 0, 0, 0};

    @Inject(method = "tick", at = @At("TAIL"))
    private void onClientTick(CallbackInfo ci) {
        if (!ModSettings.armorStatusEnabled) return;

        ClientPlayerEntity player = (ClientPlayerEntity)(Object)this;
        long now = System.currentTimeMillis();

        for (EquipmentSlot slot : EquipmentSlot.values()) {
            if (!slot.isArmorSlot()) continue;

            int idx = switch (slot) {
                case FEET   -> 0;
                case LEGS   -> 1;
                case CHEST  -> 2;
                case HEAD   -> 3;
                default     -> -1;
            };
            if (idx < 0) continue;

            ItemStack stack = player.getEquippedStack(slot);
            if (stack.isEmpty() || !stack.isDamageable()) continue;

            int pct = (int)((stack.getMaxDamage() - stack.getDamage())
                    * 100.0 / stack.getMaxDamage());

            if (pct <= ModSettings.armorStatusThreshold
                    && now - lastAlert[idx] >= COOLDOWN_MS) {

                lastAlert[idx] = now;

                if (ModSettings.armorStatusTextEnabled) {
                    AttackDelayTutorHUD.setMessage(
                            stack.getName().getString() + " spadł poniżej " + pct + "%!", 1500);
                }
                if (ModSettings.armorStatusSoundEnabled) {
                    player.playSound(SoundEvents.BLOCK_NOTE_BLOCK_CHIME.value()
                    );
                }
            }
        }
    }
}
