package pl.lordtricker.ltbpvp.client.mixin;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.hud.InGameHud;
import net.minecraft.client.render.RenderTickCounter;
import net.minecraft.text.Text;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import pl.lordtricker.ltbpvp.core.hud.AttackDelayTutorHUD;

@Mixin(InGameHud.class)
public abstract class AttackDelayTutorHudMixin {
    @Inject(method = "renderCrosshair", at = @At("RETURN"))
    private void renderAttackTutorMessage(DrawContext context, RenderTickCounter tickCounter, CallbackInfo ci) {
        long currentTime = System.currentTimeMillis();
        if (AttackDelayTutorHUD.shouldRender(currentTime)) {
            MinecraftClient client = MinecraftClient.getInstance();
            int screenWidth = client.getWindow().getScaledWidth();
            int screenHeight = client.getWindow().getScaledHeight();
            Text text = Text.literal(AttackDelayTutorHUD.message);
            int textWidth = client.textRenderer.getWidth(text);
            int y = screenHeight / 2 + AttackDelayTutorHUD.Y_OFFSET;

            context.getMatrices().push();
            float scale = AttackDelayTutorHUD.SCALE;
            context.getMatrices().scale(scale, scale, scale);
            int scaledCenterX = Math.round((screenWidth / 2.0F) / scale);
            int scaledX = Math.round(scaledCenterX - textWidth / 2.0F);
            context.drawText(client.textRenderer, text, scaledX, Math.round(y / scale), 0xFFFF0000, true);
            context.getMatrices().pop();
        }
    }
}

