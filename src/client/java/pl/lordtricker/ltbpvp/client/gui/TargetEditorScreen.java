package pl.lordtricker.ltbpvp.client.gui;

import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.client.gui.widget.SliderWidget;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.text.Text;
import pl.lordtricker.ltbpvp.client.config.ModSettings;
import pl.lordtricker.ltbpvp.client.enums.TargetStyle;

/**
 * Wersja zgodna z&nbsp;Minecraft&nbsp;1.19.2 – bez API buildera przycisków.
 */
public class TargetEditorScreen extends Screen {

    private final Screen parentScreen;
    private static final int ROW_SPACING = 25;
    private static final int WIDGET_WIDTH = 150;
    private static final int WIDGET_HEIGHT = 20;
    private static final int TOTAL_LINES = 6;

    private ButtonWidget rgbToggleButton;
    private ColorSliderWidget redSlider;
    private ColorSliderWidget greenSlider;
    private ColorSliderWidget blueSlider;
    private RangeSliderWidget rangeSlider;

    public TargetEditorScreen(Screen parent) {
        super(Text.literal("Edit - Cursor ESP"));
        this.parentScreen = parent;
    }

    @Override
    protected void init() {
        int centerX = this.width / 2;
        int totalHeight = TOTAL_LINES * ROW_SPACING + 10;
        int startY = (this.height - totalHeight) / 2;
        int y = startY;

        /* --- Styl celownika -------------------------------------- */
        ButtonWidget targetStyleButton = new ButtonWidget(
                centerX - WIDGET_WIDTH / 2,
                y,
                WIDGET_WIDTH,
                WIDGET_HEIGHT,
                Text.of("Style: " + ModSettings.targetStyle.name()),
                button -> {
                    TargetStyle[] styles = TargetStyle.values();
                    int idx = (ModSettings.targetStyle.ordinal() + 1) % styles.length;
                    ModSettings.targetStyle = styles[idx];
                    button.setMessage(Text.of("Style: " + ModSettings.targetStyle.name()));
                }
        );
        addDrawableChild(targetStyleButton);

        /* --- Przełącznik RGB ------------------------------------- */
        y += ROW_SPACING;
        rgbToggleButton = new ButtonWidget(
                centerX - WIDGET_WIDTH / 2,
                y,
                WIDGET_WIDTH,
                WIDGET_HEIGHT,
                Text.of("RGB Color: " + (ModSettings.rgbEnabled ? "ON" : "OFF")),
                button -> {
                    ModSettings.rgbEnabled = !ModSettings.rgbEnabled;
                    button.setMessage(Text.of("RGB Color: " + (ModSettings.rgbEnabled ? "ON" : "OFF")));
                }
        );
        addDrawableChild(rgbToggleButton);

        /* --- Suwaki kolorów -------------------------------------- */
        y += ROW_SPACING;
        redSlider = new ColorSliderWidget("Red", centerX - WIDGET_WIDTH / 2, y, WIDGET_WIDTH, WIDGET_HEIGHT, ModSettings.customRed);
        addDrawableChild(redSlider);

        y += ROW_SPACING;
        greenSlider = new ColorSliderWidget("Green", centerX - WIDGET_WIDTH / 2, y, WIDGET_WIDTH, WIDGET_HEIGHT, ModSettings.customGreen);
        addDrawableChild(greenSlider);

        y += ROW_SPACING;
        blueSlider = new ColorSliderWidget("Blue", centerX - WIDGET_WIDTH / 2, y, WIDGET_WIDTH, WIDGET_HEIGHT, ModSettings.customBlue);
        addDrawableChild(blueSlider);

        /* --- Suwak rozmiaru -------------------------------------- */
        y += ROW_SPACING;
        double normalized = (ModSettings.targetRange - 16) / 32.0;
        rangeSlider = new RangeSliderWidget(
                centerX - WIDGET_WIDTH / 2,
                y,
                WIDGET_WIDTH,
                WIDGET_HEIGHT,
                Text.literal("Size: " + ModSettings.targetRange + "px"),
                normalized
        );
        addDrawableChild(rangeSlider);

        /* --- Reset ----------------------------------------------- */
        y += ROW_SPACING + 5;
        ButtonWidget resetButton = new ButtonWidget(
                centerX - WIDGET_WIDTH / 2,
                y,
                WIDGET_WIDTH,
                WIDGET_HEIGHT,
                Text.of("Reset"),
                button -> {
                    ModSettings.rgbEnabled = true;
                    ModSettings.customRed   = 1.0f;
                    ModSettings.customGreen = 1.0f;
                    ModSettings.customBlue  = 1.0f;
                    ModSettings.targetRange = 24;

                    rgbToggleButton.setMessage(Text.of("RGB Color: ON"));
                    redSlider.setSliderValue(ModSettings.customRed);
                    greenSlider.setSliderValue(ModSettings.customGreen);
                    blueSlider.setSliderValue(ModSettings.customBlue);
                    rangeSlider.setSliderValue((ModSettings.targetRange - 16) / 32.0);
                }
        );
        addDrawableChild(resetButton);

        /* --- Save ------------------------------------------------- */
        ButtonWidget saveButton = new ButtonWidget(
                centerX - 50,
                this.height - 30,
                100,
                WIDGET_HEIGHT,
                Text.of("Save"),
                button -> {
                    redSlider.applySlider();
                    greenSlider.applySlider();
                    blueSlider.applySlider();
                    ModSettings.targetRange = (int) (16 + rangeSlider.getSliderValue() * 32);
                    this.client.setScreen(parentScreen);
                }
        );
        addDrawableChild(saveButton);
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

    /* --------------------------------------------------------------------- */

    private class ColorSliderWidget extends SliderWidget {
        private final String name;

        public ColorSliderWidget(String name, int x, int y, int width, int height, double value) {
            super(x, y, width, height, Text.literal(name + ": " + (int) (value * 255)), value);
            this.name = name;
        }

        @Override
        protected void updateMessage() {
            int v = (int) (this.value * 255);
            this.setMessage(Text.literal(name + ": " + v));
        }

        @Override
        protected void applyValue() {
            float f = (float) this.value;
            switch (name) {
                case "Red"   -> ModSettings.customRed   = f;
                case "Green" -> ModSettings.customGreen = f;
                case "Blue"  -> ModSettings.customBlue  = f;
            }
        }

        public void applySlider() {
            this.applyValue();
        }

        public void setSliderValue(double v) {
            this.value = v;
            updateMessage();
        }
    }

    private class RangeSliderWidget extends SliderWidget {
        public RangeSliderWidget(int x, int y, int width, int height, Text message, double value) {
            super(x, y, width, height, message, value);
        }

        @Override
        protected void updateMessage() {
            int range = (int) (16 + this.value * 32);
            this.setMessage(Text.literal("Size: " + range + "px"));
        }

        @Override
        protected void applyValue() {
            // logikę przenosimy po stronie przycisku Save
        }

        public double getSliderValue() {
            return this.value;
        }

        public void setSliderValue(double v) {
            this.value = v;
            updateMessage();
        }
    }
}