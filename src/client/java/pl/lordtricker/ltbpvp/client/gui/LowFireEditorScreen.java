package pl.lordtricker.ltbpvp.client.gui;

import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.client.gui.widget.SliderWidget;
import net.minecraft.text.Text;
import net.minecraft.util.math.MathHelper;
import pl.lordtricker.ltbpvp.client.config.ModSettings;

public class LowFireEditorScreen extends Screen {
    private final Screen parent;
    private HeightSlider slider;

    private static final int W = 150, H = 20, SPACE = 25;

    public LowFireEditorScreen(Screen parent) {
        super(Text.literal("Edit - Low Fire"));
        this.parent = parent;
    }

    @Override
    protected void init() {
        int cx = this.width / 2;
        int y  = (this.height - SPACE) / 2;

        float clamped = MathHelper.clamp(ModSettings.lowFireHeight, -1f, 1f);
        slider = new HeightSlider(cx - W / 2, y, W, H, clamped);
        addDrawableChild(slider);

        y += SPACE + 5;
        ButtonWidget reset = ButtonWidget.builder(Text.of("Reset"), b -> {
            ModSettings.lowFireHeight = 0f;
            slider.setSliderValue(0f);
        }).dimensions(cx - W / 2, y, W, H).build();
        addDrawableChild(reset);

        ButtonWidget save = ButtonWidget.builder(Text.of("Save"), b -> {
            slider.applySlider();
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

    private static class HeightSlider extends SliderWidget {
        HeightSlider(int x, int y, int w, int h, float height) {
            super(x, y, w, h,
                    Text.literal("Height: " + String.format("%.2f", height)),
                    normalize(height));
        }

        @Override
        protected void updateMessage() {
            float val = denormalize(value);
            this.setMessage(Text.literal("Height: " + String.format("%.2f", val)));
        }

        @Override
        protected void applyValue() {
            ModSettings.lowFireHeight = denormalize(value);
        }

        void applySlider() { applyValue(); }

        void setSliderValue(float v) {
            this.value = normalize(MathHelper.clamp(v, -1f, 1f));
            updateMessage();
        }

        private static double normalize(float v) {
            return (v + 1.0) / 2.0;
        }

        private static float denormalize(double v) {
            return (float)(v * 2.0 - 1.0);
        }
    }
}
