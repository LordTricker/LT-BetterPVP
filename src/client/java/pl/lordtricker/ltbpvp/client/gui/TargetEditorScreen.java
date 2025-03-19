package pl.lordtricker.ltbpvp.client.gui;

import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.client.gui.widget.SliderWidget;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.text.Text;
import pl.lordtricker.ltbpvp.client.config.ModSettings;
import pl.lordtricker.ltbpvp.client.enums.CrosshairColor;
import pl.lordtricker.ltbpvp.client.enums.TargetStyle;

public class TargetEditorScreen extends Screen {

    private final Screen parentScreen;

    private final int totalLines = 4;
    private final int rowSpacing = 25;
    private final int widgetWidth = 150;
    private final int widgetHeight = 20;

    private int startY;

    private ButtonWidget targetStyleButton;
    private ButtonWidget colorButton;
    private RangeSliderWidget rangeSlider;
    private ButtonWidget resetButton;
    private ButtonWidget backButton;

    public TargetEditorScreen(Screen parentScreen) {
        super(Text.literal("Edit - Cursor ESP"));
        this.parentScreen = parentScreen;
    }

    @Override
    protected void init() {
        int centerX = this.width / 2;

        int totalBlockHeight = totalLines * rowSpacing + 10;
        this.startY = (this.height - totalBlockHeight) / 2;

        int line1Y = startY;
        targetStyleButton = ButtonWidget.builder(
                Text.of("Style: " + getTargetStyleDisplay(ModSettings.targetStyle)),
                button -> {
                    ModSettings.targetStyle = getNextTargetStyle(ModSettings.targetStyle);
                    button.setMessage(Text.of("Style: " + getTargetStyleDisplay(ModSettings.targetStyle)));
                }
        ).dimensions(centerX - widgetWidth / 2, line1Y, widgetWidth, widgetHeight).build();
        addDrawableChild(targetStyleButton);

        int line2Y = startY + rowSpacing;
        colorButton = ButtonWidget.builder(
                Text.of("Color: " + ModSettings.crosshairColor.name()),
                btn -> {
                    CrosshairColor next = getNextColor(ModSettings.crosshairColor);
                    ModSettings.crosshairColor = next;
                    btn.setMessage(Text.of("Color: " + next.name()));
                }
        ).dimensions(centerX - widgetWidth / 2, line2Y, widgetWidth, widgetHeight).build();
        addDrawableChild(colorButton);

        int line3Y = startY + 2 * rowSpacing;
        double initialNormalized = (ModSettings.targetRange - 16) / 32.0;
        rangeSlider = new RangeSliderWidget(
                centerX - widgetWidth / 2,
                line3Y,
                widgetWidth,
                widgetHeight,
                Text.literal("Size: " + ModSettings.targetRange + "px"),
                initialNormalized
        );
        addDrawableChild(rangeSlider);

        int line4Y = startY + 3 * rowSpacing + 10;
        resetButton = ButtonWidget.builder(
                Text.of("Reset"),
                button -> {
                    double defValue = (32 - 16) / 32.0;
                    rangeSlider.setSliderValue(defValue);

                    ModSettings.crosshairColor = CrosshairColor.RGB;
                    colorButton.setMessage(Text.of("Color: RGB"));
                }
        ).dimensions(centerX - widgetWidth / 2, line4Y + 5, widgetWidth, widgetHeight).build();
        addDrawableChild(resetButton);

        int backBtnWidth = 100;
        int backBtnX = centerX - backBtnWidth / 2;
        backButton = ButtonWidget.builder(
                Text.of("Save"),
                button -> {
                    ModSettings.targetRange = (int) (16 + rangeSlider.getSliderValue() * 32);
                    this.client.setScreen(parentScreen);
                }
        ).dimensions(backBtnX, this.height - 30, backBtnWidth, widgetHeight).build();
        addDrawableChild(backButton);
    }

    private CrosshairColor getNextColor(CrosshairColor current) {
        CrosshairColor[] vals = CrosshairColor.values();
        int idx = (current.ordinal() + 1) % vals.length;
        return vals[idx];
    }

    private String getTargetStyleDisplay(TargetStyle style) {
        return style.name();
    }

    private TargetStyle getNextTargetStyle(TargetStyle current) {
        TargetStyle[] styles = TargetStyle.values();
        int index = (current.ordinal() + 1) % styles.length;
        return styles[index];
    }

    @Override
    public void render(MatrixStack matrices, int mouseX, int mouseY, float delta) {
        this.renderBackground(matrices);
        super.render(matrices, mouseX, mouseY, delta);
        drawCenteredTextLocal(matrices, this.title, 10, 0xFFFFFF);
    }

    /**
     * Rysuje wycentrowany tekst na zadanym Y (tu: 10).
     */
    private void drawCenteredTextLocal(MatrixStack matrices, Text text, int y, int color) {
        int textWidth = this.textRenderer.getWidth(text);
        int x = (this.width - textWidth) / 2;
        this.textRenderer.draw(matrices, text, (float) x, (float) y, color);
    }

    /**
     * Slider odpowiedzialny za zakres celownika.
     */
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
        }

        public double getSliderValue() {
            return this.value;
        }

        public void setSliderValue(double newValue) {
            this.value = newValue;
            updateMessage();
        }
    }
}
