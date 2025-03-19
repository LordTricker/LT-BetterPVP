package pl.lordtricker.ltbpvp.client.mixin;

import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gl.ShaderProgramKeys;
import net.minecraft.client.gui.hud.InGameHud;
import net.minecraft.client.render.*;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.Entity;
import net.minecraft.entity.attribute.EntityAttributes;
import net.minecraft.util.TriState;
import net.minecraft.util.hit.EntityHitResult;
import net.minecraft.util.hit.HitResult;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.RotationAxis;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import pl.lordtricker.ltbpvp.client.config.ModSettings;

@Mixin(InGameHud.class)
public class TargetCrosshairMixin {

    @Inject(method = "renderCrosshair", at = @At("RETURN"))
    private void onRenderCrosshair(DrawContext context, RenderTickCounter tickCounter, CallbackInfo ci) {
        MinecraftClient client = MinecraftClient.getInstance();
        if (!ModSettings.targetingEnabled) {
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

        int size = ModSettings.targetRange;
        float angle = (System.currentTimeMillis() % 36000L) / 100.0F;

        int color = 0xFF000000;
        switch (ModSettings.crosshairColor) {
            case YELLOW -> color = 0xFFFFFF00;
            case RED -> color = 0xFFFF0000;
            case LIGHT_BLUE -> color = 0xFF7FAAFF;
            case ORANGE -> color = 0xFFFF8000;
            case GREEN -> color = 0xFF00FF00;
            case WHITE -> color = 0xFFFFFFFF;
            case PURPLE -> color = 0xFFFF00FF;
            case RGB -> {
                float time = ((System.currentTimeMillis() % 2000L) / 2000.0F) * (float) Math.PI * 2.0F;
                int r = (int) ((0.5F + 0.5F * Math.sin(time)) * 255);
                int g = (int) ((0.5F + 0.5F * Math.sin(time + (2 * Math.PI / 3))) * 255);
                int b = (int) ((0.5F + 0.5F * Math.sin(time + (4 * Math.PI / 3))) * 255);
                color = 0xFF000000 | (r << 16) | (g << 8) | b;
            }
        }

        matrices.push();
        matrices.translate(centerX, centerY, 0);
        matrices.multiply(RotationAxis.POSITIVE_Z.rotationDegrees(angle));

        Identifier texture = ModSettings.targetStyle.getTexture();

        RenderLayer renderLayer = RenderLayer.of(
                "target_crosshair",
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
                identifier -> renderLayer,
                texture,
                -size/2,
                -size/2,
                0f,
                0f,
                size,
                size,
                size,
                size,
                color
        );

        matrices.pop();
    }
}