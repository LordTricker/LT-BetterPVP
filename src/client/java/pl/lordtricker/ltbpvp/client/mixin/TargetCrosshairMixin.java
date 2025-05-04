package pl.lordtricker.ltbpvp.client.mixin;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gl.ShaderProgramKeys;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.hud.InGameHud;
import net.minecraft.client.render.*;
import net.minecraft.client.render.RenderLayer.MultiPhaseParameters;
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
    private void onRenderCrosshair(DrawContext context,
                                   RenderTickCounter tickCounter,
                                   CallbackInfo ci) {

        MinecraftClient client = MinecraftClient.getInstance();
        if (!ModSettings.targetingEnabled) return;

        HitResult hit = client.crosshairTarget;
        if (!(hit instanceof EntityHitResult entityHit)) return;

        Entity target = entityHit.getEntity();
        if (target == null) return;

        double reach = client.player.getAttributeValue(EntityAttributes.ENTITY_INTERACTION_RANGE);
        if (client.player.squaredDistanceTo(target) > reach * reach) return;

        renderCustomTargetCrosshair(context, context.getMatrices());
    }

    /** Rysuje celownik z nowymi suwakami / tęczą RGB. */
    private void renderCustomTargetCrosshair(DrawContext ctx, MatrixStack matrices) {

        MinecraftClient mc = MinecraftClient.getInstance();
        int cx = mc.getWindow().getScaledWidth()  / 2;
        int cy = mc.getWindow().getScaledHeight() / 2;

        int size  = ModSettings.targetRange;
        float rot = (System.currentTimeMillis() % 36_000L) / 100.0F;

        /* ===== KOLORY ===== */
        float rf, gf, bf;

        if (ModSettings.rgbEnabled) {
            float t = ((System.currentTimeMillis() % 2_000L) / 2_000.0F) * (float) Math.PI * 2;
            rf = 0.5F + 0.5F * (float) Math.sin(t);
            gf = 0.5F + 0.5F * (float) Math.sin(t + 2 * Math.PI / 3);
            bf = 0.5F + 0.5F * (float) Math.sin(t + 4 * Math.PI / 3);
        } else {
            rf = ModSettings.customRed;
            gf = ModSettings.customGreen;
            bf = ModSettings.customBlue;
        }

        int ri = Math.round(rf * 255);
        int gi = Math.round(gf * 255);
        int bi = Math.round(bf * 255);
        int argb = 0xFF000000 | (ri << 16) | (gi << 8) | bi;

        matrices.push();
        matrices.translate(cx, cy, 0);
        matrices.multiply(RotationAxis.POSITIVE_Z.rotationDegrees(rot));

        Identifier tex = ModSettings.targetStyle.getTexture();

        RenderLayer layer = RenderLayer.of(
                "target_crosshair_rgb",
                VertexFormats.POSITION_TEXTURE_COLOR,
                VertexFormat.DrawMode.QUADS,
                256,
                false,
                true,
                MultiPhaseParameters.builder()
                        .program(new RenderPhase.ShaderProgram(ShaderProgramKeys.POSITION_TEX_COLOR))
                        .texture(new RenderPhase.Texture(tex, TriState.FALSE, false))
                        .transparency(RenderPhase.Transparency.TRANSLUCENT_TRANSPARENCY)
                        .lightmap(RenderPhase.Lightmap.DISABLE_LIGHTMAP)
                        .overlay(RenderPhase.Overlay.DISABLE_OVERLAY_COLOR)
                        .cull(RenderPhase.Cull.DISABLE_CULLING)
                        .depthTest(RenderPhase.DepthTest.ALWAYS_DEPTH_TEST)
                        .build(true)
        );

        ctx.drawTexture(
                id -> layer,
                tex,
                -size / 2, -size / 2,
                0f, 0f,
                size, size,
                size, size,
                argb
        );

        matrices.pop();
    }
}
