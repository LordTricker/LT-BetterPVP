package pl.lordtricker.ltbpvp.client.mixin;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.hud.InGameHud;
import net.minecraft.client.render.RenderTickCounter;
import net.minecraft.text.Text;
import net.minecraft.util.hit.EntityHitResult;
import net.minecraft.util.hit.HitResult;
import net.minecraft.util.math.Vec3d;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import pl.lordtricker.ltbpvp.core.config.CoreSettings;
import pl.lordtricker.ltbpvp.core.enums.DistanceDisplayMode;
import pl.lordtricker.ltbpvp.core.logic.DistanceHudLogic;

@Mixin(InGameHud.class)
public class DistanceHudMixin {
    private static final float SCALE = 0.7F;
    private static final int Y_OFFSET = 32;

    @Inject(method = "renderCrosshair", at = @At("RETURN"))
    private void ltbpvp$renderDistance(DrawContext context, RenderTickCounter tickCounter, CallbackInfo ci) {
        if (!CoreSettings.distanceHudEnabled) {
            return;
        }

        MinecraftClient client = MinecraftClient.getInstance();
        if (client.player == null || client.crosshairTarget == null) {
            return;
        }

        HitResult hitResult = client.crosshairTarget;
        if (hitResult.getType() == HitResult.Type.MISS) {
            return;
        }
        if (CoreSettings.distanceDisplayMode == DistanceDisplayMode.ENTITY_ONLY
                && !(hitResult instanceof EntityHitResult)) {
            return;
        }

        Vec3d start = client.player.getEyePos();
        Vec3d end = hitResult.getPos();
        String distanceText = DistanceHudLogic.formatMeters(DistanceHudLogic.distanceMeters(start, end));
        if (distanceText.isEmpty()) {
            return;
        }

        int screenWidth = client.getWindow().getScaledWidth();
        int screenHeight = client.getWindow().getScaledHeight();
        Text text = Text.literal(distanceText);
        int textWidth = client.textRenderer.getWidth(text);
        int y = screenHeight / 2 + Y_OFFSET;

        context.getMatrices().push();
        context.getMatrices().scale(SCALE, SCALE, 1.0F);
        int scaledCenterX = Math.round((screenWidth / 2.0F) / SCALE);
        int scaledX = Math.round(scaledCenterX - textWidth / 2.0F);
        context.drawText(client.textRenderer, text, scaledX, Math.round(y / SCALE), 0xFFFFFFFF, true);
        context.getMatrices().pop();
    }
}
