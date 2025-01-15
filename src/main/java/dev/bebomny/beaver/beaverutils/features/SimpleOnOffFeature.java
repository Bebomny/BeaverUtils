package dev.bebomny.beaver.beaverutils.features;

import dev.bebomny.beaver.beaverutils.configuration.config.EnableConfigOption;
import dev.bebomny.beaver.beaverutils.configuration.gui.menus.OptionsMenu;
import dev.bebomny.beaver.beaverutils.notifications.Notification;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;

public abstract non-sealed class SimpleOnOffFeature extends Feature {

    protected boolean enabled;
    protected EnableConfigOption enableConfig;
    protected OptionsMenu optionsMenu;

    public SimpleOnOffFeature(String name) {
        super(name);
    }

    protected void onEnable() {
        notifier.newNotification(Notification
                .builder(Text.translatable("feature.simple_on_off.enabled", getName()).formatted(Formatting.WHITE))
                .parent(Text.translatable("feature.text"))
                .build());

        if (enableConfig != null)
            enableConfig.enabled = true;
    }

    protected void onDisable() {
        notifier.newNotification(Notification
                .builder(Text.translatable("feature.simple_on_off.disabled", getName()).formatted(Formatting.WHITE))
                .parent(Text.translatable("feature.text"))
                .build());

        if (enableConfig != null)
            enableConfig.enabled = false;
    }

    protected void setEnableConfig(EnableConfigOption enableConfig) {
        this.enableConfig = enableConfig;

        if (this.enableConfig != null && config.generalConfig.autoEnable)
            setEnabled(this.enableConfig.enabled);
    }

    protected void setOptionsMenu(OptionsMenu optionsMenu) {
        this.optionsMenu = optionsMenu;
    }

    public OptionsMenu getOptionsMenu(Screen parent) {
        optionsMenu.setParent(parent);
        return optionsMenu;
    }

    public boolean isEnabled() {
        return enabled;
    }

    public void setEnabled(boolean enabled) {
        if (this.enabled == enabled)
            return;

        this.enabled = enabled;

        if (enabled)
            onEnable();
        else
            onDisable();
    }
}
