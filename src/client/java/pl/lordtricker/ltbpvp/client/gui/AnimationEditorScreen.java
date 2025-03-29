package pl.lordtricker.ltbpvp.client.gui;

import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.client.gui.widget.SliderWidget;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.text.Text;
import pl.lordtricker.ltbpvp.client.config.ModSettings;
import pl.lordtricker.ltbpvp.client.config.ModSettings.AnimationOffsets;
import pl.lordtricker.ltbpvp.client.enums.SwingStyle;

public class AnimationEditorScreen extends Screen {

    private final Screen parentScreen;

    private ButtonWidget swingStyleButton;
    private OffsetSliderWidget sliderX;
    private OffsetSliderWidget sliderY;
    private OffsetSliderWidget sliderZ;
    private ButtonWidget resetButton;
    private ButtonWidget saveButton;

    private final int totalLines = 5;
    private final int rowSpacing = 25;
    private final int widgetWidth = 150;
    private final int widgetHeight = 20;

    private int startY;

    public AnimationEditorScreen(Screen parentScreen) {
        super(Text.literal("Edit - Sword Animation"));
        this.parentScreen = parentScreen;
    }

    @Override
    protected void init() {
        int centerX = this.width / 2;

        int totalBlockHeight = totalLines * rowSpacing;
        this.startY = (this.height - totalBlockHeight) / 2;

        int line1Y = startY;
        swingStyleButton = ButtonWidget.builder(
                Text.of("Style: " + getSwingStyleDisplay(ModSettings.swingStyle)),
                btn -> {
                    ModSettings.swingStyle = getNextSwingStyle(ModSettings.swingStyle);
                    btn.setMessage(Text.of("Style: " + getSwingStyleDisplay(ModSettings.swingStyle)));
                    refreshSliders();
                }
        ).dimensions(centerX - widgetWidth / 2, line1Y, widgetWidth, widgetHeight).build();
        addDrawableChild(swingStyleButton);

        AnimationOffsets offsets = ModSettings.styleOffsets.get(ModSettings.swingStyle);

        int line2Y = startY + rowSpacing;
        sliderX = createOffsetSlider("X", offsets.offsetX, centerX - widgetWidth / 2, line2Y, widgetWidth, widgetHeight);
        addDrawableChild(sliderX);

        int line3Y = startY + 2 * rowSpacing;
        sliderY = createOffsetSlider("Y", offsets.offsetY, centerX - widgetWidth / 2, line3Y, widgetWidth, widgetHeight);
        addDrawableChild(sliderY);

        int line4Y = startY + 3 * rowSpacing;
        sliderZ = createOffsetSlider("Z", offsets.offsetZ, centerX - widgetWidth / 2, line4Y, widgetWidth, widgetHeight);
        addDrawableChild(sliderZ);

        int line5Y = startY + 4 * rowSpacing;
        resetButton = ButtonWidget.builder(
                Text.of("Reset"),
                btn -> {
                    offsets.offsetX = 0.0f;
                    offsets.offsetY = 0.0f;
                    offsets.offsetZ = 0.0f;
                    sliderX.setSliderValue(normalizeOffset(offsets.offsetX));
                    sliderY.setSliderValue(normalizeOffset(offsets.offsetY));
                    sliderZ.setSliderValue(normalizeOffset(offsets.offsetZ));
                }
        ).dimensions(centerX - widgetWidth / 2, line5Y + 5, widgetWidth, widgetHeight).build();
        addDrawableChild(resetButton);

        int saveBtnWidth = 100;
        int saveBtnX = centerX - saveBtnWidth / 2;

        saveButton = ButtonWidget.builder(
                Text.of("Save"),
                btn -> {
                    this.client.setScreen(parentScreen);
                }
        ).dimensions(saveBtnX, this.height - 30, saveBtnWidth, widgetHeight).build();
        addDrawableChild(saveButton);
    }

    /**
     * Odświeża suwaki, gdy zmieniamy styl animacji miecza.
     */
    private void refreshSliders() {
        AnimationOffsets off = ModSettings.styleOffsets.get(ModSettings.swingStyle);
        sliderX.setSliderValue(normalizeOffset(off.offsetX));
        sliderY.setSliderValue(normalizeOffset(off.offsetY));
        sliderZ.setSliderValue(normalizeOffset(off.offsetZ));
    }

    /**
     * Tworzy slider offsetu (X/Y/Z).
     */
    private OffsetSliderWidget createOffsetSlider(String axis, float initial, int x, int y, int w, int h) {
        double val = normalizeOffset(initial);
        return new OffsetSliderWidget(x, y, w, h, Text.literal(axis + ": " + String.format("%.2f", initial)), val, axis);
    }

    /**
     * Konwersja float offset -> [0..1] dla slidera (zakładamy zakres od -2.0 do +2.0).
     */
    private double normalizeOffset(float off) {
        double sliderVal = (off + 2.0) / 4.0;
        if (sliderVal < 0) sliderVal = 0;
        if (sliderVal > 1) sliderVal = 1;
        return sliderVal;
    }

    /**
     * Konwersja wartości slidera (0..1) -> float offset (-2..+2).
     */
    private float denormalizeOffset(double sliderVal) {
        return (float)(sliderVal * 4.0 - 2.0);
    }

    private String getSwingStyleDisplay(SwingStyle style) {
        return switch (style) {
            case BASIC_SWING -> "BASIC";
            case BASIC_CLAP -> "BASIC_CLAP";
            case SWIPE_IN -> "SWIPE_IN";
            case SWIPE_OUT -> "SWIPE_OUT";
            case NO_SWING -> "NO_SWING";
        };
    }

    private SwingStyle getNextSwingStyle(SwingStyle current) {
        SwingStyle[] all = SwingStyle.values();
        int index = (current.ordinal() + 1) % all.length;
        return all[index];
    }

    @Override
    public void render(MatrixStack matrices, int mouseX, int mouseY, float delta) {
        this.renderBackground(matrices);
        super.render(matrices, mouseX, mouseY, delta);

        drawCenteredTextLocal(matrices, this.title, 10, 0xFFFFFF);
    }

    /**
     * Metoda rysująca wycentrowany tekst w podanym Y, aby kod był czytelny.
     */
    private void drawCenteredTextLocal(MatrixStack matrices, Text text, int y, int color) {
        int textWidth = this.textRenderer.getWidth(text);
        int x = (this.width - textWidth) / 2;
        this.textRenderer.draw(matrices, text, (float)x, (float)y, color);
    }

    /**
     * Klasa wewnętrzna SliderWidget dostosowana do zapisywania offsetów
     * w ModSettings.styleOffsets.
     */
    private class OffsetSliderWidget extends SliderWidget {
        private final String axis;
        private double customValue;

        public OffsetSliderWidget(int x, int y, int width, int height, Text message, double value, String axis) {
            super(x, y, width, height, message, value);
            this.axis = axis;
            this.customValue = value;
        }

        public double getSliderValue() {
            return this.customValue;
        }

        public void setSliderValue(double newValue) {
            this.customValue = newValue;
            try {
                var field = SliderWidget.class.getDeclaredField("value");
                field.setAccessible(true);
                field.set(this, newValue);
            } catch (Exception e) {
                e.printStackTrace();
            }
            updateMessage();
        }

        @Override
        protected void updateMessage() {
            float offVal = (float)(this.customValue * 4.0 - 2.0);
            this.setMessage(Text.literal(axis + ": " + String.format("%.2f", offVal)));
        }

        @Override
        protected void applyValue() {
            this.customValue = this.value;
            updateMessage();

            AnimationOffsets off = ModSettings.styleOffsets.get(ModSettings.swingStyle);
            float realVal = (float)(this.customValue * 4.0 - 2.0);

            switch (axis) {
                case "X" -> off.offsetX = realVal;
                case "Y" -> off.offsetY = realVal;
                case "Z" -> off.offsetZ = realVal;
            }
        }
    }
}
