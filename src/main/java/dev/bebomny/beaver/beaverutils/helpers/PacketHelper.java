package dev.bebomny.beaver.beaverutils.helpers;

import dev.bebomny.beaver.beaverutils.client.BeaverUtilsClient;
import dev.bebomny.beaver.beaverutils.mixins.ClientConnectionAccessor;
import net.minecraft.client.MinecraftClient;
import net.minecraft.network.listener.ServerPlayPacketListener;
import net.minecraft.network.packet.Packet;
import net.minecraft.network.packet.c2s.play.PlayerMoveC2SPacket;
import net.minecraft.util.math.Vec3d;

public class PacketHelper {

    public static void sendPositionImmediately(Vec3d pos, boolean onGround) {
        MinecraftClient client = BeaverUtilsClient.getInstance().client;
        ClientConnectionAccessor connection = (ClientConnectionAccessor) client.player.networkHandler.getConnection();
        Packet<ServerPlayPacketListener> packet = new PlayerMoveC2SPacket.PositionAndOnGround(pos.getX(), pos.getY(), pos.getZ(), onGround);
        connection._sendImmediately(packet, null);
    }

    public static void sendPacketImmediately(Packet<?> packet) {
        MinecraftClient client = BeaverUtilsClient.getInstance().client;
        ClientConnectionAccessor connection = (ClientConnectionAccessor) client.player.networkHandler.getConnection();
        connection._sendImmediately(packet, null);
    }
}
