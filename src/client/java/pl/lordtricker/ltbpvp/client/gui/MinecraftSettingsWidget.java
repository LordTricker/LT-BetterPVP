package pl.lordtricker.ltbpvp.client.gui;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.text.Text;

public class MinecraftSettingsWidget {
    private ButtonWidget autoJumpButton;
    private ButtonWidget bobViewButton;
    private ButtonWidget damageTiltButton;

    private ButtonWidget gammaButton;

    public void initWidgets(int x, int y, int width, int btnHeight, int rowSpacing) {
        MinecraftClient client = MinecraftClient.getInstance();

        boolean autoJump = client.options.getAutoJump().getValue();
        autoJumpButton = ButtonWidget.builder(
                Text.of( getToggleDisplay(autoJump)),
                btn -> {
                    boolean current = client.options.getAutoJump().getValue();
                    client.options.getAutoJump().setValue(!current);
                    client.options.write();
                    btn.setMessage(Text.of(getToggleDisplay(!current)));
                }
        ).dimensions(x, y, width, btnHeight).build();

        boolean bobView = client.options.getBobView().getValue();
        bobViewButton = ButtonWidget.builder(
                Text.of(getToggleDisplay(bobView)),
                btn -> {
                    boolean current = client.options.getBobView().getValue();
                    client.options.getBobView().setValue(!current);
                    client.options.write();
                    btn.setMessage(Text.of(getToggleDisplay(!current)));
                }
        ).dimensions(x, y + rowSpacing, width, btnHeight).build();

        double tiltValue = client.options.getDamageTiltStrength().getValue();
        boolean tiltEnabled = tiltValue > 0.0;
        damageTiltButton = ButtonWidget.builder(
                Text.of(getToggleDisplay(tiltEnabled)),
                btn -> {
                    double curr = client.options.getDamageTiltStrength().getValue();
                    boolean currEnabled = (curr > 0.0);
                    double newVal = currEnabled ? 0.0 : 1.0;
                    client.options.getDamageTiltStrength().setValue(newVal);
                    client.options.write();
                    btn.setMessage(Text.of(getToggleDisplay(newVal > 0.0)));
                }
        ).dimensions(x, y + 2 * rowSpacing, width, btnHeight).build();
    }

    public ButtonWidget[] getWidgets() {
        return new ButtonWidget[] {
                autoJumpButton,
                bobViewButton,
                damageTiltButton,
        };
    }

    private String getToggleDisplay(boolean value) {
        return value ? "ON" : "OFF";
    }
}
