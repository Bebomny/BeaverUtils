package dev.bebomny.beaver.beaverutils.configuration.config;

import com.google.gson.annotations.Expose;

public class ElytraSpeedControlConfig extends EnableConfigOption{

    @Expose
    public float speedIncrement = 0.05f;

    @Expose
    public boolean launchControlEnabled = false;

    @Expose
    public float launchControlInitialVelocity = 20.0f;

    @Expose
    public boolean ctrlBoostIncrement = false;

    @Expose
    public float ctrlBoostIncrementSpeed = 0.12f;

    public ElytraSpeedControlConfig() {

    }
}
