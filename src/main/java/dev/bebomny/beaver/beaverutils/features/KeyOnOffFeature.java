package dev.bebomny.beaver.beaverutils.features;

import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.keybinding.v1.KeyBindingHelper;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.option.KeyBinding;

public abstract class KeyOnOffFeature extends SimpleOnOffFeature {

    //Allows for keybinding On/Off states
    protected KeyBinding activationKey;
    protected int keybindingKey;
    private boolean keyPressListenerRegistered = false; //ym idk

    public KeyOnOffFeature(String name) {
        super(name);
    }

    protected void addActivationKeybinding(int kbKey) {
        if (this.keybindingKey == 0) this.keybindingKey = kbKey;

        this.activationKey = new KeyBinding(name, keybindingKey, "BeaverUtils");
        beaverUtilsClient.keyBindingHandler.registerKeyBinding(name, activationKey);
        LOGGER.atInfo().log("Registered a keybinding for " + getName() + ", with key " + keybindingKey);

        if (!keyPressListenerRegistered & activationKey != null) {
            ClientTickEvents.END_CLIENT_TICK.register(this::checkKeyBindPress);
            keyPressListenerRegistered = true;
        }
    }

    private void checkKeyBindPress(MinecraftClient client) {
        while (activationKey.wasPressed()) {
            setEnabled(!isEnabled());
            this.keybindingKey = KeyBindingHelper.getBoundKeyOf(activationKey).getCode();
        }
    }

}
