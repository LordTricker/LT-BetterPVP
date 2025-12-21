package pl.lordtricker.ltbpvp.core.logic;

public final class FishingBobberLogic {
    private FishingBobberLogic() {}

    public static float clampOffsetY(float value) {
        if (value < -1.0f) return -1.0f;
        if (value > 1.0f) return 1.0f;
        return value;
    }

    public static float clampScale(float value) {
        if (value < 0.2f) return 0.2f;
        if (value > 2.0f) return 2.0f;
        return value;
    }
}
