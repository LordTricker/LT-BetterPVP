package pl.lordtricker.ltbpvp.client.util;

import net.minecraft.client.MinecraftClient;
import net.minecraft.text.Text;
import pl.lordtricker.ltbpvp.core.util.MessagesStore;

import java.util.Map;

public class Messages {
    public static String get(String key) {
        if (!MessagesStore.hasKey(key)) {
            String missing = MessagesStore.get(key);
            if (MinecraftClient.getInstance() != null && MinecraftClient.getInstance().player != null) {
                MinecraftClient.getInstance().player.sendMessage(Text.literal(missing), false);
            } else {
                System.err.println(missing);
            }
            return missing;
        }
        return MessagesStore.get(key);
    }

    public static String format(String key, Map<String, String> placeholders) {
        String message = get(key);
        for (Map.Entry<String, String> entry : placeholders.entrySet()) {
            message = message.replace("%" + entry.getKey() + "%", entry.getValue());
        }
        return message;
    }
}
