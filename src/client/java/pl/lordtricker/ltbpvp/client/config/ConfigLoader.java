package pl.lordtricker.ltbpvp.client.config;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import net.fabricmc.loader.api.FabricLoader;

import java.io.IOException;
import java.io.Reader;
import java.io.Writer;
import java.nio.file.Files;
import java.nio.file.Path;

/**
 * Klasa odpowiedzialna za wczytywanie i zapisywanie pliku JSON configu:
 * ltbetterpvp-config.json
 * w folderze: config/LT-Mods/LT-BetterPVP/
 */
public class ConfigLoader {

    private static final Gson GSON = new GsonBuilder().setPrettyPrinting().create();
    private static final String MAIN_CONFIG_FILE_NAME = "ltbetterpvp-config.json";
    private static final Path MOD_CONFIG_DIR;

    static {
        Path configDir = FabricLoader.getInstance().getConfigDir();
        MOD_CONFIG_DIR = configDir.resolve("LT-Mods").resolve("LT-BetterPVP");
        try {
            if (!Files.exists(MOD_CONFIG_DIR)) {
                Files.createDirectories(MOD_CONFIG_DIR);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Wczytuje config z pliku JSON lub tworzy nowy, jeśli plik nie istnieje.
     */
    public static Config loadConfig() {
        Path configFile = MOD_CONFIG_DIR.resolve(MAIN_CONFIG_FILE_NAME);

        if (!Files.exists(configFile)) {
            Config defaultCfg = createDefaultConfig();
            saveConfig(defaultCfg);
            return defaultCfg;
        }

        try (Reader reader = Files.newBufferedReader(configFile)) {
            Config cfg = GSON.fromJson(reader, Config.class);
            return (cfg != null) ? cfg : createDefaultConfig();
        } catch (IOException e) {
            e.printStackTrace();
            return createDefaultConfig();
        }
    }

    /**
     * Zapisuje config do pliku JSON w docelowym folderze.
     */
    public static void saveConfig(Config cfg) {
        Path configFile = MOD_CONFIG_DIR.resolve(MAIN_CONFIG_FILE_NAME);

        try (Writer writer = Files.newBufferedWriter(configFile)) {
            GSON.toJson(cfg, writer);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Tworzy domyślną instancję Config. (Możesz ewentualnie dodać tutaj
     * bardziej rozbudowaną inicjalizację, jeśli chcesz.)
     */
    private static Config createDefaultConfig() {
        return new Config();
    }
}