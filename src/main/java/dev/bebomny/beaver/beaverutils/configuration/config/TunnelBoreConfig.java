package dev.bebomny.beaver.beaverutils.configuration.config;

import com.google.gson.annotations.Expose;

public class TunnelBoreConfig extends EnableConfigOption{

    @Expose
    int stripSpacing = 3;

    public TunnelBoreConfig() {

    }
}
