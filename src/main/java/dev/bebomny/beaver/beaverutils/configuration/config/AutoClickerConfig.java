package dev.bebomny.beaver.beaverutils.configuration.config;

import com.google.gson.annotations.Expose;
import dev.bebomny.beaver.beaverutils.features.features.AutoClicker;

public class AutoClickerConfig {

    @Expose
    public int delay = 30;

    @Expose
    public AutoClicker.Mode mode = AutoClicker.Mode.ATTACK;

    @Expose
    public AutoClicker.Type type = AutoClicker.Type.CLICK;

    public AutoClickerConfig() {

    }
}
