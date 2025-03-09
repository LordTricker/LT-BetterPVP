package pl.lordtricker.ltbpvp.client.config;

import pl.lordtricker.ltbpvp.client.enums.CrosshairColor;
import pl.lordtricker.ltbpvp.client.enums.SwingStyle;
import pl.lordtricker.ltbpvp.client.enums.TargetStyle;

import java.util.EnumMap;
import java.util.Map;

/**
 * Główna klasa w pamięci (używana przez mixiny i GUI).
 * Zapisywana/odczytywana do pliku przez Config i ConfigLoader.
 */
public class ModSettings {
    public static SwingStyle swingStyle = SwingStyle.BASIC_SWING;
    public static boolean animationsEnabled = true;
    public static boolean targetingEnabled = false;
    public static boolean bobbingEnabled = false;
    public static boolean autojumpEnabled = false;
    public static boolean screenShakeEnabled = false;
    public static CrosshairColor crosshairColor = CrosshairColor.RGB;

    public static TargetStyle targetStyle = TargetStyle.SHURIKEN;
    public static int targetRange = 24;

    public static final Map<SwingStyle, AnimationOffsets> styleOffsets = new EnumMap<>(SwingStyle.class);

    static {
        for (SwingStyle style : SwingStyle.values()) {
            styleOffsets.put(style, new AnimationOffsets(0.0f, 0.0f, 0.0f));
        }
    }

    public static class AnimationOffsets {
        public float offsetX;
        public float offsetY;
        public float offsetZ;

        public AnimationOffsets(float x, float y, float z) {
            this.offsetX = x;
            this.offsetY = y;
            this.offsetZ = z;
        }
    }

    public static void save() {
        Config cfg = toConfig();
        ConfigLoader.saveConfig(cfg);
        System.out.println("[ModSettings] Saved -> betterpvp-config.json");
    }

    public static void load() {
        Config cfg = ConfigLoader.loadConfig();
        applyFrom(cfg);
        System.out.println("[ModSettings] Loaded <- betterpvp-config.json");
    }

    public static Config toConfig() {
        Config cfg = new Config();
        cfg.animationsEnabled = animationsEnabled;
        cfg.targetingEnabled = targetingEnabled;
        cfg.bobbingEnabled = bobbingEnabled;
        cfg.autojumpEnabled = autojumpEnabled;
        cfg.screenShakeEnabled = screenShakeEnabled;
        cfg.swingStyle = swingStyle;
        cfg.targetStyle = targetStyle;
        cfg.targetRange = targetRange;
        cfg.crosshairColor = crosshairColor;

        for (SwingStyle style : styleOffsets.keySet()) {
            AnimationOffsets moff = styleOffsets.get(style);
            Config.AnimationOffsets coff = cfg.styleOffsets.get(style);
            coff.offsetX = moff.offsetX;
            coff.offsetY = moff.offsetY;
            coff.offsetZ = moff.offsetZ;
        }
        return cfg;
    }

    public static void applyFrom(Config cfg) {
        animationsEnabled = cfg.animationsEnabled;
        targetingEnabled = cfg.targetingEnabled;
        bobbingEnabled = cfg.bobbingEnabled;
        autojumpEnabled = cfg.autojumpEnabled;
        screenShakeEnabled = cfg.screenShakeEnabled;
        swingStyle = cfg.swingStyle;
        targetStyle = cfg.targetStyle;
        targetRange = cfg.targetRange;
        crosshairColor = cfg.crosshairColor;

        for (SwingStyle style : cfg.styleOffsets.keySet()) {
            Config.AnimationOffsets coff = cfg.styleOffsets.get(style);
            if (!styleOffsets.containsKey(style)) {
                styleOffsets.put(style, new AnimationOffsets(0, 0, 0));
            }
            AnimationOffsets moff = styleOffsets.get(style);
            moff.offsetX = coff.offsetX;
            moff.offsetY = coff.offsetY;
            moff.offsetZ = coff.offsetZ;
        }
    }
}
