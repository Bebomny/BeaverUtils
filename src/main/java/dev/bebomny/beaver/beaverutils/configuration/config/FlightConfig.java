package dev.bebomny.beaver.beaverutils.configuration.config;

import com.google.gson.annotations.Expose;
import dev.bebomny.beaver.beaverutils.features.features.Flight;

public class FlightConfig extends EnableConfigOption{

    @Expose
    public Flight.Mode flightMode = Flight.Mode.Creative;

    @Expose
    public int floatingTickLimit = 30;

    public FlightConfig() {

    }
}
