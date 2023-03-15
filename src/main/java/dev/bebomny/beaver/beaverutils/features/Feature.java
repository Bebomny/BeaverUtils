package dev.bebomny.beaver.beaverutils.features;

import dev.bebomny.beaver.beaverutils.client.BeaverUtilsClient;
import dev.bebomny.beaver.beaverutils.helpers.Notification;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.keybinding.v1.KeyBindingHelper;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.option.KeyBinding;
import net.minecraft.text.Text;

import java.awt.*;

public abstract class Feature {

    private final String name;
    private boolean enabled = false;
    private boolean active = false;
    private final boolean useActivationKeyBind;
    private final KeyBinding activationKey;
    private final BeaverUtilsClient modBeaverUtils;

    public Feature(String name, int key, BeaverUtilsClient mod) {
        this.name = name;
        this.useActivationKeyBind = true;
        this.activationKey = new KeyBinding(name, key, "BeaverUtils");
        this.modBeaverUtils = mod;
        //register
        ClientTickEvents.END_CLIENT_TICK.register(this::checkKeyBindPress);
        ClientTickEvents.END_CLIENT_TICK.register(this::onUpdate);
        KeyBindingHelper.registerKeyBinding(activationKey);
    }

    public Feature(String name, BeaverUtilsClient mod) {
        this.name = name;
        this.useActivationKeyBind = false;
        this.activationKey = null;
        this.modBeaverUtils = mod;
        //register
        ClientTickEvents.END_CLIENT_TICK.register(this::onUpdate);
    }

    public final boolean isEnabled() {
        return enabled;
    }

    public final boolean isActive() {
        return active;
    }

    public final String getName() {
        return name;
    }

    public void setEnabled(boolean enabled) {
        if(this.enabled == enabled) {
            return;
        }

        this.enabled = enabled;

        if(enabled) {
            onEnable();
        } else {
            setActive(false);
            onDisable();
        }

        /*
        if((!useActivationKeyBind) && enabled)
            onEnable();

        if(!enabled) {
            setActive(false);
            onDisable();
        }
        */

    }

    public void setActive(boolean active) {
        if(this.active == active)
            return;

        this.active = active;

        if(active && isEnabled()) {
            //onEnable();
            onActivation();
        }
        else if(!active && isEnabled()) {
            //onDisable();
            onDeactivation();
        }


    }

    private void checkKeyBindPress(MinecraftClient client) {
        if(!this.isEnabled())
            return;

        while(activationKey.wasPressed()) {
            setActive(!active);
        }
    }

    protected void onUpdate(MinecraftClient client) {

    }

    protected void onEnable() {
        modBeaverUtils.notifier.newNotification(new Notification(Text.literal(getName() + " Enabled"), new Color(0x00FF00)));
    }

    protected void onDisable() {
        modBeaverUtils.notifier.newNotification(new Notification(Text.literal(getName() + " Disabled"), new Color(0xFF00000)));
    }

    protected void onActivation() {
        modBeaverUtils.notifier.newNotification(new Notification(Text.literal(getName() + " Activated"), new Color(0x00FF00)));
    }

    protected void onDeactivation() {
        modBeaverUtils.notifier.newNotification(new Notification(Text.literal(getName() + " Deactivated"), new Color(0xFF00000)));
    }

}
