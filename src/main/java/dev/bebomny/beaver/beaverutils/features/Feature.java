package dev.bebomny.beaver.beaverutils.features;

import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.keybinding.v1.KeyBindingHelper;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.option.KeyBinding;
import org.lwjgl.glfw.GLFW;

public abstract class Feature {

    private final String name;
    private boolean enabled = false;
    private boolean active = false;
    private final boolean useActivationKeyBind;
    private final KeyBinding activationKey;

    public Feature(String name ,int key) {
        this.name = name;
        this.useActivationKeyBind = true;
        this.activationKey = new KeyBinding(name, key, "BeaverUtils");
        //register
        ClientTickEvents.END_CLIENT_TICK.register(this::checkKeyBindPress);
        ClientTickEvents.END_CLIENT_TICK.register(this::onUpdate);
        KeyBindingHelper.registerKeyBinding(activationKey);
    }

    public Feature(String name) {
        this.name = name;
        this.useActivationKeyBind = false;
        this.activationKey = null;
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
        while(activationKey.wasPressed()) {
            setActive(!active);
        }
    }

    protected void onUpdate(MinecraftClient client) {

    }

    protected void onEnable() {

    }

    protected void onDisable() {

    }

    protected void onActivation() {

    }

    protected void onDeactivation() {

    }

}
