package dev.bebomny.beaver.beaverutils.commands.commands;

import com.mojang.brigadier.builder.ArgumentBuilder;
import dev.bebomny.beaver.beaverutils.commands.ClientCommand;
import dev.bebomny.beaver.beaverutils.helpers.BlockUtils;
import dev.bebomny.beaver.beaverutils.notifications.Categories;
import dev.bebomny.beaver.beaverutils.notifications.Notification;
import net.fabricmc.fabric.api.client.command.v2.ClientCommandManager;
import net.fabricmc.fabric.api.client.command.v2.FabricClientCommandSource;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.hud.DebugHud;
import net.minecraft.command.CommandRegistryAccess;
import net.minecraft.command.argument.BlockStateArgument;
import net.minecraft.command.argument.BlockStateArgumentType;
import net.minecraft.text.Text;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.hit.HitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;

import java.util.Objects;

public class XRayCommand extends ClientCommand {

    public XRayCommand() {
        super("xray", "Allows for adding and removing Blocks to show when xray is enabled");
    }

    @Override
    public void build(ArgumentBuilder<FabricClientCommandSource, ?> builder, CommandRegistryAccess registryAccess) {
        builder.then(ClientCommandManager.literal(getName())
                .then(ClientCommandManager.literal("add")
                        .then(ClientCommandManager.argument("block", BlockStateArgumentType.blockState(registryAccess))
                                .executes(ctx -> {
                                    Block block = ctx.getArgument("block", BlockStateArgument.class).getBlockState().getBlock();

                                    if (beaverUtilsClient.getConfig().xRayConfig.interestingBlocksAsCollection.contains(BlockUtils.getBlockName(block))) {
                                        ctx.getSource().sendFeedback(
                                                Text.of("§l§9" + BlockUtils.getBlockName(block) + " §eis already present in XRay's interesting Blocks")
                                        );
                                        return 0;
                                    }

                                    beaverUtilsClient.getConfig().xRayConfig.interestingBlocksAsCollection.add(BlockUtils.getBlockName(block));

                                    ctx.getSource().sendFeedback(
                                            Text.of("§aAdded §l§9" + BlockUtils.getBlockName(block) + " §ato XRay's interesting Blocks")
                                    );

                                    beaverUtilsClient.getNotifier().newNotification(
                                            Notification.builder("§aAdded §l§9" + BlockUtils.getBlockName(block) + " §ato XRay's interesting Blocks")
                                                    .category(Categories.COMMAND, null)
                                                    .duration(100)
                                                    .build()
                                    );

                                    return 0;
                                })
                        ).then(ClientCommandManager.literal("lookingat")
                                .executes(ctx -> {
                                    MinecraftClient client = beaverUtilsClient.client;

                                    if (client.player == null | client.world == null) return 0;

                                    HitResult hitResult = client.player.raycast(10, 0.0f, false);

                                    if (Objects.requireNonNull(hitResult.getType()) == HitResult.Type.BLOCK) {
                                        //Vec3d blockPos = hitResult.getPos();
                                        //Block block = client.world.getBlockState(new BlockPos(blockPos)).getBlock();
                                        BlockPos blockPos = ((BlockHitResult) hitResult).getBlockPos();
                                        BlockState blockState = client.world.getBlockState(blockPos);

                                        if (beaverUtilsClient.getConfig().xRayConfig.interestingBlocksAsCollection.contains(BlockUtils.getBlockName(blockState.getBlock()))) {
                                            ctx.getSource().sendFeedback(Text.of(
                                                    "§l§9" + BlockUtils.getBlockName(blockState.getBlock()) + " §eis already present in XRay's interesting Blocks" + '\n'
                                                            + "§9To remove it use: " + "§l§a/beaverutils xray remove " + BlockUtils.getBlockName(blockState.getBlock())
                                            ));
                                            return 0;
                                        }

                                        beaverUtilsClient.getConfig().xRayConfig.interestingBlocksAsCollection.add(BlockUtils.getBlockName(blockState.getBlock()));

                                        ctx.getSource().sendFeedback(
                                                Text.of("§aAdded §l§9" + BlockUtils.getBlockName(blockState.getBlock()) + " §ato XRay's interesting Blocks")
                                        );

                                        beaverUtilsClient.getNotifier().newNotification(
                                                Notification.builder("§aAdded §l§9" + BlockUtils.getBlockName(blockState.getBlock()) + " §ato XRay's interesting Blocks")
                                                        .category(Categories.COMMAND, null)
                                                        .duration(100)
                                                        .build()
                                        );
                                    } else {
                                        ctx.getSource().sendFeedback(Text.of("You're not looking at any Blocks!"));
                                    }

                                    return 0;
                                })
                        )
                ).then(ClientCommandManager.literal("remove")
                        .then(ClientCommandManager.argument("block", BlockStateArgumentType.blockState(registryAccess))
                                .executes(ctx -> {
                                    Block block = ctx.getArgument("block", BlockStateArgument.class).getBlockState().getBlock();

                                    if (!beaverUtilsClient.getConfig().xRayConfig.interestingBlocksAsCollection.contains(BlockUtils.getBlockName(block))) {
                                        ctx.getSource().sendFeedback(
                                                Text.of(
                                                        "§l§9" + BlockUtils.getBlockName(block) + " §eis not present in XRay's interesting Blocks" + '\n'
                                                                + "§9To add it use: " + "§l§a/beaverutils xray add " + BlockUtils.getBlockName(block)
                                                )
                                        );
                                        return 0;
                                    }

                                    beaverUtilsClient.getConfig().xRayConfig.interestingBlocksAsCollection.remove(BlockUtils.getBlockName(block));

                                    ctx.getSource().sendFeedback(
                                            Text.of("§cRemoved §l§9" + BlockUtils.getBlockName(block) + " §cfrom XRay's interesting Blocks")
                                    );

                                    beaverUtilsClient.getNotifier().newNotification(
                                            Notification.builder("§cRemoved §l§9" + BlockUtils.getBlockName(block) + " §cfrom XRay's interesting Blocks")
                                                    .category(Categories.COMMAND, null)
                                                    .duration(100)
                                                    .build()
                                    );

                                    return 0;
                                })
                        ).then(ClientCommandManager.literal("lookingat")
                                .executes(ctx -> {
                                    MinecraftClient client = beaverUtilsClient.client;

                                    if (client.player == null | client.world == null) return 0;

                                    HitResult hitResult = client.player.raycast(10, 0.0f, false);

                                    if (Objects.requireNonNull(hitResult.getType()) == HitResult.Type.BLOCK) {
                                        //Vec3d blockPos = hitResult.getPos();
                                        //Block block = client.world.getBlockState(new BlockPos(blockPos)).getBlock();
                                        BlockPos blockPos = ((BlockHitResult) hitResult).getBlockPos();
                                        BlockState blockState = client.world.getBlockState(blockPos);

                                        if (!beaverUtilsClient.getConfig().xRayConfig.interestingBlocksAsCollection.contains(BlockUtils.getBlockName(blockState.getBlock()))) {
                                            ctx.getSource().sendFeedback(Text.of(
                                                    "§l§9" + BlockUtils.getBlockName(blockState.getBlock()) + " §eis not present in XRay's interesting Blocks" + '\n'
                                                            + "§9To add it use: " + "§l§a/beaverutils xray add " + BlockUtils.getBlockName(blockState.getBlock())
                                            ));
                                            return 0;
                                        }

                                        beaverUtilsClient.getConfig().xRayConfig.interestingBlocksAsCollection.add(BlockUtils.getBlockName(blockState.getBlock()));

                                        ctx.getSource().sendFeedback(
                                                Text.of("§cRemoved §l§9" + BlockUtils.getBlockName(blockState.getBlock()) + " §cfrom XRay's interesting Blocks")
                                        );

                                        beaverUtilsClient.getNotifier().newNotification(
                                                Notification.builder("§cRemoved §l§9" + BlockUtils.getBlockName(blockState.getBlock()) + " §cfrom XRay's interesting Blocks")
                                                        .category(Categories.COMMAND, null)
                                                        .duration(100)
                                                        .build()
                                        );
                                    } else {
                                        ctx.getSource().sendFeedback(Text.of("You're not looking at any Blocks!"));
                                    }

                                    return 0;
                                })
                        ).then(ClientCommandManager.literal("list")
                                .executes(ctx -> {
                                    if (beaverUtilsClient.getConfig().xRayConfig.interestingBlocksAsCollection.isEmpty()) {
                                        ctx.getSource().sendFeedback(
                                                Text.of("§c§lXRay's interesting blocks List is Empty!" + '\n' + "&9To add blocks to the list use: " + "§l§a/beaverutils xray add [Block]")
                                        );
                                        return 0;
                                    }

                                    StringBuilder list = new StringBuilder();
                                    for (String s : beaverUtilsClient.getConfig().xRayConfig.interestingBlocksAsCollection) {
                                        list.append(s).append(", ");
                                    }

                                    ctx.getSource().sendFeedback(
                                            Text.of("§fXray's Interesting Blocks List: " + "§r " + '\n' + list)
                                    );
                                    return 0;
                                })
                        )
                )
        );
    }
}
