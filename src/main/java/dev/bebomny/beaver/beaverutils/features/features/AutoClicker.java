package dev.bebomny.beaver.beaverutils.features.features;

import dev.bebomny.beaver.beaverutils.configuration.config.AutoClickerConfig;
import dev.bebomny.beaver.beaverutils.configuration.gui.menus.AutoClickerMenu;
import dev.bebomny.beaver.beaverutils.features.KeyOnOffFeature;
import dev.bebomny.beaver.beaverutils.features.features.autoclicker.MouseButton;
import dev.bebomny.beaver.beaverutils.features.features.autoclicker.MouseClickAction;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.minecraft.client.MinecraftClient;
import org.lwjgl.glfw.GLFW;

public class AutoClicker extends KeyOnOffFeature {

    private final AutoClickerConfig autoClickerConfig = config.autoClickerConfig;

    private long ticksPast;

    private final MouseClickAction[] mouseClickActions;

    public AutoClicker() {
        super("AutoClicker");

        //TODO: update ticks at the end of the function and store them as long!
        this.ticksPast = 0;

        if(autoClickerConfig.leftClickAction == null) {
            autoClickerConfig.leftClickAction = MouseClickAction.defaultMouseClickAction(MouseButton.LEFT);
        }

        if(autoClickerConfig.rightClickAction == null) {
            autoClickerConfig.rightClickAction = MouseClickAction.defaultMouseClickAction(MouseButton.RIGHT);
        }

        mouseClickActions = new MouseClickAction[]{autoClickerConfig.rightClickAction, autoClickerConfig.leftClickAction};

        addActivationKeybinding(GLFW.GLFW_KEY_N);
        setOptionsMenu(new AutoClickerMenu());

        ClientTickEvents.START_CLIENT_TICK.register(this::onUpdate);
    }

    private void onUpdate(MinecraftClient client) {
        if(!isEnabled())
            return;

        for (MouseClickAction action : mouseClickActions) {
            action.performAction(ticksPast);
        }

        ticksPast++;
    }

    public MouseClickAction[] getMouseClickActions() {
        return mouseClickActions;
    }

    @Override
    protected void onDisable() {
        super.onDisable();
        client.options.attackKey.setPressed(false);
        client.options.useKey.setPressed(false);
    }
}
