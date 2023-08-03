package dev.bebomny.beaver.beaverutils.features.features;

import dev.bebomny.beaver.beaverutils.configuration.config.ElytraSpeedControlConfig;
import dev.bebomny.beaver.beaverutils.configuration.gui.menus.ElytraSpeedControlMenu;
import dev.bebomny.beaver.beaverutils.features.SimpleOnOffFeature;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.tooltip.Tooltip;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.network.packet.c2s.play.ClientCommandC2SPacket;
import net.minecraft.text.Text;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;

public class ElytraSpeedControl extends SimpleOnOffFeature {

    private final ElytraSpeedControlConfig elytraSpeedControlConfig = config.elytraSpeedControlConfig;

    public ElytraSpeedControl() {
        super("ElytraSpdCtrl");

        setEnableConfig(elytraSpeedControlConfig);
        setOptionsMenu(new ElytraSpeedControlMenu());
        setMainToolTip(Tooltip.of(Text.of("Elytra Speed Control - Control your elytra's speed with 'w' and 's' keys! No need for fireworks!")));

//        if(config.generalConfig.autoEnable)
//            setEnabled(enableConfig.enabled);

        ClientTickEvents.END_CLIENT_TICK.register(this::onUpdate);
    }

    private void onUpdate(MinecraftClient client) {
        if(client.player == null || !isEnabled())
            return;

        //TODO: Add launchControl

        ItemStack chest = client.player.getEquippedStack(EquipmentSlot.CHEST);
        if(chest.getItem() != Items.ELYTRA)
            return;

        if(client.player.isFallFlying()) {
            if(client.player.isTouchingWater()) {
                sendStartStopPacket();
                return;
            }

            float yaw = (float)Math.toRadians(client.player.getYaw());
            Vec3d boostForward = new Vec3d(
                    -MathHelper.sin(yaw) * elytraSpeedControlConfig.speedIncrement,
                    0,
                    MathHelper.cos(yaw) * elytraSpeedControlConfig.speedIncrement
            );

            Vec3d velocity = client.player.getVelocity();

            if(client.options.forwardKey.isPressed())
                client.player.setVelocity(velocity.add(boostForward));
            else if (client.options.backKey.isPressed())
                client.player.setVelocity(velocity.subtract(boostForward));

            return;
        }
    }

    private void sendStartStopPacket() {
        if(client.player == null)
            return;

        ClientCommandC2SPacket packet = new ClientCommandC2SPacket(client.player, ClientCommandC2SPacket.Mode.START_FALL_FLYING);
        client.player.networkHandler.sendPacket(packet);
    }

    public void setSpeedIncrement(float newSpeedIncrement) {
        elytraSpeedControlConfig.speedIncrement = newSpeedIncrement;
    }

    public float getSpeedIncrement() {
        return elytraSpeedControlConfig.speedIncrement;
    }
}
