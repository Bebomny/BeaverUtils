package dev.bebomny.beaver.beaverutils.configuration.config;

import com.google.gson.annotations.Expose;

public class ReachConfig extends EnableConfigOption{

    //@Expose
    //public boolean enabled = false;

    @Expose
    public float distance = 5.0f; //mc default is 4.5

    public ReachConfig() {

    }
}
