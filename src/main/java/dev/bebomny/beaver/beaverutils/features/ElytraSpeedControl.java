package dev.bebomny.beaver.beaverutils.features;

import dev.bebomny.beaver.beaverutils.client.BeaverUtilsClient;
import dev.bebomny.beaver.beaverutils.helpers.Notification;
import net.minecraft.client.MinecraftClient;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.item.ElytraItem;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.network.packet.c2s.play.ClientCommandC2SPacket;
import net.minecraft.text.Text;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;

import java.awt.*;

public class ElytraSpeedControl extends  Feature{

    private final MinecraftClient client;
    private final BeaverUtilsClient modBeaverUtils;
    public boolean instantFly;
    private int jumpTimer;

    public ElytraSpeedControl(MinecraftClient client, BeaverUtilsClient mod) {
        super("ElytraSpeedCtrl");
        this.client = client;
        this.modBeaverUtils = mod;
        this.instantFly = false;
        this.jumpTimer = 0;
    }

    @Override
    public void onUpdate(MinecraftClient client) {

        if(!isEnabled())
            return;

        if(client.player == null)
            return;

        if(jumpTimer > 0)
            jumpTimer--;

        ItemStack chest = client.player.getEquippedStack(EquipmentSlot.CHEST);
        if(chest.getItem() != Items.ELYTRA)
            return;

        if(client.player.isFallFlying()) {

            if(client.player.isTouchingWater()) {
                sendStartStopPacket();
                return;
            }

            float yaw = (float)Math.toRadians(client.player.getYaw());
            Vec3d forward = new Vec3d(-MathHelper.sin(yaw) * 0.05, 0 , MathHelper.cos(yaw) * 0.05);

            Vec3d velocity = client.player.getVelocity();

            if(client.options.forwardKey.isPressed())
                client.player.setVelocity(velocity.add(forward));
            else if (client.options.forwardKey.isPressed()) {
                client.player.setVelocity(velocity.subtract(forward));
            }
            return;
        }

        if(ElytraItem.isUsable(chest) && client.options.jumpKey.isPressed())
            doInstantFly();

    }

    private void doInstantFly() {

        if(!instantFly)
            return;

        if(jumpTimer > 0) {
            jumpTimer = 20;
            client.player.setJumping(false);
            client.player.setSprinting(true);
            client.player.jump();
        }

        sendStartStopPacket();
    }

    private void sendStartStopPacket()
    {
        ClientCommandC2SPacket packet = new ClientCommandC2SPacket(client.player,
                ClientCommandC2SPacket.Mode.START_FALL_FLYING);
        client.player.networkHandler.sendPacket(packet);
    }

    @Override
    public void onEnable() {
        modBeaverUtils.notifier.newNotification(new Notification(Text.literal("ElytraSpeedControl Enabled"), new Color(0x00FF00)));
    }

    @Override
    public void onDisable() {
        modBeaverUtils.notifier.newNotification(new Notification(Text.literal("ElytraSpeedControl Disabled"), new Color(0xFF0000)));
    }

    public void setInstantFly(boolean instantFly) {
        this.instantFly = instantFly;
    }
}
