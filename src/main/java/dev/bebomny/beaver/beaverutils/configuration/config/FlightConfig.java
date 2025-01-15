package dev.bebomny.beaver.beaverutils.configuration.config;

import com.google.gson.annotations.Expose;
import dev.bebomny.beaver.beaverutils.features.features.Flight;

public class FlightConfig extends EnableConfigOption{

    @Expose
    public Flight.Mode flightMode = Flight.Mode.Creative;

    @Expose
    public int floatingTickLimit = 30;

    @Expose
    public float flightSpeed = 0.05f;

    @Expose
    public boolean ctrlBoost = false;

    @Expose
    public float ctrlBoostSpeed = 0.12f;

    public FlightConfig() {

    }
}
