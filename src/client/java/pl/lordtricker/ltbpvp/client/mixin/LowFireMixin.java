package pl.lordtricker.ltbpvp.client.mixin;

import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.hud.InGameHud;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import pl.lordtricker.ltbpvp.client.config.ModSettings;

@Mixin(InGameHud.class)
public class LowFireMixin {

    @Inject(method = "renderFireOverlay", at = @At("HEAD"))
    private void onRenderFireOverlayHead(DrawContext ctx, CallbackInfo ci) {
        if (ModSettings.lowFireEnabled) {
            ctx.getMatrices().push();
            ctx.getMatrices().translate(0.0F, ModSettings.lowFireHeight, 0.0F);
        }
    }

    @Inject(method = "renderFireOverlay", at = @At("RETURN"))
    private void onRenderFireOverlayTail(DrawContext ctx, CallbackInfo ci) {
        if (ModSettings.lowFireEnabled) {
            ctx.getMatrices().pop();
        }
    }
}
