package dev.bebomny.beaver.beaverutils.features.features;

import dev.bebomny.beaver.beaverutils.configuration.config.AutoPlantConfig;
import dev.bebomny.beaver.beaverutils.features.KeyOnOffFeature;
import dev.bebomny.beaver.beaverutils.notifications.Categories;
import dev.bebomny.beaver.beaverutils.notifications.Notification;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.minecraft.block.AirBlock;
import net.minecraft.block.BlockState;
import net.minecraft.block.FarmlandBlock;
import net.minecraft.client.MinecraftClient;
import net.minecraft.item.Item;
import net.minecraft.item.Items;
import net.minecraft.util.Hand;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.Vec3d;
import org.lwjgl.glfw.GLFW;

import java.util.ArrayList;
import java.util.List;

public class AutoPlant extends KeyOnOffFeature {

    private AutoPlantConfig autoPlantConfig = config.autoPlantConfig;
    private List<Item> seeds = new ArrayList<>();


    public AutoPlant() {
        super("AutoPlant");

        addActivationKeybinding(GLFW.GLFW_KEY_UNKNOWN);
        setEnableConfig(autoPlantConfig);

        if(config.generalConfig.autoEnable)
            setEnabled(autoPlantConfig.enabled);

        initializeSeeds();

        ClientTickEvents.END_CLIENT_TICK.register(this::onUpdate);
    }

    private void onUpdate(MinecraftClient client) {
        if(client.player == null)
            return;

        if(!isEnabled())
            return;

        for(int y = -1; y <= 0; y++) {
            for(int x = -(autoPlantConfig.plantRadius); x <= autoPlantConfig.plantRadius; x++) {
                for(int z = -(autoPlantConfig.plantRadius); z <= autoPlantConfig.plantRadius; z++) {
                    if(tryPlant(client, client.player.getBlockPos().add(x,y,z)))
                        return;
                }
            }
        }
    }

    public boolean tryPlant(MinecraftClient client, BlockPos pos) {
        BlockState blockState = client.world.getBlockState(pos);
        if(blockState.getBlock() instanceof FarmlandBlock) {
            BlockState blockStateUp = client.world.getBlockState(pos.up());
            if(blockStateUp.getBlock() instanceof AirBlock) {
                return tryUseSeed(client, pos, Hand.MAIN_HAND) || tryUseSeed(client, pos, Hand.OFF_HAND);
            }
        }
        return false;
    }

    public boolean tryUseSeed(MinecraftClient client, BlockPos pos, Hand hand) {
        Item item = client.player.getStackInHand(hand).getItem();
        if(seeds.contains(item)) {
            Vec3d blockPos = new Vec3d(pos.getX(), pos.getY(), pos.getZ());
            BlockHitResult hit = new BlockHitResult(blockPos, Direction.UP, pos, false);
            client.interactionManager.interactBlock(client.player, hand, hit);

            notifier.newNotification(
                    Notification.builder("Seed Planted").category(Categories.FEATURE, this.getName()).build()
            );

            return true;
        }
        return false;
    }

    public void changeMode() {
        setMode(autoPlantConfig.mode == Mode.DontLookAt ? Mode.LookAt
                : autoPlantConfig.mode == Mode.LookAt ? Mode.LookAtWithInterpolation
                : autoPlantConfig.mode == Mode.LookAtWithInterpolation ? Mode.OnlyReplant
                : autoPlantConfig.mode == Mode.OnlyReplant ? Mode.DontLookAt
                : Mode.OnlyReplant);
    }

    public void setPlantRadius(int newRadius) {
        autoPlantConfig.plantRadius = newRadius;
    }

    public void setMode(Mode newMode) {
        autoPlantConfig.mode = newMode;
    }

    public int getPlantRadius() {
        return autoPlantConfig.plantRadius;
    }

    public Mode getMode() {
        return autoPlantConfig.mode;
    }

    public void initializeSeeds() {
        seeds.add(Items.WHEAT_SEEDS);
        seeds.add(Items.BEETROOT_SEEDS);
        seeds.add(Items.POTATO);
        seeds.add(Items.CARROT);
        seeds.add(Items.MELON_SEEDS);
        seeds.add(Items.PUMPKIN_SEEDS);
    }

    public enum Mode {
        DontLookAt,
        LookAt,
        LookAtWithInterpolation,
        OnlyReplant
    }
}
