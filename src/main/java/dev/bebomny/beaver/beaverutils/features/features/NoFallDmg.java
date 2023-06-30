package dev.bebomny.beaver.beaverutils.features.features;

import dev.bebomny.beaver.beaverutils.configuration.config.NoFallDmgConfig;
import dev.bebomny.beaver.beaverutils.features.SimpleOnOffFeature;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.network.packet.c2s.play.PlayerMoveC2SPacket;

public class NoFallDmg extends SimpleOnOffFeature {

    private NoFallDmgConfig noFallDmgConfig = config.noFallDmgConfig;

    public NoFallDmg() {
        super("NoFallDmg");

        setEnableConfig(noFallDmgConfig);

        if(config.generalConfig.autoEnable)
            setEnabled(enableConfig.enabled);

        //TODO: Add modes(onground, itp)

        ClientTickEvents.END_CLIENT_TICK.register(this::onUpdate);
    }

    private void onUpdate(MinecraftClient client) {
        if(!isEnabled() || client.player == null) return;

        ClientPlayerEntity playerEntity = client.player;

        if (playerEntity.fallDistance <= (playerEntity.isFallFlying() ? 1 : 2))
            return;

        if (playerEntity.isFallFlying() && playerEntity.isSneaking() && !isFallingFastEnoughToCauseDamage(playerEntity))
            return;

        ItemStack chest = client.player.getEquippedStack(EquipmentSlot.CHEST);
        if(playerEntity.isFallFlying() && chest.getItem() == Items.ELYTRA)
            return;

        playerEntity.networkHandler.sendPacket(new PlayerMoveC2SPacket.OnGroundOnly(true));
    }

    public boolean isFallingFastEnoughToCauseDamage(ClientPlayerEntity player) {
        return player.getVelocity().y < -0.5;
    }
}
