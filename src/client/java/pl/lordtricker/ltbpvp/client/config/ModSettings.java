package pl.lordtricker.ltbpvp.client.config;

import pl.lordtricker.ltbpvp.client.enums.CrosshairColor;
import pl.lordtricker.ltbpvp.client.enums.SwingStyle;
import pl.lordtricker.ltbpvp.client.enums.TargetStyle;

import java.util.EnumMap;
import java.util.Map;

public class ModSettings {
    public static SwingStyle swingStyle = SwingStyle.BASIC_SWING;
    public static boolean animationsEnabled = true;
    public static boolean targetingEnabled = false;
    public static boolean bobbingEnabled = false;
    public static boolean autojumpEnabled = false;
    public static boolean screenShakeEnabled = false;
    public static boolean gammaEnabled = false;
    public static CrosshairColor crosshairColor = CrosshairColor.RGB;

    public static TargetStyle targetStyle = TargetStyle.CIRCLE_GAP;
    public static int targetRange = 24;

    public static final Map<SwingStyle, AnimationOffsets> styleOffsets = new EnumMap<>(SwingStyle.class);

    public static boolean attackDelayTutorEnabled = false;
    public static boolean attackDelayTutorSoundEnabled = true;
    public static boolean attackDelayTutorTextEnabled = true;

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
        System.out.println("[ModSettings] Saved -> ltbetterpvp-config.json");
    }

    public static void load() {
        Config cfg = ConfigLoader.loadConfig();
        applyFrom(cfg);
        System.out.println("[ModSettings] Loaded <- ltbetterpvp-config.json");
    }

    public static Config toConfig() {
        Config cfg = new Config();
        cfg.animationsEnabled = animationsEnabled;
        cfg.targetingEnabled = targetingEnabled;
        cfg.bobbingEnabled = bobbingEnabled;
        cfg.autojumpEnabled = autojumpEnabled;
        cfg.screenShakeEnabled = screenShakeEnabled;
        cfg.gammaEnabled = gammaEnabled;
        cfg.swingStyle = swingStyle;
        cfg.targetStyle = targetStyle;
        cfg.targetRange = targetRange;
        cfg.crosshairColor = crosshairColor;
        cfg.attackDelayTutorEnabled = attackDelayTutorEnabled;
        cfg.attackDelayTutorSoundEnabled = attackDelayTutorSoundEnabled;
        cfg.attackDelayTutorTextEnabled = attackDelayTutorTextEnabled;

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
        gammaEnabled = cfg.gammaEnabled;
        swingStyle = cfg.swingStyle;
        targetStyle = cfg.targetStyle;
        targetRange = cfg.targetRange;
        crosshairColor = cfg.crosshairColor;
        attackDelayTutorEnabled = cfg.attackDelayTutorEnabled;
        attackDelayTutorSoundEnabled = cfg.attackDelayTutorSoundEnabled;
        attackDelayTutorTextEnabled = cfg.attackDelayTutorTextEnabled;

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
