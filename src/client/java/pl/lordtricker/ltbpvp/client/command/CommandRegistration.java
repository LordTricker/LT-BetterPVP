package pl.lordtricker.ltbpvp.client.command;

import com.mojang.brigadier.CommandDispatcher;
import net.fabricmc.fabric.api.client.command.v2.ClientCommandManager;
import net.fabricmc.fabric.api.client.command.v2.ClientCommandRegistrationCallback;
import net.fabricmc.fabric.api.client.command.v2.FabricClientCommandSource;
import net.minecraft.client.MinecraftClient;
import net.minecraft.command.CommandRegistryAccess;
import pl.lordtricker.ltbpvp.client.config.ModSettings;
import pl.lordtricker.ltbpvp.client.gui.MainSettingsScreen;
import pl.lordtricker.ltbpvp.client.util.ColorUtils;
import pl.lordtricker.ltbpvp.client.util.Messages;

public class CommandRegistration {

    public static void registerCommands() {
        ClientCommandRegistrationCallback.EVENT.register(CommandRegistration::registerLtbCommand);
    }

    private static void registerLtbCommand(
            CommandDispatcher<FabricClientCommandSource> dispatcher,
            CommandRegistryAccess registryAccess
    ) {
        dispatcher.register(
                ClientCommandManager.literal("ltb")
                        // /ltb – wyświetlenie podstawowych informacji
                        .executes(ctx -> {
                            String msgKey = "command.main.info";
                            String message = Messages.get(msgKey);
                            ctx.getSource().sendFeedback(ColorUtils.translateColorCodes(message));
                            return 1;
                        })
                        // /ltb pomoc – lista dostępnych komend
                        .then(ClientCommandManager.literal("pomoc")
                                .executes(ctx -> {
                                    String msgKey = "command.main.help";
                                    String helpMessage = Messages.get(msgKey);
                                    ctx.getSource().sendFeedback(ColorUtils.translateColorCodes(helpMessage));
                                    return 1;
                                })
                        )
                        // /ltb settings – otwarcie GUI ustawień
                        .then(ClientCommandManager.literal("settings")
                                .executes(ctx -> {
                                    MinecraftClient client = MinecraftClient.getInstance();
                                    client.execute(() -> client.setScreen(new MainSettingsScreen()));
                                    return 1;
                                })
                        )
                        // /ltb config save/reload
                        .then(ClientCommandManager.literal("config")
                                .then(ClientCommandManager.literal("save")
                                        .executes(ctx -> {
                                            ModSettings.save();
                                            String msgKey = "command.config.save.success";
                                            String msg = Messages.get(msgKey);
                                            ctx.getSource().sendFeedback(ColorUtils.translateColorCodes(msg));
                                            return 1;
                                        })
                                )
                                .then(ClientCommandManager.literal("reload")
                                        .executes(ctx -> {
                                            ModSettings.load();
                                            String msgKey = "command.config.reload.success";
                                            String msg = Messages.get(msgKey);
                                            ctx.getSource().sendFeedback(ColorUtils.translateColorCodes(msg));
                                            return 1;
                                        })
                                )
                        )
        );
    }
}
