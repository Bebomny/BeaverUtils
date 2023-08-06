package dev.bebomny.beaver.beaverutils.configuration;

import com.google.gson.annotations.Expose;
import dev.bebomny.beaver.beaverutils.configuration.config.*;

public class Config {

    //General config options
    @Expose
    public GeneralConfig generalConfig = new GeneralConfig();

    @Expose
    public QuickTeleportConfig quickTeleportConfig = new QuickTeleportConfig();

    @Expose
    public FullBrightConfig fullBrightConfig = new FullBrightConfig();

    @Expose
    public AutoClickerConfig autoClickerConfig = new AutoClickerConfig();

    @Expose
    public ReachConfig reachConfig = new ReachConfig();

    @Expose
    public FlightConfig flightConfig = new FlightConfig();

    @Expose
    public NoFallDmgConfig noFallDmgConfig = new NoFallDmgConfig();

    @Expose
    public AutoPlantConfig autoPlantConfig = new AutoPlantConfig();

    @Expose
    public InGameStatsConfig inGameStatsConfig = new InGameStatsConfig();

    @Expose
    public ElytraSpeedControlConfig elytraSpeedControlConfig = new ElytraSpeedControlConfig();

    @Expose
    public TunnelBoreConfig tunnelBoreConfig = new TunnelBoreConfig();

    @Expose
    public XRayConfig xRayConfig = new XRayConfig();





    public Config() {

    }
}
