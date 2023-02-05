package dev.bebomny.beaver.beaverutils.features;

import dev.bebomny.beaver.beaverutils.client.BeaverUtilsClient;
import dev.bebomny.beaver.beaverutils.mixinterface.IMinecraftClientInvoker;
import net.minecraft.client.MinecraftClient;
import net.minecraft.text.Text;
import org.lwjgl.glfw.GLFW;

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
                modBeaverUtils.notifier.newNotificationWithCustomDuration(Text.literal("AutoClicker Attacked"), 30);
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
        modBeaverUtils.notifier.newNotification(Text.literal("AutoClicker Enabled At " + delay + " tick intervals"));
    }

    @Override
    public void onDisable() {
        modBeaverUtils.notifier.newNotification(Text.literal("AutoClicker Disabled"));
    }

    @Override
    public void onActivation() {
        modBeaverUtils.notifier.newNotification(Text.literal("AutoClicker Activated At " + delay + " tick intervals"));
    }

    @Override
    public void onDeactivation() {
        modBeaverUtils.notifier.newNotification(Text.literal("AutoClicker Deactivated"));
    }
}
