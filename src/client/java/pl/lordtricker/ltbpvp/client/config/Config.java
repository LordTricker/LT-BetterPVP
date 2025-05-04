package pl.lordtricker.ltbpvp.client.config;

import pl.lordtricker.ltbpvp.client.enums.CrosshairColor;
import pl.lordtricker.ltbpvp.client.enums.SwingStyle;
import pl.lordtricker.ltbpvp.client.enums.TargetStyle;

import java.util.EnumMap;
import java.util.Map;

public class Config {
    public boolean animationsEnabled = true;
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

    public Map<SwingStyle, AnimationOffsets> styleOffsets = new EnumMap<>(SwingStyle.class);

    public boolean offhandAnimationEnabled = false;
    public AnimationOffsets offhandOffsets = new AnimationOffsets(0.0f, 0.0f, 0.0f);

    public Config() {
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