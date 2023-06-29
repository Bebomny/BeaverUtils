package dev.bebomny.beaver.beaverutils.features.features;

import dev.bebomny.beaver.beaverutils.configuration.config.QuickTeleportConfig;
import dev.bebomny.beaver.beaverutils.features.TriggerFeature;
import org.lwjgl.glfw.GLFW;

public class QuickTeleport extends TriggerFeature {

    private QuickTeleportConfig quickTeleportConfig = config.quickTeleportConfig;
    private int distance;

    public QuickTeleport() {
        super("QuickTeleport");

        this.distance = quickTeleportConfig.distance;

        addFireKeybinding(GLFW.GLFW_KEY_UNKNOWN);
    }

    @Override
    public void fire(FiredBy firedBy) {
        //TODO: teleport in looking direction! (with minus distance tp backwards)
    }

    public void setDistance(int distance) {
        this.distance = distance;
        quickTeleportConfig.distance = distance;
    }

    public int getDistance() {
        return distance;
    }


}
