package dev.bebomny.beaver.beaverutils.features;

import dev.bebomny.beaver.beaverutils.client.BeaverUtilsClient;
import dev.bebomny.beaver.beaverutils.helpers.Notification;
import dev.bebomny.beaver.beaverutils.helpers.PacketHelper;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.impl.client.indigo.renderer.helper.ColorHelper;
import net.minecraft.block.Blocks;
import net.minecraft.client.MinecraftClient;
import net.minecraft.text.Text;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import org.lwjgl.glfw.GLFW;

import java.awt.*;

public class Flight extends Feature{

    private final MinecraftClient client;
    private final BeaverUtilsClient modBeaverUtils;
    private int floatingTickCounter;
    private final static int floatingTickLimit = 20;
    private Vec3d oldPos;

    public Flight(MinecraftClient client, BeaverUtilsClient mod) {
        super("Flight", GLFW.GLFW_KEY_V);
        this.client = client;
        this.modBeaverUtils = mod;
    }

    @Override
    public void onUpdate(MinecraftClient client) {
        if(client.player == null)
            return;

        if(isEnabled() && isActive() && !client.player.getAbilities().allowFlying)
            client.player.getAbilities().allowFlying = true;

        //paper fly check bypass
        if(isEnabled() && client.player.getAbilities().flying) {

            if(client.player.getPos().getY() >= oldPos.getY() - 0.0433D)
                floatingTickCounter++;

            oldPos = client.player.getPos();

            if(floatingTickCounter >= floatingTickLimit) {
                if((client.player.world.getBlockState(new BlockPos(client.player.getPos().subtract(0.0, 0.0433D, 0.0))).isAir()) || (client.player.world.getBlockState(new BlockPos(client.player.getPos().subtract(0.0, 0.0433D, 0.0))).getBlock().equals(Blocks.WATER)))
                    forceFlyBypass(client);
                floatingTickCounter = 0;
            }
        }
    }

    public void forceFlyBypass(MinecraftClient client) {
        if(client.player == null)
            return;
        PacketHelper.sendPosition(client.player.getPos().subtract(0.0, 0.0433d, 0.0));
    }

    @Override
    public void onEnable() {
        assert client.player != null;
        client.player.getAbilities().allowFlying = true;
        oldPos = client.player.getPos();
        modBeaverUtils.notifier.newNotification(new Notification(Text.literal("Flight Enabled"), new Color(0x00FF00)));
    }

    @Override
    public void onDisable() {
        assert client.player != null;
        client.player.getAbilities().allowFlying = false;
        client.player.getAbilities().flying = false;
        modBeaverUtils.notifier.newNotification(new Notification(Text.literal("Flight Disabled"), new Color(0xFF0000)));
    }

    @Override
    public void onActivation() {
        assert client.player != null;
        client.player.getAbilities().allowFlying = true;
        modBeaverUtils.notifier.newNotification(new Notification(Text.literal("Flight Activated"), new Color(0x00FF00)));
    }

    @Override
    public void onDeactivation() {
        assert client.player != null;
        client.player.getAbilities().flying = false;
        client.player.getAbilities().allowFlying = false;
        modBeaverUtils.notifier.newNotification(new Notification(Text.literal("Flight Deactivated"), new Color(0xFF0000)));
    }
}
