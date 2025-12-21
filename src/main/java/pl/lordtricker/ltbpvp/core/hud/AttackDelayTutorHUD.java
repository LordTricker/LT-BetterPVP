package pl.lordtricker.ltbpvp.core.hud;

public class AttackDelayTutorHUD {
    public static String message = "";
    public static long expirationTime = 0;
    public static final float SCALE = 0.7F;
    public static final int X_OFFSET = 25;
    public static final int Y_OFFSET = 25;

    public static void setMessage(String msg, long durationMs) {
        message = msg;
        expirationTime = System.currentTimeMillis() + durationMs;
    }

    public static boolean shouldRender(long nowMs) {
        return expirationTime > nowMs && message != null && !message.isEmpty();
    }
}
