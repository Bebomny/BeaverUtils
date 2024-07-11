package dev.bebomny.beaver.beaverutils.features.features;

import dev.bebomny.beaver.beaverutils.configuration.config.XRayConfig;
import dev.bebomny.beaver.beaverutils.configuration.gui.menus.XRayMenu;
import dev.bebomny.beaver.beaverutils.features.KeyOnOffFeature;
import dev.bebomny.beaver.beaverutils.helpers.BlockUtils;
import net.minecraft.block.Block;
import net.minecraft.block.Blocks;
import org.lwjgl.glfw.GLFW;

import java.util.ArrayList;
import java.util.Collection;

public class XRay extends KeyOnOffFeature {

    private final XRayConfig xRayConfig = config.xRayConfig;

    /**
     * See {@link dev.bebomny.beaver.beaverutils.mixins.BlockMixin} <br>
     * and {@link dev.bebomny.beaver.beaverutils.mixins.BlockStateMixin}
     */
    public XRay() {
        super("XRay"); //GLFW.GLFW_KEY_X

        addActivationKeybinding(GLFW.GLFW_KEY_X); //88
        setOptionsMenu(new XRayMenu());

        //load config values?
        //if (config.generalConfig.autoEnable)
        //    setEnabled(xRayConfig.enable);

        //load interesting blocks
        if (xRayConfig.interestingBlocksAsCollection.isEmpty())
            xRayConfig.interestingBlocksAsCollection = populateInterestingBlocksWithStrings();

        LOGGER.atInfo().log("XRAY PRESENT!!! Is IRON_ORE Interesting?(Should be? Yes) but is it? " + isInterestingBlock(Blocks.IRON_ORE));
    }

    public boolean isInterestingBlock(Block block) {
        return xRayConfig.interestingBlocksAsCollection.contains(BlockUtils.getBlockName(block));
    }

    @Override
    protected void onEnable() {
        super.onEnable();
        reloadRenderer();
    }

    @Override
    protected void onDisable() {
        super.onDisable();
        reloadRenderer();
    }

    private void reloadRenderer() {
        client.worldRenderer.reload();
    }

    private Collection<String> populateInterestingBlocksWithStrings() {
        Collection<String> collection = new ArrayList<>();

        for (Block block : getDefaultInterestingBlocks())
            collection.add(BlockUtils.getBlockName(block));

        return collection;
    }

    private Collection<Block> getDefaultInterestingBlocks() {
        Collection<Block> collection = new ArrayList<>();
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
        //other
        collection.add(Blocks.SPAWNER);

        return collection;
    }
}
