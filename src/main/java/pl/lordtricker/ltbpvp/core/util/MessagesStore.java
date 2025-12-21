package pl.lordtricker.ltbpvp.core.util;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.lang.reflect.Type;
import java.nio.charset.StandardCharsets;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public final class MessagesStore {
    private static final Map<String, List<String>> messages = new HashMap<>();

    static {
        try (InputStream in = MessagesStore.class.getResourceAsStream("/assets/ltbpvp/messages/messages.json")) {
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

    private MessagesStore() {}

    public static boolean hasKey(String key) {
        return messages.containsKey(key);
    }

    public static String get(String key) {
        if (!messages.containsKey(key)) {
            return "Brak wiadomości dla klucza: " + key;
        }
        List<String> lines = messages.get(key);
        if (lines == null || lines.isEmpty()) {
            return "";
        }
        return String.join("\n", lines);
    }
}
