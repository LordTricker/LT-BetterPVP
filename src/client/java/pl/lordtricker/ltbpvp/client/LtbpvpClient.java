package pl.lordtricker.ltbpvp.client;

import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import pl.lordtricker.ltbpvp.client.command.CommandRegistration;
import pl.lordtricker.ltbpvp.client.keybinding.ToggleSettings;
import pl.lordtricker.ltbpvp.client.util.ColorUtils;
import pl.lordtricker.ltbpvp.client.util.Messages;

public class LtbpvpClient implements ClientModInitializer {

	private boolean welcomeSent = false;

	@Override
	public void onInitializeClient() {
		System.out.println("[LtbpvpClient] Client mod initialized!");
		ToggleSettings.initKeybinds();
		CommandRegistration.registerCommands();

		ClientTickEvents.END_CLIENT_TICK.register(client -> {
			if (!welcomeSent && client.player != null) {
				welcomeSent = true;
				String welcomeMsg = Messages.get("mod.info");
				client.player.sendMessage(ColorUtils.translateColorCodes(welcomeMsg), false);
			}
		});
	}
}
