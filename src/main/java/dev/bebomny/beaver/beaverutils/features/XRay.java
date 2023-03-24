package dev.bebomny.beaver.beaverutils.features;

import dev.bebomny.beaver.beaverutils.client.BeaverUtilsClient;
import dev.bebomny.beaver.beaverutils.helpers.Notification;
import net.minecraft.block.Block;
import net.minecraft.block.Blocks;
import net.minecraft.client.MinecraftClient;
import net.minecraft.text.Text;
import org.lwjgl.glfw.GLFW;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public class XRay extends Feature{

    private final List<Block> interestingBlocks = new ArrayList<>();
    private final MinecraftClient client;
    private final BeaverUtilsClient modBeaverUtils;
    public XRay(MinecraftClient client, BeaverUtilsClient modBeaverUtils) {
        super("XRay", GLFW.GLFW_KEY_X, modBeaverUtils);
        this.client = client;
        this.modBeaverUtils = modBeaverUtils;
        initializeInterestingBlocks();
    }

    public boolean isInterestingBlock(Block block) {
        return interestingBlocks.contains(block);
    }

    private void initializeInterestingBlocks() {
        //chests
        interestingBlocks.add(Blocks.CHEST);
        interestingBlocks.add(Blocks.ENDER_CHEST);
        interestingBlocks.add(Blocks.BARREL);
        //furnaces
        interestingBlocks.add(Blocks.FURNACE);
        interestingBlocks.add(Blocks.BLAST_FURNACE);
        interestingBlocks.add(Blocks.SMOKER);
        //Other
        interestingBlocks.add(Blocks.GRINDSTONE);
        interestingBlocks.add(Blocks.ENCHANTING_TABLE);
        interestingBlocks.add(Blocks.LOOM);
        interestingBlocks.add(Blocks.SMITHING_TABLE);
        interestingBlocks.add(Blocks.BEACON);
        interestingBlocks.add(Blocks.DRAGON_EGG);
        interestingBlocks.add(Blocks.END_PORTAL_FRAME);
        interestingBlocks.add(Blocks.END_PORTAL);
        //Redstone thingies
        interestingBlocks.add(Blocks.HOPPER);
        interestingBlocks.add(Blocks.COMPARATOR);
        interestingBlocks.add(Blocks.REPEATER);
        interestingBlocks.add(Blocks.PISTON);
        interestingBlocks.add(Blocks.STICKY_PISTON);
        interestingBlocks.add(Blocks.DISPENSER);
        interestingBlocks.add(Blocks.DROPPER);
        interestingBlocks.add(Blocks.JUKEBOX);
        interestingBlocks.add(Blocks.NOTE_BLOCK);
        interestingBlocks.add(Blocks.TNT);
        //netherite
        interestingBlocks.add(Blocks.ANCIENT_DEBRIS);
        interestingBlocks.add(Blocks.NETHERITE_BLOCK);
        //quartz
        interestingBlocks.add(Blocks.NETHER_QUARTZ_ORE);
        //emeralds
        interestingBlocks.add(Blocks.EMERALD_BLOCK);
        interestingBlocks.add(Blocks.EMERALD_ORE);
        interestingBlocks.add(Blocks.DEEPSLATE_EMERALD_ORE);
        //diamonds
        interestingBlocks.add(Blocks.DIAMOND_BLOCK);
        interestingBlocks.add(Blocks.DIAMOND_ORE);
        interestingBlocks.add(Blocks.DEEPSLATE_DIAMOND_ORE);
        //lapis
        interestingBlocks.add(Blocks.LAPIS_ORE);
        interestingBlocks.add(Blocks.LAPIS_BLOCK);
        interestingBlocks.add(Blocks.DEEPSLATE_LAPIS_ORE);
        //REDSTONE
        interestingBlocks.add(Blocks.REDSTONE_ORE);
        interestingBlocks.add(Blocks.REDSTONE_BLOCK);
        interestingBlocks.add(Blocks.DEEPSLATE_REDSTONE_ORE);
        //GOLD
        interestingBlocks.add(Blocks.GOLD_BLOCK);
        interestingBlocks.add(Blocks.GOLD_ORE);
        interestingBlocks.add(Blocks.RAW_GOLD_BLOCK);
        interestingBlocks.add(Blocks.NETHER_GOLD_ORE);
        interestingBlocks.add(Blocks.DEEPSLATE_GOLD_ORE);
        //IRON
        interestingBlocks.add(Blocks.IRON_ORE);
        interestingBlocks.add(Blocks.IRON_BLOCK);
        interestingBlocks.add(Blocks.DEEPSLATE_IRON_ORE);
        interestingBlocks.add(Blocks.RAW_IRON_BLOCK);
        //COAL
        interestingBlocks.add(Blocks.COAL_BLOCK);
        interestingBlocks.add(Blocks.COAL_ORE);
        interestingBlocks.add(Blocks.DEEPSLATE_COAL_ORE);
        //COPPER
        interestingBlocks.add(Blocks.RAW_COPPER_BLOCK);
        interestingBlocks.add(Blocks.COPPER_ORE);
        interestingBlocks.add(Blocks.DEEPSLATE_COPPER_ORE);
    }

    @Override
    public void onEnable() {
        super.onEnable();
        modBeaverUtils.reloadRenderer();
    }

    @Override
    public void onDisable() {
        super.onDisable();
        modBeaverUtils.reloadRenderer();
    }

    @Override
    public void onActivation() {
        super.onActivation();
        modBeaverUtils.reloadRenderer();
    }

    @Override
    public void onDeactivation() {
        super.onDeactivation();
        modBeaverUtils.reloadRenderer();
    }
}
