package pl.lordtricker.ltbpvp.client.command;

import com.mojang.brigadier.CommandDispatcher;
import net.fabricmc.fabric.api.client.command.v2.ClientCommandManager;
import net.fabricmc.fabric.api.client.command.v2.ClientCommandRegistrationCallback;
import net.fabricmc.fabric.api.client.command.v2.FabricClientCommandSource;
import net.minecraft.client.MinecraftClient;
import net.minecraft.command.CommandRegistryAccess;
import pl.lordtricker.ltbpvp.client.gui.MainSettingsScreen;
import pl.lordtricker.ltbpvp.client.util.Messages;
import pl.lordtricker.ltbpvp.core.config.CoreSettings;

public class ClientCommandRegistration {
    public static void registerCommands() {
        ClientCommandRegistrationCallback.EVENT.register(ClientCommandRegistration::registerLtbCommand);
    }

    private static void registerLtbCommand(
            CommandDispatcher<FabricClientCommandSource> dispatcher,
            CommandRegistryAccess registryAccess
    ) {
        dispatcher.register(
                ClientCommandManager.literal("ltb")
                        // /ltb – wyświetlenie podstawowych informacji
                        .executes(ctx -> {
                            String msgKey = "command.info";
                            String message = Messages.get(msgKey);
                            ctx.getSource().sendFeedback(CommandUi.colored(message));
                            return 1;
                        })
                        // /ltb pomoc – lista dostępnych komend
                        .then(ClientCommandManager.literal("pomoc")
                                .executes(ctx -> {
                                    String msgKey = "command.help";
                                    String helpMessage = Messages.get(msgKey);
                                    ctx.getSource().sendFeedback(CommandUi.colored(helpMessage));
                                    return 1;
                                })
                        )
                        // /ltb settings – otwarcie GUI ustawień
                        .then(ClientCommandManager.literal("settings")
                                .executes(ctx -> {
                                    MinecraftClient client = MinecraftClient.getInstance();
                                    client.setScreen(null);
                                    new Thread(() -> {
                                        try {
                                            Thread.sleep(100);
                                        } catch (InterruptedException e) {
                                            e.printStackTrace();
                                        }
                                        client.execute(() -> client.setScreen(new MainSettingsScreen()));
                                    }).start();
                                    return 1;
                                })
                        )
                        // /ltb config save/reload
                        .then(ClientCommandManager.literal("config")
                                .then(ClientCommandManager.literal("save")
                                        .executes(ctx -> {
                                            CoreSettings.save();
                                            String msgKey = "command.config.save.success";
                                            String msg = Messages.get(msgKey);
                                            ctx.getSource().sendFeedback(CommandUi.colored(msg));
                                            return 1;
                                        })
                                )
                                .then(ClientCommandManager.literal("reload")
                                        .executes(ctx -> {
                                            CoreSettings.load();
                                            String msgKey = "command.config.reload.success";
                                            String msg = Messages.get(msgKey);
                                            ctx.getSource().sendFeedback(CommandUi.colored(msg));
                                            return 1;
                                        })
                                )
                        )
        );
    }
}

