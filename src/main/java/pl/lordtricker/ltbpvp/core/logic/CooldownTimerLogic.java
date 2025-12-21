package pl.lordtricker.ltbpvp.core.logic;

public final class CooldownTimerLogic {
    private CooldownTimerLogic() {}

    public static int secondsRemaining(int remainingTicks) {
        if (remainingTicks <= 0) {
            return 0;
        }
        return (int) Math.ceil(remainingTicks / 20.0);
    }
}
