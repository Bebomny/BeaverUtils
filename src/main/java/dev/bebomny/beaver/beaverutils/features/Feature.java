package dev.bebomny.beaver.beaverutils.features;


import dev.bebomny.beaver.beaverutils.client.BeaverUtilsClient;
import dev.bebomny.beaver.beaverutils.configuration.Config;
import dev.bebomny.beaver.beaverutils.notifications.NotificationHandler;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.tooltip.Tooltip;
import org.slf4j.Logger;

public abstract sealed class Feature permits SimpleOnOffFeature, TriggerFeature {

    protected final MinecraftClient client;
    protected final BeaverUtilsClient beaverUtilsClient;
    protected final NotificationHandler notifier;
    protected final Config config;
    protected final Logger LOGGER;

    //Feature specific
    public final String name;
    public Tooltip mainToolTip;

    public Feature(String name) {
        this.client = MinecraftClient.getInstance();
        this.beaverUtilsClient = BeaverUtilsClient.getInstance();
        this.notifier = beaverUtilsClient.getNotifier();
        this.config = beaverUtilsClient.getConfig();
        this.name = name;
        this.LOGGER = beaverUtilsClient.getLogger(name);
        this.mainToolTip = null;
    }

    public String getName() {
        return name;
    }

    public Tooltip getMainToolTip() {
        return mainToolTip;
    }

    protected void setMainToolTip(Tooltip newToolTip) {
        this.mainToolTip = newToolTip;
    }

}
