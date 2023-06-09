package dev.bebomny.beaver.beaverutils.features;

import com.google.gson.annotations.Expose;
import net.minecraft.block.Block;
import net.minecraft.block.Blocks;
import org.lwjgl.glfw.GLFW;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class XRay extends Feature{

    //@Expose
    private final List<Block> interestingBlocks = new ArrayList<>();
    @Expose
    private Collection<Block> interestingBlocksAsCollection = new ArrayList<>();

    public XRay() {
        super("XRay", GLFW.GLFW_KEY_X); //GLFW.GLFW_KEY_X
        if (interestingBlocksAsCollection.isEmpty())
            initializeInterestingBlocks(interestingBlocksAsCollection);
    }

    public boolean isInterestingBlock(Block block) {
        return interestingBlocksAsCollection.contains(block);
    }

    public void printsmh() {
        logger.atInfo().log("[XRay] I am hereeeeeeeeeeeeeeeeeeeee!!!!!!!!!!11!11!!1!11111!!1!1!!!!!111111");
    }

    private void initializeInterestingBlocks(Collection<Block> collection) {
        //chests
        collection.add(Blocks.CHEST);
        collection.add(Blocks.ENDER_CHEST);
        collection.add(Blocks.BARREL);
        //furnaces
        collection.add(Blocks.FURNACE);
        collection.add(Blocks.BLAST_FURNACE);
        collection.add(Blocks.SMOKER);
        //Other
        collection.add(Blocks.GRINDSTONE);
        collection.add(Blocks.ENCHANTING_TABLE);
        collection.add(Blocks.LOOM);
        collection.add(Blocks.SMITHING_TABLE);
        collection.add(Blocks.BEACON);
        collection.add(Blocks.DRAGON_EGG);
        collection.add(Blocks.END_PORTAL_FRAME);
        collection.add(Blocks.END_PORTAL);
        //Redstone thingies
        collection.add(Blocks.HOPPER);
        collection.add(Blocks.COMPARATOR);
        collection.add(Blocks.REPEATER);
        collection.add(Blocks.PISTON);
        collection.add(Blocks.STICKY_PISTON);
        collection.add(Blocks.DISPENSER);
        collection.add(Blocks.DROPPER);
        collection.add(Blocks.JUKEBOX);
        collection.add(Blocks.NOTE_BLOCK);
        collection.add(Blocks.TNT);
        //netherite
        collection.add(Blocks.ANCIENT_DEBRIS);
        collection.add(Blocks.NETHERITE_BLOCK);
        //quartz
        collection.add(Blocks.NETHER_QUARTZ_ORE);
        //emeralds
        collection.add(Blocks.EMERALD_BLOCK);
        collection.add(Blocks.EMERALD_ORE);
        collection.add(Blocks.DEEPSLATE_EMERALD_ORE);
        //diamonds
        collection.add(Blocks.DIAMOND_BLOCK);
        collection.add(Blocks.DIAMOND_ORE);
        collection.add(Blocks.DEEPSLATE_DIAMOND_ORE);
        //lapis
        collection.add(Blocks.LAPIS_ORE);
        collection.add(Blocks.LAPIS_BLOCK);
        collection.add(Blocks.DEEPSLATE_LAPIS_ORE);
        //REDSTONE
        collection.add(Blocks.REDSTONE_ORE);
        collection.add(Blocks.REDSTONE_BLOCK);
        collection.add(Blocks.DEEPSLATE_REDSTONE_ORE);
        //GOLD
        collection.add(Blocks.GOLD_BLOCK);
        collection.add(Blocks.GOLD_ORE);
        collection.add(Blocks.RAW_GOLD_BLOCK);
        collection.add(Blocks.NETHER_GOLD_ORE);
        collection.add(Blocks.DEEPSLATE_GOLD_ORE);
        //IRON
        collection.add(Blocks.IRON_ORE);
        collection.add(Blocks.IRON_BLOCK);
        collection.add(Blocks.DEEPSLATE_IRON_ORE);
        collection.add(Blocks.RAW_IRON_BLOCK);
        //COAL
        collection.add(Blocks.COAL_BLOCK);
        collection.add(Blocks.COAL_ORE);
        collection.add(Blocks.DEEPSLATE_COAL_ORE);
        //COPPER
        collection.add(Blocks.RAW_COPPER_BLOCK);
        collection.add(Blocks.COPPER_ORE);
        collection.add(Blocks.DEEPSLATE_COPPER_ORE);
    }
}
