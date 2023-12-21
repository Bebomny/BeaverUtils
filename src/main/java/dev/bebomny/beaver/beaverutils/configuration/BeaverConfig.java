package dev.bebomny.beaver.beaverutils.configuration;

import com.google.gson.annotations.Expose;
import dev.bebomny.beaver.beaverutils.configuration.annotations.Comment;
import dev.bebomny.beaver.beaverutils.configuration.annotations.ConfigGroup;
import dev.bebomny.beaver.beaverutils.configuration.annotations.ConfigGroupEnd;
import dev.bebomny.beaver.beaverutils.configuration.annotations.ConfigValue;
import dev.bebomny.beaver.beaverutils.features.features.AutoClicker;

public class BeaverConfig {

    //General
    @Expose
    @ConfigGroup("general")
    @Comment("yo mama debug")
    @ConfigValue("general.debug")
    public boolean debug = true;

    @Expose
    @ConfigGroupEnd
    @Comment("Automatically enables selected features for you on startup")
    @ConfigValue("general.auto-enable")
    public boolean autoEnable = true;

    //AutoClicker
    @Expose
    @ConfigGroup("auto-clicker")
    @Comment("Enables the autoClicker feature")
    @ConfigValue("auto-clicker.enabled")
    public boolean enabled = false;

    @Expose
    @Comment("Delay between clicks")
    @ConfigValue("auto-clicker.delay")
    public int delay = 30;

    @Expose
    @Comment("Mode which autoClicker uses. Attack/Use/Both")
    @ConfigValue("auto-clicker.mode")
    public AutoClicker.Mode mode = AutoClicker.Mode.ATTACK;

    @Expose
    @ConfigGroupEnd
    @Comment("Type of Clicks" + "\n" + "Click/Hold")
    @ConfigValue("auto-clicker.type")
    public AutoClicker.Type type = AutoClicker.Type.CLICK;
}
