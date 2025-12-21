package pl.lordtricker.ltbpvp.core.logic;

public final class TargetCrosshairLogic {
    private TargetCrosshairLogic() {}

    public static float computeAngleDegrees(long nowMs) {
        return (nowMs % 36000L) / 100.0F;
    }

    public static float[] computeColor(long nowMs, boolean rgbEnabled, float customRed, float customGreen, float customBlue) {
        if (rgbEnabled) {
            float time = ((nowMs % 2000L) / 2000.0F) * (float) Math.PI * 2.0F;
            float red = 0.5F + 0.5F * (float) Math.sin(time);
            float green = 0.5F + 0.5F * (float) Math.sin(time + (2 * Math.PI / 3));
            float blue = 0.5F + 0.5F * (float) Math.sin(time + (4 * Math.PI / 3));
            return new float[] { red, green, blue };
        }
        return new float[] { customRed, customGreen, customBlue };
    }
}
