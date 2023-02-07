package dev.bebomny.beaver.beaverutils.features;

import dev.bebomny.beaver.beaverutils.client.BeaverUtilsClient;
import dev.bebomny.beaver.beaverutils.helpers.MathHelper;
import dev.bebomny.beaver.beaverutils.helpers.Notification;
import dev.bebomny.beaver.beaverutils.helpers.PacketHelper;
import net.fabricmc.fabric.api.client.rendering.v1.HudRenderCallback;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.network.packet.c2s.play.PlayerInteractEntityC2SPacket;
import net.minecraft.text.Text;
import net.minecraft.util.Hand;
import net.minecraft.util.hit.EntityHitResult;
import net.minecraft.util.hit.HitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.RaycastContext;
import net.minecraft.world.RegistryWorldView;
import org.lwjgl.glfw.GLFW;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public class Reach extends Feature{

    private final MinecraftClient client;
    private final BeaverUtilsClient modBeaverUtils;
    private float reachDistance;

    private ArrayList<Entity> targets;

    public Reach(MinecraftClient client, BeaverUtilsClient modBeaverUtils) {
        super("Reach", GLFW.GLFW_KEY_G);
        this.client = client;
        this.modBeaverUtils = modBeaverUtils;
        this.reachDistance = 5;
    }

    public void onAttack() {
        if(isEnabled() && isActive()) {
            if(client.player == null || client.world == null)
                return;

            Vec3d playerPos = client.player.getPos();

            HitResult hitResult = client.player.raycast(100, client.getTickDelta(), true);

            switch (hitResult.getType()) {
                case BLOCK -> {
                    Vec3d blockPos = hitResult.getPos();
                    List<Entity> targets = new ArrayList<>();
                    for(Entity entity : client.world.getEntities()) {
                        if(!entity.isAlive())
                            continue;

                        if(entity instanceof LivingEntity && entity != client.player && entity.squaredDistanceTo(blockPos) < 10)
                            targets.add(entity);
                    }

                    if(!targets.isEmpty()) {
                        targets.sort((a, b) -> (int) (a.squaredDistanceTo(blockPos) - b.squaredDistanceTo(blockPos)));

                        Entity target = targets.get(0);
                        //Vec3d targetPos = client.world.getBlockState(new BlockPos(blockPos.add(0,1,0))).isAir() && client.world.getBlockState(new BlockPos(blockPos.add(0,2,0))).isAir() ? blockPos.add(0,1,0) : ;
                        Vec3d targetPos = blockPos.add(0, 1, 0);

                        if(client.player.getAttackCooldownProgress(0.0f) > 0.9f) {
                            teleportFromTo(client, playerPos, targetPos);

                            PlayerInteractEntityC2SPacket attackPacket = PlayerInteractEntityC2SPacket.attack(target, false);
                            PacketHelper.sendPacketImmediately(attackPacket);
                            client.player.swingHand(Hand.MAIN_HAND);
                            modBeaverUtils.notifier.newNotification(new Notification(Text.literal("Attacked Entity " + target.getType().toString() + " At Distance " + target.distanceTo(client.player) + "m")));

                            teleportFromTo(client, targetPos, playerPos);

                            client.player.setPosition(playerPos);
                        }

                    }
                }
                case ENTITY -> {
                    EntityHitResult entityHitResult = (EntityHitResult) hitResult;
                    Entity target = entityHitResult.getEntity();
                    Vec3d targetPos = target.getPos();
                    if(client.player.getAttackCooldownProgress(0.0f) > 0.9f) {
                        teleportFromTo(client, playerPos, targetPos);

                        PlayerInteractEntityC2SPacket attackPacket = PlayerInteractEntityC2SPacket.attack(target, false);
                        PacketHelper.sendPacketImmediately(attackPacket);
                        client.player.swingHand(Hand.MAIN_HAND);
                        modBeaverUtils.notifier.newNotification(new Notification(Text.literal("Attacked Entity " + target.getType().getName().toString() + " At Distance " + target.distanceTo(client.player) + "m")));
                        target.getEntityName();
                        teleportFromTo(client, targetPos, playerPos);

                        client.player.setPosition(playerPos);
                        modBeaverUtils.flight.forceFlyBypass(client);
                    }
                }
                case MISS -> {
                    Vec3d blockPos = hitResult.getPos();
                    List<Entity> targets = new ArrayList<>();
                    for(Entity entity : client.world.getEntities()) {
                        if(!entity.isAlive())
                            continue;

                        if(entity instanceof LivingEntity && entity != client.player && entity.squaredDistanceTo(blockPos) < 10)
                            targets.add(entity);
                    }

                    if(!targets.isEmpty()) {
                        targets.sort((a, b) -> (int) (a.squaredDistanceTo(blockPos) - b.squaredDistanceTo(blockPos)));

                        Entity target = targets.get(0);
                        //Vec3d targetPos = client.world.getBlockState(new BlockPos(blockPos.add(0,1,0))).isAir() && client.world.getBlockState(new BlockPos(blockPos.add(0,2,0))).isAir() ? blockPos.add(0,1,0) : ;
                        Vec3d targetPos = blockPos.add(0, 1, 0);

                        if(client.player.getAttackCooldownProgress(0.0f) > 0.9f) {
                            teleportFromTo(client, playerPos, targetPos);

                            PlayerInteractEntityC2SPacket attackPacket = PlayerInteractEntityC2SPacket.attack(target, false);
                            PacketHelper.sendPacketImmediately(attackPacket);
                            client.player.swingHand(Hand.MAIN_HAND);
                            modBeaverUtils.notifier.newNotification(new Notification(Text.literal("Attacked Entity " + target.getCustomName() + " At Distance " + target.distanceTo(client.player) + "m")));

                            teleportFromTo(client, targetPos, playerPos);

                            client.player.setPosition(playerPos);
                        }
                    } else {
                        modBeaverUtils.notifier.newNotification(new Notification(Text.literal("Miss, No Targets Found, Hit Pos: "
                                + "x: " + hitResult.getPos().getX()
                                + " y: " + hitResult.getPos().getY()
                                + " z: " + hitResult.getPos().getZ())));
                    }
                }
            }


            /*
            targets = new ArrayList<Entity>();
            for(Entity entity : client.world.getEntities()) {


                if(entity instanceof LivingEntity && entity != client.player && client.player.squaredDistanceTo(entity) < 100)
                    targets.add(entity);
            }

            if(!targets.isEmpty()) {
                targets.sort((a, b) -> (int)(a.distanceTo(client.player) - b.distanceTo(client.player) - a.raycast()));

                Entity target = targets.get(0);
                Vec3d lastPos = playerPos;
                if(client.player.getAttackCooldownProgress(0.0f) > 0.9f) {

                }
            }

             */

        }
    }

    public void teleportFromTo(MinecraftClient client, Vec3d from, Vec3d to) {
        ClientPlayerEntity player = client.player;
        double distPerTp = 8.5d;
        double targetDistance = Math.ceil(from.distanceTo(to) / distPerTp);
        for(int i = 1; i <= targetDistance; i++) {
            Vec3d temp = from.lerp(to, i / targetDistance);
            PacketHelper.sendPosition(temp);
            if(i % 4 == 0) {
                try {
                    Thread.sleep((long)((1/20) * 1000));
                    modBeaverUtils.LOGGER.atInfo().log("Sleep");
                } catch (InterruptedException e) {
                    modBeaverUtils.LOGGER.atInfo().log("Sleep Failed");
                    //e.printStackTrace();
                }
            }
        }
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

    @Override
    public void onActivation() {
        modBeaverUtils.notifier.newNotification(new Notification(Text.literal("Extended Reach Activated"), new Color(0x00FF00)));
    }

    @Override
    public void onDeactivation() {
        modBeaverUtils.notifier.newNotification(new Notification(Text.literal("Extended Reach Deactivated"), new Color(0xFF0000)));
    }

}
