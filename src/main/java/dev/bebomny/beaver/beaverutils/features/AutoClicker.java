package dev.bebomny.beaver.beaverutils.features;

import dev.bebomny.beaver.beaverutils.client.BeaverUtilsClient;
import dev.bebomny.beaver.beaverutils.helpers.Notification;
import dev.bebomny.beaver.beaverutils.mixinterface.IMinecraftClientInvoker;
import net.minecraft.client.MinecraftClient;
import net.minecraft.text.Text;
import org.lwjgl.glfw.GLFW;

import java.awt.Color;

public class AutoClicker extends Feature{

    private final MinecraftClient client;
    private final BeaverUtilsClient modBeaverUtils;
    public int delay;
    private int ticksPast;
    private Mode mode;
    private Type type;

    public AutoClicker (MinecraftClient client, BeaverUtilsClient modBeaverUtils) {
        super("AutoClicker", GLFW.GLFW_KEY_N, modBeaverUtils);
        this.client = client;
        this.modBeaverUtils = modBeaverUtils;
        this.delay = 30;
        this.ticksPast = 0;
        this.mode = Mode.ATTACK;
        this.type = Type.CLICK;
    }

    @Override
    protected void onUpdate(MinecraftClient client) {
        if(!isEnabled() || !isActive())
            return;

        switch (type) {

            case CLICK -> {
                if(ticksPast >= delay) {
                    switch (mode) {
                        case ATTACK -> {
                            ((IMinecraftClientInvoker) client).invokeDoAttack();
                            modBeaverUtils.notifier.newNotification(new Notification(Text.literal("AutoClicker Attacked"), new Color(0xFFFFFF), 30));
                        }

                        case USE -> {
                            ((IMinecraftClientInvoker) client).invokeDoItemUse();
                            modBeaverUtils.notifier.newNotification(new Notification(Text.literal("AutoClicker Used Item"), new Color(0xFFFFFF), 30));
                        }

                        case BOTH -> {
                            ((IMinecraftClientInvoker) client).invokeDoAttack();
                            ((IMinecraftClientInvoker) client).invokeDoItemUse();
                            modBeaverUtils.notifier.newNotification(new Notification(Text.literal("AutoClicker Attacked And Used Item"), new Color(0xFFFFFF), 30));
                        }
                    }
                    ticksPast = 0;
                }
                ticksPast++;
            }

            case HOLD -> {
                switch (mode) {
                    case ATTACK -> {
                        client.options.attackKey.setPressed(true);
                        modBeaverUtils.notifier.newNotification(new Notification(Text.literal("AutoClicker Holding Attack Button"), new Color(0xFFFFFF), 30));
                    }
                    case USE -> {
                        client.options.useKey.setPressed(true);
                        modBeaverUtils.notifier.newNotification(new Notification(Text.literal("AutoClicker Holding Use Item Button"), new Color(0xFFFFFF), 30));
                    }
                    case BOTH -> {
                        client.options.attackKey.setPressed(true);
                        client.options.useKey.setPressed(true);
                        modBeaverUtils.notifier.newNotification(new Notification(Text.literal("AutoClicker Holding Attack And UseItem Buttons"), new Color(0xFFFFFF), 30));
                    }
                }
            }
        }

    }

    public void changeMode() {
        mode = mode == Mode.ATTACK ? Mode.USE : mode == Mode.BOTH ? Mode.ATTACK : Mode.BOTH;
    }

    public void changeType() {
        type = type == Type.CLICK ? Type.HOLD : Type.CLICK;
    }

    public Mode getMode() {
        return mode;
    }

    public Type getType() {
        return type;
    }

    public void setDelay(int n) {
        delay = n;
    }

    @Override
    public void onEnable() {
        modBeaverUtils.notifier.newNotification(new Notification(Text.literal("AutoClicker Enabled At " + delay + " tick intervals"), new Color(0x00FF00)));
    }

    @Override
    public void onDisable() {
        modBeaverUtils.notifier.newNotification(new Notification(Text.literal("AutoClicker Disabled"), new Color(0xFF0000)));
    }

    @Override
    public void onActivation() {
        modBeaverUtils.notifier.newNotification(new Notification(Text.literal("AutoClicker Activated At " + delay + " tick intervals"), new Color(0x00FF00)));
    }

    @Override
    public void onDeactivation() {
        modBeaverUtils.notifier.newNotification(new Notification(Text.literal("AutoClicker Deactivated"), new Color(0xFF0000)));
        client.options.attackKey.setPressed(false);
        client.options.useKey.setPressed(false);
    }

    public enum Mode {
        USE,
        ATTACK,
        BOTH
    }

    public enum Type {
        CLICK,
        HOLD
    }

}


