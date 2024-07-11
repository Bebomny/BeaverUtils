package dev.bebomny.beaver.beaverutils.helpers;

import net.minecraft.block.Block;
import net.minecraft.block.Blocks;
import net.minecraft.registry.Registries;
import net.minecraft.util.Identifier;
import net.minecraft.util.InvalidIdentifierException;

public class BlockUtils {

    public static String getBlockName(Block block) {
        return Registries.BLOCK.getId(block).toString();
    }

    public static Block getBlockByName(String name) {
        try {
            return Registries.BLOCK.get(Identifier.of(name));
        } catch(InvalidIdentifierException e) {
            return Blocks.AIR;
        }
    }
}
