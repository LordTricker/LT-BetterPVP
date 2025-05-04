package pl.lordtricker.ltbpvp.client.gui;

import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.client.gui.widget.SliderWidget;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.text.Text;
import pl.lordtricker.ltbpvp.client.config.ModSettings;
import pl.lordtricker.ltbpvp.client.config.ModSettings.AnimationOffsets;

public class OffHandAnimationEditorScreen extends Screen {
    private final Screen parent;
    private OffsetSliderWidget sliderX, sliderY, sliderZ;
    private ButtonWidget resetButton, saveButton;
    private static final int ROW_SPACING = 25;
    private static final int WIDGET_WIDTH = 150;
    private static final int WIDGET_HEIGHT = 20;

    public OffHandAnimationEditorScreen(Screen parent) {
        super(Text.literal("Edit - OffHand Animation"));
        this.parent = parent;
    }

    @Override
    protected void init() {
        int centerX = this.width / 2;
        int totalHeight = 3 * ROW_SPACING + 10;
        int startY = (this.height - totalHeight) / 2;
        int y = startY;

        // X offset slider
        sliderX = new OffsetSliderWidget(
                "X", centerX - WIDGET_WIDTH / 2, y, WIDGET_WIDTH, WIDGET_HEIGHT,
                ModSettings.offhandOffsets.offsetX
        );
        addDrawableChild(sliderX);

        // Y offset slider
        y += ROW_SPACING;
        sliderY = new OffsetSliderWidget(
                "Y", centerX - WIDGET_WIDTH / 2, y, WIDGET_WIDTH, WIDGET_HEIGHT,
                ModSettings.offhandOffsets.offsetY
        );
        addDrawableChild(sliderY);

        // Z offset slider
        y += ROW_SPACING;
        sliderZ = new OffsetSliderWidget(
                "Z", centerX - WIDGET_WIDTH / 2, y, WIDGET_WIDTH, WIDGET_HEIGHT,
                ModSettings.offhandOffsets.offsetZ
        );
        addDrawableChild(sliderZ);

        // Reset button
        y += ROW_SPACING + 5;
        resetButton = ButtonWidget.builder(
                Text.of("Reset"),
                btn -> {
                    ModSettings.offhandOffsets.offsetX = 0f;
                    ModSettings.offhandOffsets.offsetY = 0f;
                    ModSettings.offhandOffsets.offsetZ = 0f;
                    sliderX.setSliderValue(0f);
                    sliderY.setSliderValue(0f);
                    sliderZ.setSliderValue(0f);
                }
        ).dimensions(centerX - WIDGET_WIDTH / 2, y, WIDGET_WIDTH, WIDGET_HEIGHT).build();
        addDrawableChild(resetButton);

        // Save button
        saveButton = ButtonWidget.builder(
                Text.of("Save"),
                btn -> {
                    sliderX.applySlider();
                    sliderY.applySlider();
                    sliderZ.applySlider();
                    this.client.setScreen(parent);
                }
        ).dimensions(centerX - 50, this.height - 30, 100, WIDGET_HEIGHT).build();
        addDrawableChild(saveButton);
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

    /**
     * SliderWidget for offhand offsets, mapping float offsets (-2..+2) to slider values.
     */
    private class OffsetSliderWidget extends SliderWidget {
        private final String axis;

        public OffsetSliderWidget(String axis, int x, int y, int width, int height, float initialOffset) {
            super(x, y, width, height,
                    Text.literal(axis + ": " + String.format("%.2f", initialOffset)),
                    normalize(initialOffset)
            );
            this.axis = axis;
        }

        @Override
        protected void updateMessage() {
            float offset = denormalize(this.value);
            this.setMessage(Text.literal(axis + ": " + String.format("%.2f", offset)));
        }

        @Override
        protected void applyValue() {
            float offset = denormalize(this.value);
            AnimationOffsets off = ModSettings.offhandOffsets;
            switch (axis) {
                case "X" -> off.offsetX = offset;
                case "Y" -> off.offsetY = offset;
                case "Z" -> off.offsetZ = offset;
            }
        }

        public void applySlider() {
            this.applyValue();
        }

        public void setSliderValue(float offset) {
            this.value = normalize(offset);
            updateMessage();
        }

        private static double normalize(float offset) {
            return (offset + 2.0) / 4.0;
        }

        private static float denormalize(double sliderVal) {
            return (float)(sliderVal * 4.0 - 2.0);
        }
    }
}