package dev.bebomny.beaver.beaverutils.features;

import dev.bebomny.beaver.beaverutils.configuration.config.EnableConfigOption;
import dev.bebomny.beaver.beaverutils.notifications.Categories;
import dev.bebomny.beaver.beaverutils.notifications.Notification;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.keybinding.v1.KeyBindingHelper;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.option.KeyBinding;

public abstract class KeyOnOffFeature extends Feature{

    //Allows for keybinding On/Off states

    protected boolean enabled;
    protected KeyBinding activationKey;
    protected int keybindingKey;
    private boolean keyPressListenerRegistered = false; //ym idk
    protected EnableConfigOption enableConfig;

    public KeyOnOffFeature(String name) {
        super(name);
    }

    protected void addActivationKeybinding(int kbKey) {
        if (this.keybindingKey == 0) this.keybindingKey = kbKey;

        this.activationKey = new KeyBinding(name, keybindingKey, "BeaverUtils");
        beaverUtilsClient.keyBindingHandler.registerKeyBinding(name, activationKey);
        LOGGER.info("Registered a keybinding for " + getName() + ", with key " + keybindingKey);

        if(!keyPressListenerRegistered & activationKey != null) {
            ClientTickEvents.END_CLIENT_TICK.register(this::checkKeyBindPress);
            keyPressListenerRegistered = true;
        }
    }

    private void checkKeyBindPress(MinecraftClient client) {
        while(activationKey.wasPressed()) {
            setEnabled(!isEnabled());
            this.keybindingKey = KeyBindingHelper.getBoundKeyOf(activationKey).getCode();
        }
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
