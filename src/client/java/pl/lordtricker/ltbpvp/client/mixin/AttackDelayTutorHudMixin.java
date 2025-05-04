package pl.lordtricker.ltbpvp.client.mixin;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.hud.InGameHud;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.text.Text;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import pl.lordtricker.ltbpvp.client.hud.AttackDelayTutorHUD;

@Mixin(InGameHud.class)
public abstract class AttackDelayTutorHudMixin {

    @Inject(method = "renderCrosshair", at = @At("RETURN"))
    private void renderAttackTutorMessage(MatrixStack matrices, CallbackInfo ci) {
        long currentTime = System.currentTimeMillis();
        if (AttackDelayTutorHUD.expirationTime > currentTime && !AttackDelayTutorHUD.message.isEmpty()) {
            MinecraftClient client = MinecraftClient.getInstance();
            int screenWidth = client.getWindow().getScaledWidth();
            int screenHeight = client.getWindow().getScaledHeight();
            Text text = Text.literal(AttackDelayTutorHUD.message);
            int textWidth = client.textRenderer.getWidth(text);
            int x = (screenWidth - textWidth) / 2;
            int y = screenHeight / 2 + 25;
            int xOffset = 25;

            matrices.push();
            float scale = 0.7F;
            matrices.scale(scale, scale, scale);
            client.textRenderer.draw(matrices, text, (x + xOffset) / scale, y / scale, 0xFF0000);
            matrices.pop();
        }
    }
}
