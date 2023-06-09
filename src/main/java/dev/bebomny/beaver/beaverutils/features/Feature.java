package dev.bebomny.beaver.beaverutils.features;

import com.google.gson.annotations.Expose;
import dev.bebomny.beaver.beaverutils.client.BeaverUtilsClient;
import dev.bebomny.beaver.beaverutils.helpers.KeyBindingHandler;
import dev.bebomny.beaver.beaverutils.notifications.Categories;
import dev.bebomny.beaver.beaverutils.notifications.Notification;
import dev.bebomny.beaver.beaverutils.notifications.NotificationHandler;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.keybinding.v1.KeyBindingHelper;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.client.option.KeyBinding;
import net.minecraft.text.Text;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public abstract class Feature {

    protected final MinecraftClient client;
    protected final BeaverUtilsClient beaverUtilsClient;
    protected final NotificationHandler notifier;
    protected final Logger logger;

    //Feature specific
    public final String name;
    protected KeyBinding activationKey;
    @Expose
    protected boolean enabled;

    public Feature(String name) {
        this.client = MinecraftClient.getInstance();
        this.beaverUtilsClient = BeaverUtilsClient.getInstance();
        this.notifier = beaverUtilsClient.getNotifier();
        this.logger = beaverUtilsClient.getLogger(name);
        this.name = name;
        this.enabled = false; //TODO: Get from config
        this.activationKey = null;

        //register events
    }

    public Feature(String name, int key) {
        this.client = MinecraftClient.getInstance();
        this.beaverUtilsClient = BeaverUtilsClient.getInstance();
        this.notifier = beaverUtilsClient.getNotifier();
        this.logger = beaverUtilsClient.getLogger(name);
        this.name = name;
        this.enabled = false; //TODO: Get from config
        this.activationKey = new KeyBinding(name, key, "BeaverUtils");


        //register events
        beaverUtilsClient.keyBindingHandler.registerKeyBinding(activationKey);
        ClientTickEvents.END_CLIENT_TICK.register(this::checkKeyBindPress);
    }

    private void checkKeyBindPress(MinecraftClient client) {
        while(activationKey.wasPressed()) {
            setEnabled(!isEnabled());
        }
    }

    protected void onEnable() {
        notifier.newNotification(Notification.builder(
                "§l§a" + getName() + " §l§aEnabled")
                .category(Categories.STATE, getName())
                .build());
        logger.atInfo().log("XRay Enabled");
    }

    protected void onDisable() {
        notifier.newNotification(Notification.builder(
                "§l§c" + getName() + " §l§cDisabled")
                .category(Categories.STATE, getName())
                .build());
        logger.atInfo().log("XRay Disabled");
    }

    public String getName() {
        return name;
    }

    public void setEnabled(boolean enabled) {
        if(this.enabled == enabled)
            return;

        this.enabled = enabled;

        if(enabled)
            onEnable();
        else
            onDisable();
    }

    public boolean isEnabled() {
        return enabled;
    }


}
