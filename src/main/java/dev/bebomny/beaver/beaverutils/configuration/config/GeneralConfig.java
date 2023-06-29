package dev.bebomny.beaver.beaverutils.configuration.config;

import com.google.gson.annotations.Expose;

public class GeneralConfig {

    @Expose
    public boolean debug = true;

    @Expose
    public boolean autoEnable = false;
}