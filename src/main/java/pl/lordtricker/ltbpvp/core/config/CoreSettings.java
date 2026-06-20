package pl.lordtricker.ltbpvp.core.config;

import pl.lordtricker.ltbpvp.core.enums.CrosshairColor;
import pl.lordtricker.ltbpvp.core.enums.DistanceDisplayMode;
import pl.lordtricker.ltbpvp.core.enums.SwingStyle;
import pl.lordtricker.ltbpvp.core.enums.TargetStyle;
import pl.lordtricker.ltbpvp.core.logic.CooldownTimerLogic;

import java.util.EnumMap;
import java.util.Map;

public class CoreSettings {
    private static CoreConfigIO configIO;
    public static SwingStyle swingStyle = SwingStyle.BASIC_SWING;
    public static boolean animationsEnabled = true;
    public static boolean adsEnabled = true;
    public static boolean targetingEnabled = false;
    public static boolean bobbingEnabled = false;
    public static boolean autojumpEnabled = false;
    public static boolean screenShakeEnabled = false;
    public static boolean gammaEnabled = false;
    public static CrosshairColor crosshairColor = CrosshairColor.RGB;
    public static boolean rgbEnabled = false;
    public static float customRed   = 1.0f;
    public static float customGreen = 1.0f;
    public static float customBlue  = 1.0f;

    public static TargetStyle targetStyle = TargetStyle.CIRCLE_GAP;
    public static int targetRange = 24;

    public static final Map<SwingStyle, AnimationOffsets> styleOffsets = new EnumMap<>(SwingStyle.class);

    public static boolean offhandAnimationEnabled = false;
    public static AnimationOffsets offhandOffsets = new AnimationOffsets(0.0f, 0.0f, 0.0f);

    public static boolean attackDelayTutorEnabled = false;
    public static boolean attackDelayTutorSoundEnabled = true;
    public static boolean attackDelayTutorTextEnabled = true;

    public static boolean armorStatusEnabled = false;
    public static boolean armorStatusSoundEnabled = true;
    public static boolean armorStatusTextEnabled = true;
    public static int armorStatusThreshold = 25;

    public static boolean lowFireEnabled = true;
    public static float lowFireHeight = -0.08f;

    public static boolean fishingBobberEnabled = false;
    public static float fishingBobberOffsetY = 0.0f;
    public static float fishingBobberScale = 1.0f;

    public static boolean cooldownTimerEnabled = false;
    public static int cooldownTimerColor = CooldownTimerLogic.DEFAULT_COLOR;
    public static int shieldCooldownTimerColor = CooldownTimerLogic.DEFAULT_SHIELD_COLOR;
    public static boolean distanceHudEnabled = false;
    public static DistanceDisplayMode distanceDisplayMode = DistanceDisplayMode.ENTITY_ONLY;

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

    public static void init(CoreConfigIO io) {
        configIO = io;
    }

    public static void save() {
        CoreConfig cfg = toConfig();
        requireConfigIO().save(cfg);
        System.out.println("[CoreSettings] Saved -> ltbetterpvp-config.json");
    }

    public static void load() {
        CoreConfig cfg = requireConfigIO().load();
        applyFrom(cfg);
        System.out.println("[CoreSettings] Loaded <- ltbetterpvp-config.json");
    }

    public static CoreConfig toConfig() {
        CoreConfig cfg = new CoreConfig();
        cfg.animationsEnabled = animationsEnabled;
        cfg.adsEnabled = adsEnabled;
        cfg.targetingEnabled = targetingEnabled;
        cfg.bobbingEnabled = bobbingEnabled;
        cfg.autojumpEnabled = autojumpEnabled;
        cfg.screenShakeEnabled = screenShakeEnabled;
        cfg.gammaEnabled = gammaEnabled;
        cfg.swingStyle = swingStyle;
        cfg.targetStyle = targetStyle;
        cfg.targetRange = targetRange;
        cfg.crosshairColor = crosshairColor;
        cfg.rgbEnabled = rgbEnabled;
        cfg.customRed = customRed;
        cfg.customGreen = customGreen;
        cfg.customBlue = customBlue;
        cfg.attackDelayTutorEnabled = attackDelayTutorEnabled;
        cfg.attackDelayTutorSoundEnabled = attackDelayTutorSoundEnabled;
        cfg.attackDelayTutorTextEnabled = attackDelayTutorTextEnabled;
        cfg.offhandAnimationEnabled = offhandAnimationEnabled;
        cfg.offhandOffsets.offsetX = offhandOffsets.offsetX;
        cfg.offhandOffsets.offsetY = offhandOffsets.offsetY;
        cfg.offhandOffsets.offsetZ = offhandOffsets.offsetZ;
        cfg.armorStatusEnabled = armorStatusEnabled;
        cfg.armorStatusSoundEnabled = armorStatusSoundEnabled;
        cfg.armorStatusTextEnabled = armorStatusTextEnabled;
        cfg.armorStatusThreshold = armorStatusThreshold;
        cfg.lowFireEnabled = lowFireEnabled;
        cfg.lowFireHeight = lowFireHeight;
        cfg.fishingBobberEnabled = fishingBobberEnabled;
        cfg.fishingBobberOffsetY = fishingBobberOffsetY;
        cfg.fishingBobberScale = fishingBobberScale;
        cfg.cooldownTimerEnabled = cooldownTimerEnabled;
        cfg.cooldownTimerColor = cooldownTimerColor;
        cfg.shieldCooldownTimerColor = shieldCooldownTimerColor;
        cfg.distanceHudEnabled = distanceHudEnabled;
        cfg.distanceDisplayMode = distanceDisplayMode;
        return cfg;
    }

    public static void applyFrom(CoreConfig cfg) {
        animationsEnabled = cfg.animationsEnabled;
        adsEnabled = cfg.adsEnabled;
        targetingEnabled = cfg.targetingEnabled;
        bobbingEnabled = cfg.bobbingEnabled;
        autojumpEnabled = cfg.autojumpEnabled;
        screenShakeEnabled = cfg.screenShakeEnabled;
        gammaEnabled = cfg.gammaEnabled;
        swingStyle = cfg.swingStyle;
        targetStyle = cfg.targetStyle;
        targetRange = cfg.targetRange;
        crosshairColor = cfg.crosshairColor;
        rgbEnabled = cfg.rgbEnabled;
        customRed = cfg.customRed;
        customGreen = cfg.customGreen;
        customBlue = cfg.customBlue;
        attackDelayTutorEnabled = cfg.attackDelayTutorEnabled;
        attackDelayTutorSoundEnabled = cfg.attackDelayTutorSoundEnabled;
        attackDelayTutorTextEnabled = cfg.attackDelayTutorTextEnabled;
        offhandAnimationEnabled = cfg.offhandAnimationEnabled;
        offhandOffsets.offsetX = cfg.offhandOffsets.offsetX;
        offhandOffsets.offsetY = cfg.offhandOffsets.offsetY;
        offhandOffsets.offsetZ = cfg.offhandOffsets.offsetZ;
        armorStatusEnabled = cfg.armorStatusEnabled;
        armorStatusSoundEnabled = cfg.armorStatusSoundEnabled;
        armorStatusTextEnabled = cfg.armorStatusTextEnabled;
        armorStatusThreshold = cfg.armorStatusThreshold;
        lowFireEnabled = cfg.lowFireEnabled;
        lowFireHeight = cfg.lowFireHeight;
        fishingBobberEnabled = cfg.fishingBobberEnabled;
        fishingBobberOffsetY = cfg.fishingBobberOffsetY;
        fishingBobberScale = cfg.fishingBobberScale;
        cooldownTimerEnabled = cfg.cooldownTimerEnabled;
        cooldownTimerColor = cfg.cooldownTimerColor;
        shieldCooldownTimerColor = cfg.shieldCooldownTimerColor;
        distanceHudEnabled = cfg.distanceHudEnabled;
        distanceDisplayMode = cfg.distanceDisplayMode;
        for (SwingStyle style : cfg.styleOffsets.keySet()) {
            CoreConfig.AnimationOffsets coff = cfg.styleOffsets.get(style);
            AnimationOffsets moff = styleOffsets.get(style);
            moff.offsetX = coff.offsetX;
            moff.offsetY = coff.offsetY;
            moff.offsetZ = coff.offsetZ;
        }
    }

    private static CoreConfigIO requireConfigIO() {
        if (configIO == null) {
            throw new IllegalStateException("CoreSettings CoreConfigIO not initialized.");
        }
        return configIO;
    }
}

