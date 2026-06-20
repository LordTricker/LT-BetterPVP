package pl.lordtricker.ltbpvp.core.config;

import pl.lordtricker.ltbpvp.core.enums.CrosshairColor;
import pl.lordtricker.ltbpvp.core.enums.DistanceDisplayMode;
import pl.lordtricker.ltbpvp.core.enums.SwingStyle;
import pl.lordtricker.ltbpvp.core.enums.TargetStyle;
import pl.lordtricker.ltbpvp.core.logic.CooldownTimerLogic;

import java.util.EnumMap;
import java.util.Map;

public class CoreConfig {
    public boolean animationsEnabled = true;
    public boolean adsEnabled = true;
    public boolean targetingEnabled = false;
    public boolean bobbingEnabled = false;
    public boolean autojumpEnabled = false;
    public boolean screenShakeEnabled = false;
    public boolean gammaEnabled = false;
    public CrosshairColor crosshairColor = CrosshairColor.RGB;
    public boolean rgbEnabled = false;
    public float customRed = 1.0f;
    public float customGreen = 1.0f;
    public float customBlue = 1.0f;

    public SwingStyle swingStyle = SwingStyle.BASIC_SWING;
    public TargetStyle targetStyle = TargetStyle.CIRCLE_GAP;
    public int targetRange = 24;

    public boolean attackDelayTutorEnabled = false;
    public boolean attackDelayTutorSoundEnabled = true;
    public boolean attackDelayTutorTextEnabled = true;

    public boolean armorStatusEnabled = false;
    public boolean armorStatusSoundEnabled = true;
    public boolean armorStatusTextEnabled = true;
    public int armorStatusThreshold = 25;

    public boolean lowFireEnabled = true;
    public float lowFireHeight = -0.08f;

    public boolean fishingBobberEnabled = false;
    public float fishingBobberOffsetY = 0.0f;
    public float fishingBobberScale = 1.0f;

    public boolean cooldownTimerEnabled = false;
    public int cooldownTimerColor = CooldownTimerLogic.DEFAULT_COLOR;
    public int shieldCooldownTimerColor = CooldownTimerLogic.DEFAULT_SHIELD_COLOR;
    public boolean distanceHudEnabled = false;
    public DistanceDisplayMode distanceDisplayMode = DistanceDisplayMode.ENTITY_ONLY;

    public Map<SwingStyle, AnimationOffsets> styleOffsets = new EnumMap<>(SwingStyle.class);

    public boolean offhandAnimationEnabled = false;
    public AnimationOffsets offhandOffsets = new AnimationOffsets(0.0f, 0.0f, 0.0f);

    public CoreConfig() {
        for (SwingStyle style : SwingStyle.values()) {
            AnimationOffsets off = new AnimationOffsets(0.0f, 0.0f, 0.0f);
            styleOffsets.put(style, off);
        }
    }

    public static class AnimationOffsets {
        public float offsetX;
        public float offsetY;
        public float offsetZ;

        public AnimationOffsets() {
            this.offsetX = 0.0f;
            this.offsetY = 0.0f;
            this.offsetZ = 0.0f;
        }

        public AnimationOffsets(float x, float y, float z) {
            this.offsetX = x;
            this.offsetY = y;
            this.offsetZ = z;
        }
    }
}
