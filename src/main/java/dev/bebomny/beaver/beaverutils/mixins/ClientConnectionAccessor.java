package dev.bebomny.beaver.beaverutils.mixins;

import net.minecraft.network.ClientConnection;
import net.minecraft.network.Packet;
import net.minecraft.network.PacketCallbacks;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Invoker;

@Mixin(ClientConnection.class)
public interface ClientConnectionAccessor {

    @Invoker("sendImmediately")
    void sendImmediately(Packet<?> packet, @Nullable PacketCallbacks callbacks);
}
