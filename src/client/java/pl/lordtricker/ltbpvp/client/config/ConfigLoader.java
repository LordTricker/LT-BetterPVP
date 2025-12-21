package pl.lordtricker.ltbpvp.client.config;

import net.fabricmc.loader.api.FabricLoader;
import pl.lordtricker.ltbpvp.core.config.CoreConfig;
import pl.lordtricker.ltbpvp.core.config.CoreConfigIO;
import pl.lordtricker.ltbpvp.core.config.CoreConfigLoader;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

/**
 * Klasa odpowiedzialna za wczytywanie i zapisywanie pliku JSON configu:
 * ltbetterpvp-config.json
 * w folderze: config/LT-Mods/LT-BetterPVP/
 */
public class ConfigLoader implements CoreConfigIO {
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

    public static CoreConfig loadConfig() {
        return CoreConfigLoader.loadConfig(MOD_CONFIG_DIR);
    }

    public static void saveConfig(CoreConfig cfg) {
        CoreConfigLoader.saveConfig(cfg, MOD_CONFIG_DIR);
    }

    @Override
    public CoreConfig load() {
        return loadConfig();
    }

    @Override
    public void save(CoreConfig config) {
        saveConfig(config);
    }
}

