package pl.lordtricker.ltbpvp.client.gui;

import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.client.gui.widget.SliderWidget;
import net.minecraft.text.Text;
import pl.lordtricker.ltbpvp.client.config.ModSettings;
import pl.lordtricker.ltbpvp.client.enums.CrosshairColor;
import pl.lordtricker.ltbpvp.client.enums.TargetStyle;

public class TargetEditorScreen extends Screen {
    private final Screen parentScreen;
    private ButtonWidget targetStyleButton;
    private ButtonWidget colorButton; // przycisk wyboru koloru
    private RangeSliderWidget rangeSlider;
    private ButtonWidget resetButton;
    private ButtonWidget backButton;

    public TargetEditorScreen(Screen parentScreen) {
        super(Text.literal("Edycja celownika"));
        this.parentScreen = parentScreen;
    }

    @Override
    protected void init() {
        int centerX = this.width / 2;
        int startY = 50;
        int sliderWidth = 150;
        int btnHeight = 20;
        int rowSpacing = 25;

        targetStyleButton = ButtonWidget.builder(
                Text.of("Celownik: " + getTargetStyleDisplay(ModSettings.targetStyle)),
                button -> {
                    ModSettings.targetStyle = getNextTargetStyle(ModSettings.targetStyle);
                    button.setMessage(Text.of("Celownik: " + getTargetStyleDisplay(ModSettings.targetStyle)));
                }
        ).dimensions(centerX - sliderWidth / 2, startY, sliderWidth, btnHeight).build();
        addDrawableChild(targetStyleButton);

        colorButton = ButtonWidget.builder(
                Text.of("Kolor: " + ModSettings.crosshairColor.name()),
                btn -> {
                    CrosshairColor next = getNextColor(ModSettings.crosshairColor);
                    ModSettings.crosshairColor = next;
                    btn.setMessage(Text.of("Kolor: " + next.name()));
                }
        ).dimensions(centerX - sliderWidth / 2, startY + rowSpacing, sliderWidth, btnHeight).build();
        addDrawableChild(colorButton);

        double initialNormalized = (ModSettings.targetRange - 16) / 32.0;
        rangeSlider = new RangeSliderWidget(
                centerX - sliderWidth / 2,
                startY + 2 * rowSpacing,
                sliderWidth,
                btnHeight,
                Text.literal("Rozmiar: " + ModSettings.targetRange + "px"),
                initialNormalized
        );
        addDrawableChild(rangeSlider);

        resetButton = ButtonWidget.builder(
                Text.of("Resetuj ustawienia"),
                button -> {
                    rangeSlider.setSliderValue((32 - 16) / 32.0);
                    ModSettings.crosshairColor = CrosshairColor.RGB;
                    colorButton.setMessage(Text.of("Kolor: RGB"));
                }
        ).dimensions(centerX - sliderWidth / 2, startY + 3 * rowSpacing + 10, sliderWidth, btnHeight).build();
        addDrawableChild(resetButton);

        backButton = ButtonWidget.builder(
                Text.of("Wstecz"),
                button -> {
                    ModSettings.targetRange = (int)(16 + rangeSlider.getSliderValue() * 32);
                    this.client.setScreen(parentScreen);
                }
        ).dimensions(centerX - 40, this.height - 30, 80, btnHeight).build();
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
    public void render(DrawContext context, int mouseX, int mouseY, float delta) {
        this.renderBackground(context, mouseX, mouseY, delta);
        super.render(context, mouseX, mouseY, delta);
        int textWidth = this.textRenderer.getWidth(this.title);
        context.drawText(this.textRenderer, this.title, (this.width - textWidth) / 2, 10, 0xFFFFFF, false);
    }

    private class RangeSliderWidget extends SliderWidget {
        public RangeSliderWidget(int x, int y, int width, int height, Text message, double value) {
            super(x, y, width, height, message, value);
        }

        @Override
        protected void updateMessage() {
            int range = (int)(16 + this.value * 32);
            this.setMessage(Text.literal("Rozmiar: " + range + "px"));
        }

        @Override
        protected void applyValue() {}

        public double getSliderValue() {
            return this.value;
        }

        public void setSliderValue(double newValue) {
            this.value = newValue;
            updateMessage();
        }
    }
}
