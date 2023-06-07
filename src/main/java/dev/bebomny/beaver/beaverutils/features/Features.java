package dev.bebomny.beaver.beaverutils.features;


import com.google.gson.annotations.Expose;
import dev.bebomny.beaver.beaverutils.configuration.GeneralConfig;

//Class for config GSON serialization and direct access to feature's methods
public class Features {

    //General config options
    @Expose
    public final GeneralConfig general = new GeneralConfig();

    @Expose
    public final XRay xRay = new XRay();

    public Features() {

    }
}
