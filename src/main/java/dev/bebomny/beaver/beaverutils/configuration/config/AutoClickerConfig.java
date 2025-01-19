package dev.bebomny.beaver.beaverutils.configuration.config;

import com.google.gson.annotations.Expose;
import dev.bebomny.beaver.beaverutils.features.features.autoclicker.MouseClickAction;

public class AutoClickerConfig {

    @Expose
    public MouseClickAction leftClickAction;

    @Expose
    public MouseClickAction rightClickAction;

    public AutoClickerConfig() {

    }
}
