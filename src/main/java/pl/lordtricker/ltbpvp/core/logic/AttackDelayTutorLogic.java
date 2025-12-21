package pl.lordtricker.ltbpvp.core.logic;

public class AttackDelayTutorLogic {
    private static final String MESSAGE = "Uderzyłeś za szybko!";
    private static final long MESSAGE_DURATION_MS = 500L;

    private long lastSwingTime = 0L;
    private boolean hasAttackedOnce = false;

    public Result onSwing(long nowMs, boolean hitEntity, float attackSpeed, boolean textEnabled, boolean soundEnabled) {
        if (!hasAttackedOnce) {
            hasAttackedOnce = true;
            lastSwingTime = nowMs;
            return null;
        }

        long delta = nowMs - lastSwingTime;
        lastSwingTime = nowMs;

        if (attackSpeed <= 0.0f) {
            return null;
        }

        float cooldownTicks = 20.0F / attackSpeed;
        long cooldownMs = (long) (cooldownTicks * 50);

        if (!hitEntity || delta >= cooldownMs) {
            return null;
        }

        return new Result(textEnabled, soundEnabled, MESSAGE, MESSAGE_DURATION_MS);
    }

    public record Result(boolean showText, boolean playSound, String message, long durationMs) {}
}
