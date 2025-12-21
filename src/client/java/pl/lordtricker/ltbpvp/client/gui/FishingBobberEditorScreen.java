package pl.lordtricker.ltbpvp.client.gui;

import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.client.gui.widget.SliderWidget;
import net.minecraft.text.Text;
import pl.lordtricker.ltbpvp.core.config.CoreSettings;
import pl.lordtricker.ltbpvp.core.logic.FishingBobberLogic;

public class FishingBobberEditorScreen extends Screen {
    private final Screen parent;
    private OffsetSlider offsetSlider;
    private ScaleSlider scaleSlider;

    private static final int W = 160;
    private static final int H = 20;
    private static final int SPACE = 25;

    public FishingBobberEditorScreen(Screen parent) {
        super(Text.literal("Edit - Fishing Bobber"));
        this.parent = parent;
    }

    @Override
    protected void init() {
        int cx = this.width / 2;
        int y = (this.height - SPACE * 2) / 2;

        float offset = FishingBobberLogic.clampOffsetY(CoreSettings.fishingBobberOffsetY);
        offsetSlider = new OffsetSlider(cx - W / 2, y, W, H, offset);
        addDrawableChild(offsetSlider);

        y += SPACE + 5;
        float scale = FishingBobberLogic.clampScale(CoreSettings.fishingBobberScale);
        scaleSlider = new ScaleSlider(cx - W / 2, y, W, H, scale);
        addDrawableChild(scaleSlider);

        y += SPACE + 5;
        ButtonWidget reset = ButtonWidget.builder(Text.of("Reset"), b -> {
            CoreSettings.fishingBobberOffsetY = 0f;
            CoreSettings.fishingBobberScale = 1.0f;
            offsetSlider.setSliderValue(0f);
            scaleSlider.setSliderValue(1.0f);
        }).dimensions(cx - W / 2, y, W, H).build();
        addDrawableChild(reset);

        ButtonWidget save = ButtonWidget.builder(Text.of("Save"), b -> {
            offsetSlider.applySlider();
            scaleSlider.applySlider();
            this.client.setScreen(parent);
        }).dimensions(cx - 50, this.height - 30, 100, H).build();
        addDrawableChild(save);
    }

    @Override
    public void render(DrawContext ctx, int mouseX, int mouseY, float delta) {
        this.renderBackground(ctx, mouseX, mouseY, delta);
        super.render(ctx, mouseX, mouseY, delta);
        int w = this.textRenderer.getWidth(this.title);
        ctx.drawText(this.textRenderer, this.title, (this.width - w) / 2, 10, 0xFFFFFF, false);
    }

    private static class OffsetSlider extends SliderWidget {
        OffsetSlider(int x, int y, int w, int h, float offset) {
            super(x, y, w, h,
                    Text.literal("Offset Y: " + String.format("%.2f", offset)),
                    normalize(offset));
        }

        @Override
        protected void updateMessage() {
            float val = denormalize(value);
            this.setMessage(Text.literal("Offset Y: " + String.format("%.2f", val)));
        }

        @Override
        protected void applyValue() {
            CoreSettings.fishingBobberOffsetY = denormalize(value);
        }

        void applySlider() {
            applyValue();
        }

        void setSliderValue(float v) {
            this.value = normalize(FishingBobberLogic.clampOffsetY(v));
            updateMessage();
        }

        private static double normalize(float v) {
            return (v + 1.0) / 2.0;
        }

        private static float denormalize(double v) {
            return (float) (v * 2.0 - 1.0);
        }
    }

    private static class ScaleSlider extends SliderWidget {
        ScaleSlider(int x, int y, int w, int h, float scale) {
            super(x, y, w, h,
                    Text.literal("Scale: " + String.format("%.2f", scale)),
                    normalize(scale));
        }

        @Override
        protected void updateMessage() {
            float val = denormalize(value);
            this.setMessage(Text.literal("Scale: " + String.format("%.2f", val)));
        }

        @Override
        protected void applyValue() {
            CoreSettings.fishingBobberScale = denormalize(value);
        }

        void applySlider() {
            applyValue();
        }

        void setSliderValue(float v) {
            this.value = normalize(FishingBobberLogic.clampScale(v));
            updateMessage();
        }

        private static double normalize(float v) {
            return (v - 0.2) / 1.8;
        }

        private static float denormalize(double v) {
            return (float) (v * 1.8 + 0.2);
        }
    }
}
