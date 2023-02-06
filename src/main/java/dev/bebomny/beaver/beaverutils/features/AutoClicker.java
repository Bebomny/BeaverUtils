package dev.bebomny.beaver.beaverutils.features;

import dev.bebomny.beaver.beaverutils.client.BeaverUtilsClient;
import dev.bebomny.beaver.beaverutils.helpers.Notification;
import dev.bebomny.beaver.beaverutils.mixinterface.IMinecraftClientInvoker;
import net.minecraft.client.MinecraftClient;
import net.minecraft.text.Text;
import org.lwjgl.glfw.GLFW;

import java.awt.*;

public class AutoClicker extends Feature{

    private final MinecraftClient client;
    private final BeaverUtilsClient modBeaverUtils;
    public int delay;
    private int ticksPast;

    public AutoClicker (MinecraftClient client, BeaverUtilsClient modBeaverUtils) {
        super("AutoClicker", GLFW.GLFW_KEY_N);
        this.client = client;
        this.modBeaverUtils = modBeaverUtils;
        delay = 30;
        ticksPast = 0;
    }

    @Override
    protected void onUpdate(MinecraftClient client) {
        if(isEnabled() && isActive()) {
            if(ticksPast >= delay) {
                ((IMinecraftClientInvoker) client).invokeDoAttack();
                modBeaverUtils.notifier.newNotification(new Notification(Text.literal("AutoClicker Attacked"), new Color(0xFFFFFF), 30));
                ticksPast = 0;
            }
            ticksPast++;
        }
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
    }
}
