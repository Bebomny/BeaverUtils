package dev.bebomny.beaver.beaverutils.features;


import dev.bebomny.beaver.beaverutils.client.BeaverUtilsClient;
import dev.bebomny.beaver.beaverutils.configuration.Config;
import dev.bebomny.beaver.beaverutils.notifications.NotificationHandler;
import net.minecraft.client.MinecraftClient;
import org.slf4j.Logger;

public abstract class Feature {

    protected final MinecraftClient client;
    protected final BeaverUtilsClient beaverUtilsClient;
    protected final NotificationHandler notifier;
    protected final Config config;
    protected final Logger LOGGER;

    //Feature specific
    public final String name;

    public Feature(String name) {
        this.client = MinecraftClient.getInstance();
        this.beaverUtilsClient = BeaverUtilsClient.getInstance();
        this.notifier = beaverUtilsClient.getNotifier();
        this.config = beaverUtilsClient.getConfig();
        this.LOGGER = beaverUtilsClient.getLogger(name);
        this.name = name;
    }

    public String getName() {
        return name;
    }


}
