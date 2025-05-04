package pl.lordtricker.ltbpvp.client.gui;

import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.client.gui.widget.SliderWidget;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.text.Text;
import pl.lordtricker.ltbpvp.client.config.ModSettings;

public class ArmorStatusEditorScreen extends Screen {

    private final Screen parent;

    private ButtonWidget soundToggle;
    private ButtonWidget textToggle;
    private ThresholdSlider thresholdSlider;

    private static final int W = 150, H = 20, SPACE = 25;

    public ArmorStatusEditorScreen(Screen parent) {
        super(Text.literal("Edit – Armor status"));
        this.parent = parent;
    }

    @Override
    protected void init() {
        int cx = this.width / 2;
        int y  = (this.height - 4 * SPACE) / 2;

        soundToggle = ButtonWidget.builder(
                Text.of("Sound: " + (ModSettings.armorStatusSoundEnabled ? "ON" : "OFF")),
                b -> {
                    ModSettings.armorStatusSoundEnabled = !ModSettings.armorStatusSoundEnabled;
                    b.setMessage(Text.of("Sound: " + (ModSettings.armorStatusSoundEnabled ? "ON" : "OFF")));
                }
        ).dimensions(cx - W / 2, y, W, H).build();
        addDrawableChild(soundToggle);

        y += SPACE;
        textToggle = ButtonWidget.builder(
                Text.of("Text: " + (ModSettings.armorStatusTextEnabled ? "ON" : "OFF")),
                b -> {
                    ModSettings.armorStatusTextEnabled = !ModSettings.armorStatusTextEnabled;
                    b.setMessage(Text.of("Text: " + (ModSettings.armorStatusTextEnabled ? "ON" : "OFF")));
                }
        ).dimensions(cx - W / 2, y, W, H).build();
        addDrawableChild(textToggle);

        y += SPACE;
        thresholdSlider = new ThresholdSlider(cx - W / 2, y, W, H,
                ModSettings.armorStatusThreshold / 100.0);
        addDrawableChild(thresholdSlider);

        y += SPACE + 5;
        ButtonWidget reset = ButtonWidget.builder(Text.of("Reset"), b -> {
            ModSettings.armorStatusSoundEnabled = true;
            ModSettings.armorStatusTextEnabled  = true;
            ModSettings.armorStatusThreshold    = 25;
            soundToggle.setMessage(Text.of("Sound: ON"));
            textToggle .setMessage(Text.of("Text:  ON"));
            thresholdSlider.setSliderValue(0.25);
        }).dimensions(cx - W / 2, y, W, H).build();
        addDrawableChild(reset);

        // Save
        ButtonWidget save = ButtonWidget.builder(Text.of("Save"), b -> {
            ModSettings.armorStatusThreshold =
                    (int)(thresholdSlider.getSliderValue() * 100);
            this.client.setScreen(parent);
        }).dimensions(cx - 50, this.height - 30, 100, H).build();
        addDrawableChild(save);
    }

    @Override
    public void render(MatrixStack matrices, int mouseX, int mouseY, float delta) {
        this.renderBackground(matrices);
        super.render(matrices, mouseX, mouseY, delta);
        drawCenteredTextLocal(matrices, this.title, 10, 0xFFFFFF);
    }

    private void drawCenteredTextLocal(MatrixStack matrices, Text text, int y, int color) {
        int textWidth = this.textRenderer.getWidth(text);
        int x = (this.width - textWidth) / 2;
        this.textRenderer.draw(matrices, text, (float) x, (float) y, color);
    }

    /* ---------- slider ------------ */
    private class ThresholdSlider extends SliderWidget {
        ThresholdSlider(int x, int y, int w, int h, double value) {
            super(x, y, w, h, Text.empty(), value);
            updateMessage();
        }
        @Override protected void updateMessage() {
            this.setMessage(Text.literal("Threshold: " + (int)(value * 100) + "%"));
        }
        @Override protected void applyValue() { /* wartość odczytujemy przy Save */ }
        double getSliderValue() { return value; }
        void   setSliderValue(double v) { value = v; updateMessage(); }
    }
}