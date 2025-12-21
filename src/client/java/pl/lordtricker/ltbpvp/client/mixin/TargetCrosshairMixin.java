package pl.lordtricker.ltbpvp.client.mixin;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.hud.InGameHud;
import net.minecraft.client.render.RenderTickCounter;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gl.RenderPipelines;
import net.minecraft.entity.Entity;
import net.minecraft.entity.attribute.EntityAttributes;
import net.minecraft.util.hit.EntityHitResult;
import net.minecraft.util.hit.HitResult;
import net.minecraft.util.Identifier;
import org.joml.Matrix3x2fStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import pl.lordtricker.ltbpvp.core.config.CoreSettings;
import pl.lordtricker.ltbpvp.core.logic.TargetCrosshairLogic;

@Mixin(InGameHud.class)
public class TargetCrosshairMixin {

    @Inject(method = "renderCrosshair", at = @At("RETURN"))
    private void onRenderCrosshair(DrawContext context, RenderTickCounter tickCounter, CallbackInfo ci) {
        MinecraftClient client = MinecraftClient.getInstance();
        if (!CoreSettings.targetingEnabled) {
            return;
        }
        HitResult hitResult = client.crosshairTarget;
        if (!(hitResult instanceof EntityHitResult)) {
            return;
        }
        EntityHitResult entityHitResult = (EntityHitResult) hitResult;
        Entity target = entityHitResult.getEntity();
        if (target == null) {
            return;
        }
        double reachDistance = client.player.getAttributeValue(EntityAttributes.ENTITY_INTERACTION_RANGE);
        double distanceSq = client.player.squaredDistanceTo(target);
        if (distanceSq > reachDistance * reachDistance) {
            return;
        }
        Matrix3x2fStack matrices = context.getMatrices();
        renderCustomTargetCrosshair(context, matrices);
    }

    private void renderCustomTargetCrosshair(DrawContext context, Matrix3x2fStack matrices) {
        MinecraftClient client = MinecraftClient.getInstance();
        int screenWidth = client.getWindow().getScaledWidth();
        int screenHeight = client.getWindow().getScaledHeight();
        int centerX = screenWidth / 2;
        int centerY = screenHeight / 2;

        int size = CoreSettings.targetRange;

        long nowMs = System.currentTimeMillis();
        float angle = TargetCrosshairLogic.computeAngleDegrees(nowMs);
        float[] color = TargetCrosshairLogic.computeColor(
                nowMs,
                CoreSettings.rgbEnabled,
                CoreSettings.customRed,
                CoreSettings.customGreen,
                CoreSettings.customBlue
        );

        int red = Math.round(color[0] * 255.0F);
        int green = Math.round(color[1] * 255.0F);
        int blue = Math.round(color[2] * 255.0F);
        int argb = 0xFF000000 | (red << 16) | (green << 8) | blue;

        matrices.pushMatrix();
        matrices.translate(centerX, centerY);
        matrices.rotate((float) Math.toRadians(angle));

        Identifier texture = Identifier.of(
                CoreSettings.targetStyle.getNamespace(),
                CoreSettings.targetStyle.getPath()
        );
        context.drawTexture(
                RenderPipelines.GUI_TEXTURED,
                texture,
                -size / 2,
                -size / 2,
                0.0F,
                0.0F,
                size,
                size,
                size,
                size,
                argb
        );

        matrices.popMatrix();
    }

}
