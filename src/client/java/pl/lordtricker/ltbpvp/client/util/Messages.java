package pl.lordtricker.ltbpvp.client.util;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import net.minecraft.client.MinecraftClient;
import net.minecraft.text.Text;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.lang.reflect.Type;
import java.nio.charset.StandardCharsets;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Messages {
    private static final Map<String, List<String>> messages = new HashMap<>();

    static {
        try (InputStream in = Messages.class.getResourceAsStream("/assets/ltbpvp/messages/messages.json")) {
            if (in == null) {
                System.err.println("messages.json not found in resources!");
            } else {
                Type type = new TypeToken<Map<String, List<String>>>() {}.getType();
                Map<String, List<String>> loaded = new Gson().fromJson(new InputStreamReader(in, StandardCharsets.UTF_8), type);
                if (loaded != null) {
                    for (Map.Entry<String, List<String>> entry : loaded.entrySet()) {
                        if (entry.getValue() == null) {
                            entry.setValue(Collections.singletonList("Brak wiadomości dla klucza: " + entry.getKey()));
                        }
                    }
                    messages.putAll(loaded);
                    System.out.println("Wczytano klucze z messages.json: " + messages.keySet());
                } else {
                    System.err.println("Wczytany obiekt JSON jest null!");
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Zwraca komunikat dla danego klucza. Jeśli klucz nie występuje lub jego wartość jest pusta,
     * wysyła informację do chatu (jeśli gracz jest dostępny) i zwraca domyślną wiadomość.
     */
    public static String get(String key) {
        if (!messages.containsKey(key)) {
            String missing = "Brak wiadomości dla klucza: " + key;
            if (MinecraftClient.getInstance() != null && MinecraftClient.getInstance().player != null) {
                MinecraftClient.getInstance().player.sendMessage(Text.of(missing), false);
            } else {
                System.err.println(missing);
            }
            return missing;
        }
        List<String> lines = messages.get(key);
        if (lines == null || lines.isEmpty()) {
            return "";
        }
        return String.join("\n", lines);
    }

    /**
     * Formatuje komunikat, zastępując placeholdery wartościami z mapy.
     */
    public static String format(String key, Map<String, String> placeholders) {
        String message = get(key);
        for (Map.Entry<String, String> entry : placeholders.entrySet()) {
            message = message.replace("%" + entry.getKey() + "%", entry.getValue());
        }
        return message;
    }
}
