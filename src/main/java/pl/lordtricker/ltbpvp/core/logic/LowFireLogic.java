package pl.lordtricker.ltbpvp.core.logic;

public final class LowFireLogic {
    private LowFireLogic() {}

    public static boolean shouldApply(boolean enabled) {
        return enabled;
    }

    public static float clampHeight(float height) {
        if (height < -1.0f) return -1.0f;
        if (height > 1.0f) return 1.0f;
        return height;
    }
}

