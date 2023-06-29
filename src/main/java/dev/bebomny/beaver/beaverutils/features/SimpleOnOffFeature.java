package dev.bebomny.beaver.beaverutils.features;

import dev.bebomny.beaver.beaverutils.configuration.config.EnableConfigOption;
import dev.bebomny.beaver.beaverutils.notifications.Categories;
import dev.bebomny.beaver.beaverutils.notifications.Notification;

public abstract class SimpleOnOffFeature extends Feature{

    protected boolean enabled;
    protected EnableConfigOption enableConfig;

    public SimpleOnOffFeature(String name) {
        super(name);
    }

    protected void onEnable() {
        notifier.newNotification(Notification.builder(
                        "§l§a" + getName() + " §l§aEnabled")
                .category(Categories.STATE, getName())
                .build());

        if(enableConfig != null)
            enableConfig.enabled = true;
    }

    protected void onDisable() {
        notifier.newNotification(Notification.builder(
                        "§l§c" + getName() + " §l§cDisabled")
                .category(Categories.STATE, getName())
                .build());

        if(enableConfig != null)
            enableConfig.enabled = false;
    }

    protected void setEnableConfig(EnableConfigOption enableConfig) {
        this.enableConfig = enableConfig;
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
