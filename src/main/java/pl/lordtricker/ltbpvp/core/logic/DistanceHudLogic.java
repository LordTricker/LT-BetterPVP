package pl.lordtricker.ltbpvp.core.logic;

import net.minecraft.util.math.Vec3d;

import java.util.Locale;

public final class DistanceHudLogic {
    private DistanceHudLogic() {}

    public static double distanceMeters(Vec3d from, Vec3d to) {
        if (from == null || to == null) {
            return -1.0;
        }
        return from.distanceTo(to);
    }

    public static String formatMeters(double distance) {
        if (distance < 0.0) {
            return "";
        }
        return String.format(Locale.ROOT, "%.1f m", distance);
    }
}
