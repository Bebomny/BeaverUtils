package dev.bebomny.beaver.beaverutils.helpers;

import dev.bebomny.beaver.beaverutils.client.BeaverUtilsClient;
import net.fabricmc.fabric.api.client.keybinding.v1.KeyBindingHelper;
import net.minecraft.client.option.KeyBinding;
import net.minecraft.text.Text;
import org.slf4j.Logger;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class KeyBindingHandler {

    private final BeaverUtilsClient beaverUtilsClient;
    private final Logger LOGGER;
    private final HashMap<String, KeyBinding> keyBindings = new HashMap<>();

    public KeyBindingHandler() {
        this.beaverUtilsClient = BeaverUtilsClient.getInstance();
        this.LOGGER = BeaverUtilsClient.getLogger("KeyBindingHandler");
    }

    public boolean registerKeyBinding(String name, KeyBinding keyBinding) {
        try {
            KeyBindingHelper.registerKeyBinding(keyBinding);
            keyBindings.put(keyBinding.toString(), keyBinding);
            LOGGER.atInfo().log(
                    String.format("Registered a keybinding for %s with key %d, %s",
                            name,
                            keyBinding.getDefaultKey().getCode(),
                            keyBinding.getDefaultKey().getTranslationKey()));
            return true;
        } catch (Exception e) {
            if(e instanceof NullPointerException)
                LOGGER.atWarn().log("Keybinding is null, probably this method doesnt require a keybinding");
            else
                LOGGER.atWarn().log("Couldn't register a keybinding with name: " + name);
            return false;
        }
    }
}
