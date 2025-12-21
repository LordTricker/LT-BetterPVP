package pl.lordtricker.ltbpvp.core.logic;

public class ArmorStatusLogic {
    private static final long COOLDOWN_MS = 5_000L;

    private final long[] lastAlert = {0L, 0L, 0L, 0L};

    public Result checkArmor(int slotIndex, int durabilityPercent, String itemName, long nowMs,
                             int threshold, boolean textEnabled, boolean soundEnabled) {
        if (slotIndex < 0 || slotIndex >= lastAlert.length) {
            return null;
        }
        if (durabilityPercent > threshold) {
            return null;
        }
        if (nowMs - lastAlert[slotIndex] < COOLDOWN_MS) {
            return null;
        }

        lastAlert[slotIndex] = nowMs;
        String message = itemName + " spadł poniżej " + durabilityPercent + "%!";
        return new Result(textEnabled, soundEnabled, message, 1500L);
    }

    public record Result(boolean showText, boolean playSound, String message, long durationMs) {}
}
