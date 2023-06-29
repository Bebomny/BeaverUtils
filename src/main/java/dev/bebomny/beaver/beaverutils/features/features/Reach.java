package dev.bebomny.beaver.beaverutils.features.features;

import dev.bebomny.beaver.beaverutils.configuration.config.ReachConfig;
import dev.bebomny.beaver.beaverutils.features.SimpleOnOffFeature;

public class Reach extends SimpleOnOffFeature {

    private final ReachConfig reachConfig = config.reachConfig;

    public Reach() {
        super("Reach");

        setEnableConfig(reachConfig);

        if(config.generalConfig.autoEnable)
            setEnabled(reachConfig.enabled);

        //distance is accessed directly from the config so no need to load it here
    }

    public void setDistance(float newDistance) {
        reachConfig.distance = newDistance;
    }

    public float getDistance() {
        return reachConfig.distance;
    }
}
