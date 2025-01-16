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

    private MouseClickAction[] mouseClickActions;

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

//        switch(autoClickerConfig.type) {
//            case ClickType.CLICK -> {
//                if(ticksPast >= getDelay()) {
//                    switch (autoClickerConfig.mode) {
//                        case ATTACK -> {
//                            ((IMinecraftClientInvoker) client).invokeDoAttack();
//                            notifier.newNotification(Notification.builder(Text.translatable("feature.auto_clicker.click.attacked.text"))
//                                    .parent(Text.translatable("feature.text"))
//                                    .duration(10)
//                                    .build());
//                        }
//
//                        case USE -> {
//                            ((IMinecraftClientInvoker) client).invokeDoItemUse();
//                            notifier.newNotification(Notification.builder(Text.translatable("feature.auto_clicker.click.used_item.text"))
//                                    .parent(Text.translatable("feature.text"))
//                                    .duration(10)
//                                    .build());
//                        }
//
//                        case BOTH -> {
//                            ((IMinecraftClientInvoker) client).invokeDoAttack();
//                            ((IMinecraftClientInvoker) client).invokeDoItemUse();
//                            notifier.newNotification(Notification.builder(Text.translatable("feature.auto_clicker.click.both.text"))
//                                    .parent(Text.translatable("feature.text"))
//                                    .duration(10)
//                                    .build());
//                        }
//                    }
//                    ticksPast = 0;
//                }
//                ticksPast++;
//            }
//
//            case ClickType.HOLD -> {
//                switch (autoClickerConfig.mode) {
//                    case ATTACK -> {
//                        client.options.attackKey.setPressed(true);
//                        notifier.newNotification(Notification.builder(Text.translatable("feature.auto_clicker.hold.attack.text"))
//                                .parent(Text.translatable("feature.text"))
//                                .duration(10)
//                                .build());
//                    }
//
//                    case USE -> {
//                        client.options.useKey.setPressed(true);
//                        notifier.newNotification(Notification.builder(Text.translatable("feature.auto_clicker.hold.use_item.text"))
//                                .parent(Text.translatable("feature.text"))
//                                .duration(10)
//                                .build());
//                    }
//
//                    case BOTH -> {
//                        client.options.attackKey.setPressed(true);
//                        client.options.useKey.setPressed(true);
//                        notifier.newNotification(Notification.builder(Text.translatable("feature.auto_clicker.hold.both.text"))
//                                .parent(Text.translatable("feature.text"))
//                                .duration(10)
//                                .build());
//                    }
//                }
//            }
//        }

    }

//    public void changeMode() {
//        setMode(
//                getMode() == Mode.ATTACK
//                        ? Mode.USE : getMode() == Mode.BOTH
//                        ? Mode.ATTACK : Mode.BOTH);
//    }
//
//    public void changeType() {
//        setType(getType() == Type.CLICK ? Type.HOLD : Type.CLICK);
//    }
//
//    public int getDelay() {
//        return autoClickerConfig.delay;
//    }
//
//    public Type getType() {
//        return autoClickerConfig.type;
//    }
//
//    public Mode getMode() {
//        return autoClickerConfig.mode;
//    }
//
//    public void setDelay(int newDelay) {
//        autoClickerConfig.delay = newDelay;
//    }
//
//    public void setType(Type newType) {
//        autoClickerConfig.type = newType;
//    }
//
//    public void setMode(Mode newMode) {
//        autoClickerConfig.mode = newMode;
//    }

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
