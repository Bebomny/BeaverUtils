package dev.bebomny.beaver.beaverutils.features;

import dev.bebomny.beaver.beaverutils.client.BeaverUtilsClient;
import dev.bebomny.beaver.beaverutils.helpers.Notification;
import net.minecraft.client.MinecraftClient;
import net.minecraft.text.Text;
import net.minecraft.util.math.Vec3d;
import org.lwjgl.glfw.GLFW;

import java.awt.*;

public class Reach extends Feature{

    private final MinecraftClient client;
    private final BeaverUtilsClient modBeaverUtils;
    private float reachDistance;

    public Reach(MinecraftClient client, BeaverUtilsClient modBeaverUtils) {
        super("Reach");
        this.client = client;
        this.modBeaverUtils = modBeaverUtils;
        this.reachDistance = 5;
    }

    public void onAttack() {
        if(isEnabled() && isActive()) {
            modBeaverUtils.notifier.newNotification(new Notification(Text.literal("Attacked")));
            assert client.player != null;
            Vec3d playerPos = client.player.getPos();
        }
    }

    public void teleportFromTo(MinecraftClient client, Vec3d from, Vec3d to) {

    }

    public float getReachDistance() {
        return reachDistance;
    }

    public void setReachDistance(float n) {
        reachDistance = n;
    }

    @Override
    public void onEnable() {
        modBeaverUtils.notifier.newNotification(new Notification(Text.literal("Reach Enabled"), new Color(0x00FF00)));
    }

    @Override
    public void onDisable() {
        modBeaverUtils.notifier.newNotification(new Notification(Text.literal("Reach Disabled"), new Color(0xFF0000)));
    }

}
