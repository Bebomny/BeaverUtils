package dev.bebomny.beaver.beaverutils.features;

import dev.bebomny.beaver.beaverutils.client.BeaverUtilsClient;

import java.util.ArrayList;
import java.util.List;

public class FeatureHandler {

    private final BeaverUtilsClient beaverUtilsClient;

    public final List<Feature> featureList = new ArrayList<>();

    public FeatureHandler() {
        this.beaverUtilsClient = BeaverUtilsClient.getInstance();
    }

    public void addFeature(Feature feature) {
        if (featureList.contains(feature)) {
            beaverUtilsClient.LOGGER.warn("Feature Already Present: " + feature.getName() + ", Skipping!");
            return;
        }

        featureList.add(feature);
    }
}
