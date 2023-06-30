package dev.bebomny.beaver.beaverutils.configuration.config;

import com.google.gson.annotations.Expose;
import dev.bebomny.beaver.beaverutils.features.features.AutoPlant;

public class AutoPlantConfig extends EnableConfigOption{

    @Expose
    public AutoPlant.Mode mode = AutoPlant.Mode.OnlyReplant;

    @Expose
    public int plantRadius = 2;

    public AutoPlantConfig() {

    }
}
