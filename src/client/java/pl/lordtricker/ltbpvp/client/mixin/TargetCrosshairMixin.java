package pl.lordtricker.ltbpvp.client.mixin;

import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.hud.InGameHud;
import net.minecraft.client.render.RenderTickCounter;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.render.GameRenderer;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.Entity;
import net.minecraft.entity.attribute.EntityAttributes;
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
        double reachDistance = client.player.getAttributeValue(EntityAttributes.PLAYER_ENTITY_INTERACTION_RANGE);
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

        float red = 1.0F, green = 1.0F, blue = 1.0F;
        switch (ModSettings.crosshairColor) {
            case YELLOW -> {
                red = 1.0F; green = 1.0F; blue = 0.0F;
            }
            case RED -> {
                red = 1.0F; green = 0.0F; blue = 0.0F;
            }
            case LIGHT_BLUE -> {
                red = 0.5F; green = 0.7F; blue = 1.0F;
            }
            case ORANGE -> {
                red = 1.0F; green = 0.5F; blue = 0.0F;
            }
            case GREEN -> {
                red = 0.0F; green = 1.0F; blue = 0.0F;
            }
            case WHITE -> {
                red = 1.0F; green = 1.0F; blue = 1.0F;
            }
            case PURPLE -> {
                red = 1.0F; green = 0.0F; blue = 1.0F;
            }
            case RGB -> {
                float time = ((System.currentTimeMillis() % 2000L) / 2000.0F) * (float)Math.PI * 2.0F;
                red   = 0.5F + 0.5F * (float)Math.sin(time);
                green = 0.5F + 0.5F * (float)Math.sin(time + (2 * Math.PI / 3));
                blue  = 0.5F + 0.5F * (float)Math.sin(time + (4 * Math.PI / 3));
            }
        }

        matrices.push();
        matrices.translate(centerX, centerY, 0);
        matrices.multiply(RotationAxis.POSITIVE_Z.rotationDegrees(angle));

        RenderSystem.enableBlend();
        RenderSystem.defaultBlendFunc();
        RenderSystem.setShader(GameRenderer::getPositionTexProgram);
        RenderSystem.setShaderColor(red, green, blue, 1.0F);

        Identifier texture = ModSettings.targetStyle.getTexture();
        RenderSystem.setShaderTexture(0, texture);

        context.drawTexture(texture, -size / 2, -size / 2, 0, 0, size, size, size, size);

        RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);
        RenderSystem.disableBlend();
        matrices.pop();
    }

}