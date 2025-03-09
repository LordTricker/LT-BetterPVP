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
 * betterpvp-config.json
 */
public class ConfigLoader {

    private static final Gson GSON = new GsonBuilder().setPrettyPrinting().create();
    private static final String CONFIG_FILE_NAME = "ltbetterpvp-config.json";

    public static Config loadConfig() {
        Path configDir = FabricLoader.getInstance().getConfigDir();
        Path configFile = configDir.resolve(CONFIG_FILE_NAME);

        if (!Files.exists(configFile)) {
            Config defaultCfg = new Config();
            saveConfig(defaultCfg);
            return defaultCfg;
        }

        try (Reader reader = Files.newBufferedReader(configFile)) {
            Config cfg = GSON.fromJson(reader, Config.class);
            return (cfg != null) ? cfg : new Config();
        } catch (IOException e) {
            e.printStackTrace();
            return new Config();
        }
    }

    public static void saveConfig(Config cfg) {
        Path configDir = FabricLoader.getInstance().getConfigDir();
        Path configFile = configDir.resolve(CONFIG_FILE_NAME);

        try (Writer writer = Files.newBufferedWriter(configFile)) {
            GSON.toJson(cfg, writer);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
