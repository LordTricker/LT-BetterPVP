package pl.lordtricker.ltbpvp.client.mixin;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.hud.InGameHud;
import net.minecraft.client.render.RenderTickCounter;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.client.render.RenderPhase;
import net.minecraft.client.gl.ShaderProgramKeys;
import net.minecraft.client.render.VertexFormat;
import net.minecraft.client.render.VertexFormats;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.Entity;
import net.minecraft.entity.attribute.EntityAttributes;
import net.minecraft.util.hit.EntityHitResult;
import net.minecraft.util.hit.HitResult;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.RotationAxis;
import net.minecraft.util.TriState;
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
        MatrixStack matrices = context.getMatrices();
        renderCustomTargetCrosshair(context, matrices);
    }

    private void renderCustomTargetCrosshair(DrawContext context, MatrixStack matrices) {
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

        matrices.push();
        matrices.translate(centerX, centerY, 0);
        matrices.multiply(RotationAxis.POSITIVE_Z.rotationDegrees(angle));

        Identifier texture = Identifier.of(
                CoreSettings.targetStyle.getNamespace(),
                CoreSettings.targetStyle.getPath()
        );
        RenderLayer layer = RenderLayer.of(
                "target_crosshair_rgb",
                VertexFormats.POSITION_TEXTURE_COLOR,
                VertexFormat.DrawMode.QUADS,
                256,
                false,
                true,
                RenderLayer.MultiPhaseParameters.builder()
                        .program(new RenderPhase.ShaderProgram(ShaderProgramKeys.POSITION_TEX_COLOR))
                        .texture(new RenderPhase.Texture(texture, TriState.FALSE, false))
                        .transparency(RenderPhase.Transparency.TRANSLUCENT_TRANSPARENCY)
                        .lightmap(RenderPhase.Lightmap.DISABLE_LIGHTMAP)
                        .overlay(RenderPhase.Overlay.DISABLE_OVERLAY_COLOR)
                        .cull(RenderPhase.Cull.DISABLE_CULLING)
                        .depthTest(RenderPhase.DepthTest.ALWAYS_DEPTH_TEST)
                        .build(true)
        );

        context.drawTexture(
                id -> layer,
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

        matrices.pop();
    }

}

