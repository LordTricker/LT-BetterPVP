package pl.lordtricker.ltbpvp.client.gui;

import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.client.gui.widget.SliderWidget;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.text.Text;
import pl.lordtricker.ltbpvp.client.config.ModSettings;

/**
 * Wersja zgodna z&nbsp;Minecraft&nbsp;1.19.2 – ButtonWidget bez buildera.
 */
public class ArmorStatusEditorScreen extends Screen {

    private final Screen parent;

    private ButtonWidget soundToggle;
    private ButtonWidget textToggle;
    private ThresholdSlider thresholdSlider;

    private static final int W = 150, H = 20, SPACE = 25;

    public ArmorStatusEditorScreen(Screen parent) {
        super(Text.of("Edit – Armor status"));
        this.parent = parent;
    }

    @Override
    protected void init() {
        int cx = this.width / 2;
        int y  = (this.height - 4 * SPACE) / 2;

        /* --- Sound toggle --------------------------------------- */
        soundToggle = new ButtonWidget(
                cx - W / 2,
                y,
                W,
                H,
                Text.of("Sound: " + (ModSettings.armorStatusSoundEnabled ? "ON" : "OFF")),
                button -> {
                    ModSettings.armorStatusSoundEnabled = !ModSettings.armorStatusSoundEnabled;
                    button.setMessage(Text.of("Sound: " + (ModSettings.armorStatusSoundEnabled ? "ON" : "OFF")));
                }
        );
        addDrawableChild(soundToggle);

        /* --- Text toggle ---------------------------------------- */
        y += SPACE;
        textToggle = new ButtonWidget(
                cx - W / 2,
                y,
                W,
                H,
                Text.of("Text: " + (ModSettings.armorStatusTextEnabled ? "ON" : "OFF")),
                button -> {
                    ModSettings.armorStatusTextEnabled = !ModSettings.armorStatusTextEnabled;
                    button.setMessage(Text.of("Text: " + (ModSettings.armorStatusTextEnabled ? "ON" : "OFF")));
                }
        );
        addDrawableChild(textToggle);

        /* --- Threshold slider ----------------------------------- */
        y += SPACE;
        thresholdSlider = new ThresholdSlider(cx - W / 2, y, W, H, ModSettings.armorStatusThreshold / 100.0);
        addDrawableChild(thresholdSlider);

        /* --- Reset ---------------------------------------------- */
        y += SPACE + 5;
        ButtonWidget reset = new ButtonWidget(
                cx - W / 2,
                y,
                W,
                H,
                Text.of("Reset"),
                button -> {
                    ModSettings.armorStatusSoundEnabled = true;
                    ModSettings.armorStatusTextEnabled  = true;
                    ModSettings.armorStatusThreshold    = 25;
                    soundToggle.setMessage(Text.of("Sound: ON"));
                    textToggle .setMessage(Text.of("Text:  ON"));
                    thresholdSlider.setSliderValue(0.25);
                }
        );
        addDrawableChild(reset);

        /* --- Save ----------------------------------------------- */
        ButtonWidget save = new ButtonWidget(
                cx - 50,
                this.height - 30,
                100,
                H,
                Text.of("Save"),
                button -> {
                    ModSettings.armorStatusThreshold = (int) (thresholdSlider.getSliderValue() * 100);
                    this.client.setScreen(parent);
                }
        );
        addDrawableChild(save);
    }

    /* --------------------------------------------------------------------- */

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
            super(x, y, w, h, Text.of(""), value);
            updateMessage();
        }

        @Override
        protected void updateMessage() {
            this.setMessage(Text.of("Threshold: " + (int) (value * 100) + "%"));
        }

        @Override
        protected void applyValue() {
            // odczytywana przy Save
        }

        double getSliderValue() {
            return value;
        }

        void setSliderValue(double v) {
            value = v;
            updateMessage();
        }
    }
}