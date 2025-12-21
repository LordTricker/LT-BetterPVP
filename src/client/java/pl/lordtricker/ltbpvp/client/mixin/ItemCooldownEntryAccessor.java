package pl.lordtricker.ltbpvp.client.mixin;

import net.minecraft.entity.player.ItemCooldownManager;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin(targets = "net.minecraft.entity.player.ItemCooldownManager$Entry")
public interface ItemCooldownEntryAccessor {
    @Accessor("startTick")
    int ltbpvp$getStartTick();

    @Accessor("endTick")
    int ltbpvp$getEndTick();
}