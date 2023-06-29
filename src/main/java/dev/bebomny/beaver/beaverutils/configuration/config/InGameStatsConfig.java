package dev.bebomny.beaver.beaverutils.configuration.config;

import com.google.gson.annotations.Expose;

public class InGameStatsConfig extends EnableConfigOption{

    //Currently in upper class
    //@Expose
    //public boolean enabled = false;

    @Expose
    public int bufferSize = 20;

    public InGameStatsConfig() {

    }
}
