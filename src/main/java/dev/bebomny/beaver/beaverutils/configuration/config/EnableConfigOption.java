package dev.bebomny.beaver.beaverutils.configuration.config;

import com.google.gson.annotations.Expose;

public abstract class EnableConfigOption {

    @Expose
    public boolean enabled = false;
}
