package pl.lordtricker.ltbpvp.client.hud;

public class AttackDelayTutorHUD {
    public static String message = "";
    public static long expirationTime = 0;

    public static void setMessage(String msg, long durationMs) {
        message = msg;
        expirationTime = System.currentTimeMillis() + durationMs;
    }
}
