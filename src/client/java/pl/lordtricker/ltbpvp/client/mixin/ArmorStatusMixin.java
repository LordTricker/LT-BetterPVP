package pl.lordtricker.ltbpvp.client.mixin;

import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.item.ItemStack;
import net.minecraft.sound.SoundEvents;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import pl.lordtricker.ltbpvp.core.config.CoreSettings;
import pl.lordtricker.ltbpvp.core.hud.AttackDelayTutorHUD;
import pl.lordtricker.ltbpvp.core.logic.ArmorStatusLogic;

/** Jeden alert na 5s na slot – kropka. */
@Mixin(ClientPlayerEntity.class)
public abstract class ArmorStatusMixin {
    @Unique private final ArmorStatusLogic ltbpvp$logic = new ArmorStatusLogic();

    @Inject(method = "tick", at = @At("TAIL"))
    private void onClientTick(CallbackInfo ci) {
        if (!CoreSettings.armorStatusEnabled) return;

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

            int pct = (int)((stack.getMaxDamage() - stack.getDamage()) * 100.0 / stack.getMaxDamage());

            ArmorStatusLogic.Result result = ltbpvp$logic.checkArmor(
                    idx,
                    pct,
                    stack.getName().getString(),
                    now,
                    CoreSettings.armorStatusThreshold,
                    CoreSettings.armorStatusTextEnabled,
                    CoreSettings.armorStatusSoundEnabled
            );

            if (result != null) {
                if (result.showText()) {
                    AttackDelayTutorHUD.setMessage(result.message(), result.durationMs());
                }
                if (result.playSound()) {
                    player.playSound(SoundEvents.BLOCK_NOTE_BLOCK_CHIME.value());
                }
            }
        }
    }
}

