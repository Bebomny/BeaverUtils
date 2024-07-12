package dev.bebomny.beaver.beaverutils.configuration.config;

import com.google.gson.annotations.Expose;

public class GeneralConfig {

    @Expose
    public int softNotificationMax = 3;

    @Expose
    public boolean debug = true;

    @Expose
    public boolean autoEnable = true;
}