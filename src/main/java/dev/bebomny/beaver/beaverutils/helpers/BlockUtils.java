package dev.bebomny.beaver.beaverutils.helpers;

import net.minecraft.block.Block;
import net.minecraft.block.Blocks;
import net.minecraft.util.Identifier;
import net.minecraft.util.InvalidIdentifierException;
import net.minecraft.util.registry.Registry;
import net.minecraft.util.registry.RegistryKey;

public class BlockUtils {

    public static String getBlockName(Block block) {
        return Registry.BLOCK.getId(block).toString();
    }

    public static Block getBlockByName(String name) {
        try {
            return Registry.BLOCK.get(new Identifier(name));
        } catch(InvalidIdentifierException e) {
            return Blocks.AIR;
        }
    }
}
