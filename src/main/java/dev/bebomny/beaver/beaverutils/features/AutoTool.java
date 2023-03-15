package dev.bebomny.beaver.beaverutils.features;

import dev.bebomny.beaver.beaverutils.client.BeaverUtilsClient;
import dev.bebomny.beaver.beaverutils.helpers.Notification;
import net.minecraft.block.BlockState;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.enchantment.Enchantments;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.text.Text;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import java.awt.*;

public class AutoTool extends Feature{
    private final MinecraftClient client;
    private final BeaverUtilsClient modBeaverUtils;

    public AutoTool(MinecraftClient client, BeaverUtilsClient mod) {
        super("AutoTool", mod);
        this.client = client;
        this.modBeaverUtils = mod;
    }

    public void onBlockBreakingEvent(BlockPos pos, Direction direction) {
        if(!isEnabled())
            return;

        equipBestTool(pos);

    }

    private void equipBestTool(BlockPos pos) {
        ClientPlayerEntity player = client.player;
        if(player.getAbilities().creativeMode)
            return;

        int bestSlot = getBestSlot(pos);
        if(bestSlot == -1)
            return;

        player.getInventory().selectedSlot = bestSlot;
    }

    private int getBestSlot(BlockPos pos) {
        ClientPlayerEntity player = client.player;
        PlayerInventory playerInventory = player.getInventory();
        ItemStack heldItem = client.player.getMainHandStack();

        BlockState state = client.world.getBlockState(pos);
        float bestSpeed = getMiningSpeed(heldItem, state);
        int bestSlot = -1;

        for(int slot = 0; slot < 9; slot++) {
            if(slot == playerInventory.selectedSlot)
                continue;

            ItemStack stack = playerInventory.getStack(slot);

            float speed = getMiningSpeed(stack, state);
            if(speed <= bestSpeed)
                continue;

            bestSpeed = speed;
            bestSlot = slot;
        }

        return bestSlot;
    }

    private float getMiningSpeed(ItemStack stack, BlockState state) {
        float speed = stack.getMiningSpeedMultiplier(state);

        if(speed > 1) {
            int efficiency = EnchantmentHelper.getLevel(Enchantments.EFFICIENCY, stack);

            if(efficiency > 0 && !stack.isEmpty())
                speed += efficiency * efficiency + 1;

        }

        return speed;
    }
}
