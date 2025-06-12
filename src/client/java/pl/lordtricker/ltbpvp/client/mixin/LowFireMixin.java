package pl.lordtricker.ltbpvp.client.mixin;

import net.minecraft.client.gui.hud.InGameOverlayRenderer;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.util.math.MatrixStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import pl.lordtricker.ltbpvp.client.config.ModSettings;

@Mixin(InGameOverlayRenderer.class)
public abstract class LowFireMixin {

    @Inject(
            method = "renderFireOverlay(Lnet/minecraft/client/util/math/MatrixStack;" +
                    "Lnet/minecraft/client/render/VertexConsumerProvider;)V",
            at = @At("HEAD")
    )
    private static void ltbpvp$fireHead(MatrixStack matrices,
                                        VertexConsumerProvider providers,
                                        CallbackInfo ci) {
        if (ModSettings.lowFireEnabled) {
            matrices.push();
            matrices.translate(0.0F, ModSettings.lowFireHeight, 0.0F);
        }
    }

    @Inject(
            method = "renderFireOverlay(Lnet/minecraft/client/util/math/MatrixStack;" +
                    "Lnet/minecraft/client/render/VertexConsumerProvider;)V",
            at = @At("RETURN")
    )
    private static void ltbpvp$fireTail(MatrixStack matrices,
                                        VertexConsumerProvider providers,
                                        CallbackInfo ci) {
        if (ModSettings.lowFireEnabled) {
            matrices.pop();
        }
    }
}
