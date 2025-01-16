package dev.bebomny.beaver.beaverutils.configuration.config;

import com.google.gson.annotations.Expose;
import dev.bebomny.beaver.beaverutils.features.features.AutoClicker;
import dev.bebomny.beaver.beaverutils.features.features.autoclicker.ClickType;
import dev.bebomny.beaver.beaverutils.features.features.autoclicker.MouseClickAction;

public class AutoClickerConfig {

//    @Expose
//    public int delay = 30;
//
//    @Expose
//    public AutoClicker.Mode mode = AutoClicker.Mode.ATTACK;
//
//    @Expose
//    public ClickType type = ClickType.CLICK;

    @Expose
    public MouseClickAction leftClickAction;

    @Expose
    public MouseClickAction rightClickAction;

    public AutoClickerConfig() {

    }
}
