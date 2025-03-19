package pl.lordtricker.ltbpvp.client;

import com.mojang.brigadier.CommandDispatcher;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.command.v1.ClientCommandManager;
import net.fabricmc.fabric.api.client.command.v1.FabricClientCommandSource;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayConnectionEvents;
import pl.lordtricker.ltbpvp.client.command.CommandRegistration;
import pl.lordtricker.ltbpvp.client.config.ModSettings;
import pl.lordtricker.ltbpvp.client.util.ColorUtils;
import pl.lordtricker.ltbpvp.client.util.Messages;

public class LtbpvpClient implements ClientModInitializer {

	@Override
	public void onInitializeClient() {
		ModSettings.load();

		ClientPlayConnectionEvents.JOIN.register((handler, sender, client) -> {
			if (client.player != null) {
				String welcomeMsg = Messages.get("player.join");
				client.player.sendMessage(ColorUtils.translateColorCodes(welcomeMsg), false);
			}
		});

		CommandDispatcher<FabricClientCommandSource> dispatcher = ClientCommandManager.DISPATCHER;
		CommandRegistration.registerCommands(dispatcher);
	}
}
