package dev.bebomny.beaver.beaverutils.features;

import dev.bebomny.beaver.beaverutils.client.BeaverUtilsClient;
import dev.bebomny.beaver.beaverutils.helpers.Notification;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.network.packet.c2s.play.PlayerMoveC2SPacket;
import net.minecraft.text.Text;

import java.awt.*;

public class NoFall extends Feature{

    private final MinecraftClient client;
    private final BeaverUtilsClient modBeaverUtils;

    public NoFall(MinecraftClient client, BeaverUtilsClient modBeaverUtils) {
        super("NoFall");
        this.client = client;
        this.modBeaverUtils = modBeaverUtils;
    }

    @Override
    public void onUpdate(MinecraftClient client) {
        if(isEnabled() && client.player != null) {
            ClientPlayerEntity player = client.player;
            if (player.fallDistance <= (player.isFallFlying() ? 1 : 2))
                return;

            if (player.isFallFlying() && player.isSneaking() && !isFallingFastEnoughToCauseDamage(player))
                return;

            player.networkHandler.sendPacket(new PlayerMoveC2SPacket.OnGroundOnly(true));
        }
    }

    public boolean isFallingFastEnoughToCauseDamage(ClientPlayerEntity player) {
        return player.getVelocity().y < -0.5;
    }

    @Override
    public void onEnable() {
        modBeaverUtils.notifier.newNotification(new Notification(Text.literal("NoFall Enabled"), new Color(0x00FF00)));
    }

    @Override
    public void onDisable() {
        modBeaverUtils.notifier.newNotification(new Notification(Text.literal("NoFall Disabled"), new Color(0xFF0000)));
    }

}
