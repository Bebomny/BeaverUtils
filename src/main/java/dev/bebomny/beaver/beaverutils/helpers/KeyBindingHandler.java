package dev.bebomny.beaver.beaverutils.helpers;

import dev.bebomny.beaver.beaverutils.client.BeaverUtilsClient;
import net.fabricmc.fabric.api.client.keybinding.v1.KeyBindingHelper;
import net.minecraft.client.option.KeyBinding;
import org.slf4j.Logger;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class KeyBindingHandler {

    private final BeaverUtilsClient beaverUtilsClient;
    private final Logger LOGGER;
    private HashMap<String, KeyBinding> keyBindings = new HashMap<>();

    public KeyBindingHandler() {
        this.beaverUtilsClient = BeaverUtilsClient.getInstance();
        this.LOGGER = beaverUtilsClient.getLogger("KeyBindHandler");
    }

    public boolean registerKeyBinding(KeyBinding keyBinding) {
        try {
            KeyBindingHelper.registerKeyBinding(keyBinding);
            keyBindings.put(keyBinding.getTranslationKey(), keyBinding);
            return true;
        } catch (Exception ignored) {
            LOGGER.atWarn().log("Couldn't register a keybinding with id: " + keyBinding.getDefaultKey().toString());
            return false;
        }
    }
}
