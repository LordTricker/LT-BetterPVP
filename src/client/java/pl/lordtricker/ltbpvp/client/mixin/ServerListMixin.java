package pl.lordtricker.ltbpvp.client.mixin;

import net.minecraft.client.option.ServerList;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import pl.lordtricker.ltbpvp.client.util.ServerListPatcher;
import pl.lordtricker.ltbpvp.core.config.CoreSettings;

@Mixin(ServerList.class)
public class ServerListMixin {
    @Inject(method = "loadFile", at = @At("TAIL"), require = 0)
    private void ltbpvp$afterLoad(CallbackInfo ci) {
        ltbpvp$injectOrMove();
    }

    @Unique
    private void ltbpvp$injectOrMove() {
        if (!CoreSettings.adsEnabled) return;
        ServerListPatcher.injectOrMove((ServerList)(Object)this);
    }
}

