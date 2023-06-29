package dev.bebomny.beaver.beaverutils.features;

import dev.bebomny.beaver.beaverutils.client.BeaverUtilsClient;
import dev.bebomny.beaver.beaverutils.features.features.*;

public class Features {

    private final BeaverUtilsClient beaverUtilsClient;

    public XRay xRay;
    public QuickTeleport quickTeleport;
    public FullBright fullBright;
    public Reach reach;
    public AutoClicker autoClicker;
    public InGameStats inGameStats;

    public Features() {
        this.beaverUtilsClient = BeaverUtilsClient.getInstance();

        //XRay
        this.xRay = new XRay();
        beaverUtilsClient.featureHandler.addFeature(xRay);

        this.quickTeleport = new QuickTeleport();
        beaverUtilsClient.featureHandler.addFeature(quickTeleport);

        this.fullBright = new FullBright();
        beaverUtilsClient.featureHandler.addFeature(fullBright);

        this.autoClicker = new AutoClicker();
        beaverUtilsClient.featureHandler.addFeature(autoClicker);

        this.reach = new Reach();
        beaverUtilsClient.featureHandler.addFeature(reach);

        this.inGameStats = new InGameStats();
        beaverUtilsClient.featureHandler.addFeature(inGameStats);
    }
}
