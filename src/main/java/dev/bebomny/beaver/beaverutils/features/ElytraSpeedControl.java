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
        super("ElytraSpeedCtrl", mod);
        this.client = client;
        this.modBeaverUtils = mod;
        this.instantFly = false;
        this.jumpTimer = 0;
    }

    @Override
    public void onUpdate(MinecraftClient client) {
        if(client.player == null)
            return;

        if(!isEnabled()) //check if module enabled
            return;

        if(jumpTimer > 0) //instant fly jump delay
            jumpTimer--;

        ItemStack chest = client.player.getEquippedStack(EquipmentSlot.CHEST); // get ItemStack from players chest EquipmentSlot
        if(chest.getItem() != Items.ELYTRA) //if that item is not an elytra then skip this loop
            return;

        if(client.player.isFallFlying()) { //check if player is flying on elytra right now

            if(client.player.isTouchingWater()) { //if player is touching water then stop this module
                sendStartStopPacket(); //send packet to the server that the player is stopping
                return;
            }

            float yaw = (float)Math.toRadians(client.player.getYaw()); //get player yqw angle
            Vec3d forward = new Vec3d(-MathHelper.sin(yaw) * 0.05, 0 , MathHelper.cos(yaw) * 0.05); // calculate forward vector based on the yaw of the player, so we can accelerate in more than one direction

            Vec3d velocity = client.player.getVelocity(); // get player's velocity

            if(client.options.forwardKey.isPressed()) // if pressing forward button, ex. 'W'
                client.player.setVelocity(velocity.add(forward)); // add forward vector to players velocity to accelerate
            else if (client.options.backKey.isPressed()) { // if pressing backwards button, ex. 'S'
                client.player.setVelocity(velocity.subtract(forward));// subtract forward vector from players velocity to decelerate
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

    private void sendStartStopPacket() {
        if(client.player == null)
            return;

        ClientCommandC2SPacket packet = new ClientCommandC2SPacket(client.player,
                ClientCommandC2SPacket.Mode.START_FALL_FLYING);
        client.player.networkHandler.sendPacket(packet);
    }

    public void setInstantFly(boolean instantFly) {
        this.instantFly = instantFly;
    }
}
