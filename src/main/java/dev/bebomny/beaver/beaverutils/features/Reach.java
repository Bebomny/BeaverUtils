package dev.bebomny.beaver.beaverutils.features;

import dev.bebomny.beaver.beaverutils.client.BeaverUtilsClient;
import dev.bebomny.beaver.beaverutils.helpers.Notification;
import dev.bebomny.beaver.beaverutils.helpers.PacketHelper;
import net.fabricmc.loader.impl.lib.sat4j.core.Vec;
import net.minecraft.client.MinecraftClient;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.network.packet.c2s.play.PlayerInteractEntityC2SPacket;
import net.minecraft.text.Text;
import net.minecraft.util.Hand;
import net.minecraft.util.hit.EntityHitResult;
import net.minecraft.util.hit.HitResult;
import net.minecraft.util.math.Vec3d;
import org.lwjgl.glfw.GLFW;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public class Reach extends Feature{

    private final MinecraftClient client;
    private final BeaverUtilsClient modBeaverUtils;
    private float reachDistance;
    private int maxExtendedReachDistance;
    private Vec3d lastCorrectPlayerPos;
    private boolean attackedLastTick;
    private int attackedTicksAgo;

    public Reach(MinecraftClient client, BeaverUtilsClient modBeaverUtils) {
        super("Reach", GLFW.GLFW_KEY_G, modBeaverUtils);
        this.client = client;
        this.modBeaverUtils = modBeaverUtils;
        this.reachDistance = 5;
        this.maxExtendedReachDistance = 100;
        this.attackedLastTick = false;
        this.attackedTicksAgo = 0;

        if(client.player != null)
            this.lastCorrectPlayerPos = client.player.getPos();
    }

    public void onAttack() {
        if(isEnabled() && isActive()) {
            if(client.player == null || client.world == null)
                return;

            Vec3d playerPos = client.player.getPos();

            HitResult hitResult = client.player.raycast(maxExtendedReachDistance, client.getTickDelta(), true);

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

                    if(targets.isEmpty()) {
                        modBeaverUtils.notifier.newNotification(new Notification(Text.literal("Miss, No Targets Found, Hit Pos: "
                                + "x: " + String.format("%.5g", hitResult.getPos().getX())
                                + " y: " + String.format("%.5g", hitResult.getPos().getY())
                                + " z: " + String.format("%.5g", hitResult.getPos().getZ()))));
                        return;
                    }

                    targets.sort((a, b) -> (int) (a.squaredDistanceTo(blockPos) - b.squaredDistanceTo(blockPos)));

                    Entity target = targets.get(0);
                    Vec3d targetPos = blockPos.add(0, 1, 0);

                    executePathAndAttackTarget(client, modBeaverUtils, target, playerPos, targetPos);
                }

                case ENTITY -> {
                    EntityHitResult entityHitResult = (EntityHitResult) hitResult;
                    Entity target = entityHitResult.getEntity();
                    Vec3d targetPos = target.getPos();

                    executePathAndAttackTarget(client, modBeaverUtils, target, playerPos, targetPos);
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
                        Vec3d targetPos = blockPos.add(0, 1, 0);

                        executePathAndAttackTarget(client, modBeaverUtils, target, playerPos, targetPos);

                    } else {
                        modBeaverUtils.notifier.newNotification(new Notification(Text.literal("Miss, No Targets Found, Hit Pos: "
                                + "x: " + String.format("%.5g", hitResult.getPos().getX())
                                + " y: " + String.format("%.5g", hitResult.getPos().getY())
                                + " z: " + String.format("%.5g", hitResult.getPos().getZ()))));
                    }
                }
            }
        }
    }

    public void executePathAndAttackTarget(MinecraftClient client, BeaverUtilsClient modBeaverUtils, Entity target, Vec3d playerPos, Vec3d targetPos) {
        if(client.player == null || modBeaverUtils == null)
            return;

        if(client.player.getAttackCooldownProgress(0.0f) > 0.6f) {

            lastCorrectPlayerPos = playerPos;

            quickTeleportFromTo(playerPos, targetPos);
            //teleportFromTo(playerPos, targetPos);

            PlayerInteractEntityC2SPacket attackPacket = PlayerInteractEntityC2SPacket.attack(target, false);
            PacketHelper.sendPacketImmediately(attackPacket);
            client.player.swingHand(Hand.MAIN_HAND);
            modBeaverUtils.notifier.newNotification(new Notification(Text.literal("Attacked Target " + target.getType().toString() + " At Distance " + target.distanceTo(client.player) + "m")));

            quickTeleportFromTo(targetPos, playerPos);
            //teleportFromTo(targetPos, playerPos);

            client.player.setPosition(playerPos);
            modBeaverUtils.flight.forceFlyBypass(client);
            attackedLastTick = true;
            attackedTicksAgo = 2;
        }
    }

    public void teleportFromTo(Vec3d from, Vec3d to) {
        double distPerTp = 8.5d;
        double targetDistance = Math.ceil(from.distanceTo(to) / distPerTp);
        for(int i = 1; i <= targetDistance; i++) {
            Vec3d temp = from.lerp(to, i / targetDistance);
            PacketHelper.sendPositionImmediately(temp);
            if(i % 4 == 0) {
                try {
                    Thread.sleep((long)(1/20) * 1000);
                    //modBeaverUtils.LOGGER.atInfo().log("Sleep");
                } catch (InterruptedException e) {
                    modBeaverUtils.LOGGER.atInfo().log("Sleep Failed");
                    //e.printStackTrace();
                }
            }
        }
    }

    public void quickTeleportFromTo(Vec3d from, Vec3d to) {
        //bypass papers moved too fast check
        //by sending a lot of packets really fast
        //this somehow increases the max traveled distance
        sendQuickPositionPackets(8, from);

        //send position to teleport to
        PacketHelper.sendPositionImmediately(to);
    }

    public void sendQuickPositionPackets(int amount, Vec3d pos) {
        if(client.player == null)
            return;

        for(int i = 0; i < amount; i++) {
            PacketHelper.sendPositionImmediately(pos);
        }
    }

    public void quickCorrectPlayerPosition(Vec3d lastCorrectPlayerPos, Vec3d playerPos) {
        quickTeleportFromTo(playerPos, lastCorrectPlayerPos);
    }

    @Override
    protected void onUpdate(MinecraftClient client) {
        if(client.player == null)
            return;

        if(attackedLastTick)
            if(lastCorrectPlayerPos.distanceTo(client.player.getPos()) >= 15) {
                //correctPosition(lastCorrectPlayerPos, client.player.getPos());
                quickCorrectPlayerPosition(lastCorrectPlayerPos, client.player.getPos());
            }

        if(attackedTicksAgo <= 0 && attackedLastTick)
            attackedLastTick = false;

        if(attackedTicksAgo > 0)
            attackedTicksAgo--;


    }

    public void correctPosition(Vec3d lastCorrectPlayerPos, Vec3d playerPos) {
        teleportFromTo(playerPos, lastCorrectPlayerPos);
        //modBeaverUtils.LOGGER.atInfo().log("Position Corrected");
    }

    public int getMaxExtendedReachDistance() {
        return maxExtendedReachDistance;
    }

    public void setMaxExtendedReachDistance(int maxExtendedReachDistance) {
        this.maxExtendedReachDistance = maxExtendedReachDistance;
    }

    public float getReachDistance() {
        return reachDistance;
    }

    public void setReachDistance(float n) {
        reachDistance = n;
    }

    @Override
    public void onEnable() {
        super.onEnable();
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
