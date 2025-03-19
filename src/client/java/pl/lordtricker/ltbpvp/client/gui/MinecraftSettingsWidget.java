package pl.lordtricker.ltbpvp.client.gui;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.text.Text;

public class MinecraftSettingsWidget {
    private ButtonWidget autoJumpButton;
    private ButtonWidget bobViewButton;

    public void initWidgets(int x, int y, int width, int btnHeight, int rowSpacing) {
        MinecraftClient client = MinecraftClient.getInstance();

        boolean autoJump = client.options.getAutoJump().getValue();
        autoJumpButton = new ButtonWidget(
                x,
                y,
                width,
                btnHeight,
                Text.of(getToggleDisplay(autoJump)),
                btn -> {
                    boolean current = client.options.getAutoJump().getValue();
                    client.options.getAutoJump().setValue(!current);
                    client.options.write();
                    btn.setMessage(Text.of(getToggleDisplay(!current)));
                }
        );

        boolean bobView = client.options.getBobView().getValue();
        bobViewButton = new ButtonWidget(
                x,
                y + rowSpacing,
                width,
                btnHeight,
                Text.of(getToggleDisplay(bobView)),
                btn -> {
                    boolean current = client.options.getBobView().getValue();
                    client.options.getBobView().setValue(!current);
                    client.options.write();
                    btn.setMessage(Text.of(getToggleDisplay(!current)));
                }
        );
    }

    public ButtonWidget[] getWidgets() {
        return new ButtonWidget[] {
                autoJumpButton,
                bobViewButton,
        };
    }

    private String getToggleDisplay(boolean value) {
        return value ? "ON" : "OFF";
    }
}
