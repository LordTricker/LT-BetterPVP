package pl.lordtricker.ltbpvp.client.mixin;

import net.minecraft.client.render.command.OrderedRenderCommandQueue;
import net.minecraft.client.render.entity.FishingBobberEntityRenderer;
import net.minecraft.client.render.entity.state.FishingBobberEntityState;
import net.minecraft.client.render.state.CameraRenderState;
import net.minecraft.client.util.math.MatrixStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import pl.lordtricker.ltbpvp.core.config.CoreSettings;
import pl.lordtricker.ltbpvp.core.logic.FishingBobberLogic;

@Mixin(FishingBobberEntityRenderer.class)
public abstract class FishingBobberMixin {
    @Inject(
            method = "render",
            at = @At("HEAD"),
            require = 0
    )
    private void ltbpvp$beforeRender(FishingBobberEntityState state,
                                     MatrixStack matrices,
                                     OrderedRenderCommandQueue renderCommandQueue,
                                     CameraRenderState cameraRenderState,
                                     CallbackInfo ci) {
        if (!CoreSettings.fishingBobberEnabled) {
            return;
        }
        float offsetY = FishingBobberLogic.clampOffsetY(CoreSettings.fishingBobberOffsetY);
        float scale = FishingBobberLogic.clampScale(CoreSettings.fishingBobberScale);
        matrices.push();
        matrices.translate(0.0f, offsetY, 0.0f);
        matrices.scale(scale, scale, scale);
    }

    @Inject(
            method = "render",
            at = @At("RETURN"),
            require = 0
    )
    private void ltbpvp$afterRender(FishingBobberEntityState state,
                                    MatrixStack matrices,
                                    OrderedRenderCommandQueue renderCommandQueue,
                                    CameraRenderState cameraRenderState,
                                    CallbackInfo ci) {
        if (!CoreSettings.fishingBobberEnabled) {
            return;
        }
        matrices.pop();
    }
}
