package pl.lordtricker.ltbpvp.client.gui;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.text.Text;

public class MinecraftSettingsWidget {
    private ButtonWidget autoJumpButton;
    private ButtonWidget bobViewButton;

    public void initWidgets(int x, int y, int width, int btnHeight, int rowSpacing) {
        MinecraftClient client = MinecraftClient.getInstance();

        boolean autoJump = client.options.autoJump;
        autoJumpButton = new ButtonWidget(
                x,
                y,
                width,
                btnHeight,
                Text.of(getToggleDisplay(autoJump)),
                btn -> {
                    boolean current = client.options.autoJump;
                    client.options.autoJump = !current;
                    client.options.write();
                    btn.setMessage(Text.of(getToggleDisplay(!current)));
                }
        );

        boolean bobView = client.options.bobView;
        bobViewButton = new ButtonWidget(
                x,
                y + rowSpacing,
                width,
                btnHeight,
                Text.of(getToggleDisplay(bobView)),
                btn -> {
                    boolean current = client.options.bobView;
                    client.options.bobView = !current;
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
