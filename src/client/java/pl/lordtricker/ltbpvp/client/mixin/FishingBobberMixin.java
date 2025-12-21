package pl.lordtricker.ltbpvp.client.mixin;

import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.entity.FishingBobberEntityRenderer;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.projectile.FishingBobberEntity;
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
    private void ltbpvp$beforeRender(FishingBobberEntity entity,
                                     float yaw,
                                     float tickDelta,
                                     MatrixStack matrices,
                                     VertexConsumerProvider vertexConsumers,
                                     int light,
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
    private void ltbpvp$afterRender(FishingBobberEntity entity,
                                    float yaw,
                                    float tickDelta,
                                    MatrixStack matrices,
                                    VertexConsumerProvider vertexConsumers,
                                    int light,
                                    CallbackInfo ci) {
        if (!CoreSettings.fishingBobberEnabled) {
            return;
        }
        matrices.pop();
    }
}
