package pl.lordtricker.ltbpvp.client.mixin;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.hud.InGameHud;
import net.minecraft.client.render.RenderTickCounter;
import net.minecraft.entity.player.ItemCooldownManager;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import pl.lordtricker.ltbpvp.core.config.CoreSettings;
import pl.lordtricker.ltbpvp.core.logic.CooldownTimerLogic;

import java.util.HashMap;
import java.util.Map;

@Mixin(InGameHud.class)
public class CooldownTimerMixin {
    private static final int FINISH_FLASH_TICKS = 6;
    private static final Map<Item, Integer> lastEndTicks = new HashMap<>();

    @Inject(
            method = "renderHotbarItem(Lnet/minecraft/client/gui/DrawContext;IILnet/minecraft/client/render/RenderTickCounter;Lnet/minecraft/entity/player/PlayerEntity;Lnet/minecraft/item/ItemStack;I)V",
            at = @At("TAIL"),
            require = 0
    )
    private void ltbpvp$renderCooldownText(DrawContext context,
                                          int x,
                                          int y,
                                          RenderTickCounter tickCounter,
                                          PlayerEntity player,
                                          ItemStack stack,
                                          int seed,
                                          CallbackInfo ci) {
        if (!CoreSettings.cooldownTimerEnabled) {
            return;
        }
        if (stack == null || stack.isEmpty()) {
            return;
        }
        ItemCooldownManager manager = player.getItemCooldownManager();
        if (!(manager instanceof ItemCooldownManagerAccessor accessor)) {
            return;
        }
        Map<Item, Object> entries = accessor.ltbpvp$getEntries();
        if (entries == null) {
            return;
        }
        Item item = stack.getItem();
        Object entry = entries.get(item);
        if (entry == null) {
            Integer lastEnd = lastEndTicks.get(item);
            if (lastEnd != null) {
                int ticksSinceEnd = accessor.ltbpvp$getTick() - lastEnd;
                if (ticksSinceEnd >= 0 && ticksSinceEnd <= FINISH_FLASH_TICKS) {
                    drawFinishFlash(context, x, y);
                } else if (ticksSinceEnd > FINISH_FLASH_TICKS) {
                    lastEndTicks.remove(item);
                }
            }
            return;
        }
        ItemCooldownEntryAccessor entryAccessor = (ItemCooldownEntryAccessor) entry;
        int startTick = entryAccessor.ltbpvp$getStartTick();
        int endTick = entryAccessor.ltbpvp$getEndTick();
        lastEndTicks.put(item, endTick);
        int remainingTicks = endTick - accessor.ltbpvp$getTick();
        int seconds = CooldownTimerLogic.secondsRemaining(remainingTicks);
        if (seconds <= 0) {
            return;
        }
        String text = Integer.toString(seconds);
        MinecraftClient client = MinecraftClient.getInstance();
        int width = client.textRenderer.getWidth(text);
        float scale = 1.3f;
        float centerX = x + 8;
        float centerY = y + 9;
        float drawX = (centerX - (width / 2.0f)) / scale;
        float drawY = (centerY - (client.textRenderer.fontHeight / 2.0f)) / scale;
        context.getMatrices().push();
        context.getMatrices().translate(0.0f, 0.0f, 200.0f);
        context.getMatrices().scale(scale, scale, 1.0f);
        context.drawText(client.textRenderer, text, Math.round(drawX), Math.round(drawY), 0xFFFF0000, true);
        context.getMatrices().pop();
        drawCooldownBar(context, x, y, startTick, endTick, remainingTicks);
    }

    private void drawCooldownBar(DrawContext context, int x, int y, int startTick, int endTick, int remainingTicks) {
        int total = endTick - startTick;
        if (total <= 0) {
            return;
        }
        float fraction = Math.min(1.0f, Math.max(0.0f, remainingTicks / (float) total));
        int barWidth = Math.max(1, Math.round(16 * fraction));
        context.fill(x, y, x + barWidth, y + 2, 0xFFFF0000);
    }

    private void drawFinishFlash(DrawContext context, int x, int y) {
        context.fill(x, y, x + 16, y + 2, 0xFF00FF00);
    }
}
