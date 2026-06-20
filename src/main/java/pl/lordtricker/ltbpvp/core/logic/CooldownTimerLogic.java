package pl.lordtricker.ltbpvp.core.logic;

public final class CooldownTimerLogic {
    public static final int DEFAULT_COLOR = 0xFFFF0000;
    public static final int DEFAULT_SHIELD_COLOR = 0xFF00AAFF;

    private CooldownTimerLogic() {}

    public static int secondsRemaining(int remainingTicks) {
        if (remainingTicks <= 0) {
            return 0;
        }
        return (int) Math.ceil(remainingTicks / 20.0);
    }

    public static int parseHexColor(String value, int fallback) {
        if (value == null) {
            return fallback;
        }
        String cleaned = value.trim();
        if (cleaned.startsWith("#")) {
            cleaned = cleaned.substring(1);
        }
        if (cleaned.length() != 6) {
            return fallback;
        }
        try {
            return 0xFF000000 | Integer.parseInt(cleaned, 16);
        } catch (NumberFormatException ignored) {
            return fallback;
        }
    }

    public static String formatHexColor(int argb) {
        return String.format("#%06X", argb & 0xFFFFFF);
    }
}
