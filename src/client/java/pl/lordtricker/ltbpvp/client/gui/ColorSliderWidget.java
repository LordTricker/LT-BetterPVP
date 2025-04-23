package pl.lordtricker.ltbpvp.client.gui;

import net.minecraft.client.gui.widget.SliderWidget;
import net.minecraft.text.Text;
import pl.lordtricker.ltbpvp.client.config.ModSettings;


public class ColorSliderWidget extends SliderWidget {
    private final String name;

    public ColorSliderWidget(String name, int x, int y, int width, int height, double value) {
        super(x, y, width, height, Text.literal(name + ": " + (int)(value * 255)), value);
        this.name = name;
    }

    @Override
    protected void updateMessage() {
        int v = (int)(this.value * 255);
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
