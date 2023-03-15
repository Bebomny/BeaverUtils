package dev.bebomny.beaver.beaverutils.features;

import dev.bebomny.beaver.beaverutils.client.BeaverUtilsClient;
import dev.bebomny.beaver.beaverutils.helpers.Notification;
import net.minecraft.block.AirBlock;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.FarmlandBlock;
import net.minecraft.client.MinecraftClient;
import net.minecraft.item.Item;
import net.minecraft.item.Items;
import net.minecraft.text.Text;
import net.minecraft.util.Hand;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.Vec3d;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public class AutoPlant extends Feature{

    private final MinecraftClient client;
    private final BeaverUtilsClient modBeaverUtils;

    List<Item> seeds = new ArrayList<>();

    public AutoPlant(MinecraftClient client, BeaverUtilsClient modBeaverUtils) {
        super("AutoPlant", modBeaverUtils);
        this.client = client;
        this.modBeaverUtils = modBeaverUtils;
        initializeSeeds();
    }

    @Override
    public void onUpdate(MinecraftClient client) {
        if(client.player == null)
            return;

        if(!isEnabled())
            return;

        for(int y = -1; y <= 0; y++) {
            for(int x = -2; x <= 2; x++) {
                for(int z = -2; z <= 2; z++) {
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
            return true;
        }
        return false;
    }

    public void initializeSeeds() {
        seeds.add(Items.WHEAT_SEEDS);
        seeds.add(Items.BEETROOT_SEEDS);
        seeds.add(Items.POTATO);
        seeds.add(Items.CARROT);
        seeds.add(Items.MELON_SEEDS);
        seeds.add(Items.PUMPKIN_SEEDS);
    }
}
