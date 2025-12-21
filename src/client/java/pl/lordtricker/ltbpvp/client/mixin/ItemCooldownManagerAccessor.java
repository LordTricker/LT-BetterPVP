package pl.lordtricker.ltbpvp.client.mixin;

import net.minecraft.util.Identifier;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

import java.util.Map;

@Mixin(net.minecraft.entity.player.ItemCooldownManager.class)
public interface ItemCooldownManagerAccessor {
    @Accessor("entries")
    Map<Identifier, Object> ltbpvp$getEntries();

    @Accessor("tick")
    int ltbpvp$getTick();
}
