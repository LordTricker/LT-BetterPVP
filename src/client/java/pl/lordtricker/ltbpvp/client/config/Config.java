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

    public SwingStyle swingStyle = SwingStyle.BASIC_SWING;
    public TargetStyle targetStyle = TargetStyle.CIRCLE_GAP;
    public int targetRange = 24;

    public boolean attackDelayTutorEnabled = false;
    public boolean attackDelayTutorSoundEnabled = true;
    public boolean attackDelayTutorTextEnabled = true;

    public Map<SwingStyle, AnimationOffsets> styleOffsets = new EnumMap<>(SwingStyle.class);

    public Config() {
        for (SwingStyle style : SwingStyle.values()) {
            AnimationOffsets off = new AnimationOffsets();
            off.offsetX = 0.0f;
            off.offsetY = 0.0f;
            off.offsetZ = 0.0f;
            styleOffsets.put(style, off);
        }
    }

    public static class AnimationOffsets {
        public float offsetX;
        public float offsetY;
        public float offsetZ;
    }
}
