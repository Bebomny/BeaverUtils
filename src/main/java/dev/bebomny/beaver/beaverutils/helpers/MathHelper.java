package dev.bebomny.beaver.beaverutils.helpers;

public class MathHelper {

    public static double getDistanceSquared(double x, double z) {
        double dist = 0;

        dist += Math.pow(x, 2);
        dist += Math.pow(z, 2);

        return Math.sqrt(dist);
    }
}
