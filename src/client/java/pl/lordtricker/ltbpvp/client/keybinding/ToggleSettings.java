package pl.lordtricker.ltbpvp.client.keybinding;

import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.keybinding.v1.KeyBindingHelper;
import net.minecraft.client.option.KeyBinding;
import org.lwjgl.glfw.GLFW;
import pl.lordtricker.ltbpvp.client.gui.MainSettingsScreen;

public class ToggleSettings {

    private static KeyBinding toggleKey;

    public static void initKeybinds() {
        toggleKey = new KeyBinding(
                "GUI ustawień betterpvp",
                GLFW.GLFW_KEY_V,
                "LT-Mods binds"
        );
        KeyBindingHelper.registerKeyBinding(toggleKey);

        ClientTickEvents.END_CLIENT_TICK.register(client -> {
            while (toggleKey.wasPressed()) {
                long window = client.getWindow().getHandle();
                boolean ctrlPressed = GLFW.glfwGetKey(window, GLFW.GLFW_KEY_LEFT_CONTROL) == GLFW.GLFW_PRESS
                        || GLFW.glfwGetKey(window, GLFW.GLFW_KEY_RIGHT_CONTROL) == GLFW.GLFW_PRESS;
                if (!ctrlPressed) {
                    continue;
                }
                client.execute(() -> client.setScreen(new MainSettingsScreen()));
            }
        });
    }
}
