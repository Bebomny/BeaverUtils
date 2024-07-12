package dev.bebomny.beaver.beaverutils.features;

import dev.bebomny.beaver.beaverutils.notifications.Notification;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.keybinding.v1.KeyBindingHelper;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.option.KeyBinding;
import net.minecraft.text.Text;

public abstract non-sealed class TriggerFeature extends Feature{

    protected KeyBinding fireKey;
    protected int keybindingKey;
    private boolean keyPressListenerRegistered = false; //ym idk

    public TriggerFeature(String name) {
        super(name);
    }

    protected void addFireKeybinding(int kbKey) {
        if (this.keybindingKey == 0) this.keybindingKey = kbKey;

        this.fireKey = new KeyBinding(name, keybindingKey, "BeaverUtils");
        beaverUtilsClient.keyBindingHandler.registerKeyBinding(name, fireKey);
        LOGGER.atInfo().log("Registered a keybinding for " + getName() + ", with key " + keybindingKey);

        if(!keyPressListenerRegistered & fireKey != null) {
            ClientTickEvents.END_CLIENT_TICK.register(this::checkKeyBindPress);
            keyPressListenerRegistered = true;
        }
    }

    private void checkKeyBindPress(MinecraftClient client) {
        while(fireKey.wasPressed()) {
            fire(FiredBy.KEY);
            this.keybindingKey = KeyBindingHelper.getBoundKeyOf(fireKey).getCode();
        }
    }

    public void fire(FiredBy firedBy) {
        notifier.newNotification(Notification
                .builder(Text.translatable("feature.trigger.fire_text", getName()))
                .parent(Text.translatable("feature.text"))
                .build());
    }

    public enum FiredBy {
        KEY,
        COMMAND,
        MENU,
        ELSE
    }
}
