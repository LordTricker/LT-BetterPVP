package pl.lordtricker.ltbpvp.client;

import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayConnectionEvents;
import pl.lordtricker.ltbpvp.client.command.ClientCommandRegistration;
import pl.lordtricker.ltbpvp.client.config.ConfigLoader;
import pl.lordtricker.ltbpvp.client.util.ColorUtils;
import pl.lordtricker.ltbpvp.client.util.Messages;
import pl.lordtricker.ltbpvp.core.config.CoreSettings;
import pl.lordtricker.ltbpvp.core.util.RemoteAdConfig;

public class LtbpvpClient implements ClientModInitializer {
    @Override
    public void onInitializeClient() {
        CoreSettings.init(new ConfigLoader());
        CoreSettings.load();

        // Preload remote ad config (server name/address) asynchronously
        RemoteAdConfig.preloadAsync();

        ClientPlayConnectionEvents.JOIN.register((handler, sender, client) -> {
            if (client.player != null) {
                String welcomeMsg = Messages.get("player.join");
                client.player.sendMessage(ColorUtils.translateColorCodes(welcomeMsg), false);
            }
        });

        ClientCommandRegistration.registerCommands();
    }
}

