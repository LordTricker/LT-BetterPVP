package pl.lordtricker.ltbpvp.core.config;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import java.io.IOException;
import java.io.Reader;
import java.io.Writer;
import java.nio.file.Files;
import java.nio.file.Path;

public final class CoreConfigLoader {
    private static final Gson GSON = new GsonBuilder().setPrettyPrinting().create();
    private static final String MAIN_CONFIG_FILE_NAME = "ltbetterpvp-config.json";

    private CoreConfigLoader() {}

    public static CoreConfig loadConfig(Path modConfigDir) {
        Path configDir = ensureConfigDir(modConfigDir);
        Path configFile = configDir.resolve(MAIN_CONFIG_FILE_NAME);

        if (!Files.exists(configFile)) {
            CoreConfig defaultCfg = createDefaultConfig();
            saveConfig(defaultCfg, configDir);
            return defaultCfg;
        }

        try (Reader reader = Files.newBufferedReader(configFile)) {
            JsonObject obj = JsonParser.parseReader(reader).getAsJsonObject();
            CoreConfig cfg = GSON.fromJson(obj, CoreConfig.class);
            if (cfg == null) {
                cfg = createDefaultConfig();
            }
            boolean updated = applyDefaultsForMissing(obj, cfg);
            if (updated) {
                saveConfig(cfg, configDir);
            }
            return cfg;
        } catch (Exception e) {
            e.printStackTrace();
            return createDefaultConfig();
        }
    }

    public static void saveConfig(CoreConfig cfg, Path modConfigDir) {
        Path configDir = ensureConfigDir(modConfigDir);
        Path configFile = configDir.resolve(MAIN_CONFIG_FILE_NAME);

        try (Writer writer = Files.newBufferedWriter(configFile)) {
            GSON.toJson(cfg, writer);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static Path ensureConfigDir(Path modConfigDir) {
        if (modConfigDir == null) {
            throw new IllegalArgumentException("modConfigDir is null");
        }
        try {
            if (!Files.exists(modConfigDir)) {
                Files.createDirectories(modConfigDir);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return modConfigDir;
    }

    private static CoreConfig createDefaultConfig() {
        return new CoreConfig();
    }

    private static boolean applyDefaultsForMissing(JsonObject obj, CoreConfig cfg) {
        boolean updated = false;
        if (obj == null) {
            return false;
        }
        if (!obj.has("lowFireEnabled")) {
            cfg.lowFireEnabled = true;
            updated = true;
        }
        if (!obj.has("lowFireHeight")) {
            cfg.lowFireHeight = -0.08f;
            updated = true;
        }
        if (!obj.has("autojumpEnabled")) {
            cfg.autojumpEnabled = false;
            updated = true;
        }
        if (!obj.has("bobbingEnabled")) {
            cfg.bobbingEnabled = false;
            updated = true;
        }
        if (!obj.has("screenShakeEnabled")) {
            cfg.screenShakeEnabled = false;
            updated = true;
        }
        if (!obj.has("fishingBobberEnabled")) {
            cfg.fishingBobberEnabled = false;
            updated = true;
        }
        if (!obj.has("fishingBobberOffsetY")) {
            cfg.fishingBobberOffsetY = 0.0f;
            updated = true;
        }
        if (!obj.has("fishingBobberScale")) {
            cfg.fishingBobberScale = 1.0f;
            updated = true;
        }
        if (!obj.has("cooldownTimerEnabled")) {
            cfg.cooldownTimerEnabled = false;
            updated = true;
        }
        return updated;
    }
}
