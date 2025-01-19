package dev.bebomny.beaver.beaverutils.commands.commands;

import com.mojang.brigadier.builder.ArgumentBuilder;
import dev.bebomny.beaver.beaverutils.commands.ClientCommand;
import dev.bebomny.beaver.beaverutils.helpers.BlockUtils;
import dev.bebomny.beaver.beaverutils.notifications.Notification;
import net.fabricmc.fabric.api.client.command.v2.ClientCommandManager;
import net.fabricmc.fabric.api.client.command.v2.FabricClientCommandSource;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.client.MinecraftClient;
import net.minecraft.command.CommandRegistryAccess;
import net.minecraft.command.argument.BlockStateArgument;
import net.minecraft.command.argument.BlockStateArgumentType;
import net.minecraft.text.Text;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.hit.HitResult;
import net.minecraft.util.math.BlockPos;

import java.util.Objects;

public class XRayCommand extends ClientCommand {

    public XRayCommand() {
        super(Text.translatable("client_command.xray.name"), Text.translatable("client_command.xray.description"));
    }

    @Override
    public void build(ArgumentBuilder<FabricClientCommandSource, ?> builder, CommandRegistryAccess registryAccess) {
        builder.then(ClientCommandManager.literal(getName().getString())
                .then(ClientCommandManager.literal(Text.translatable("client_command.xray.add.literal").getString())
                        .then(ClientCommandManager.argument("block", BlockStateArgumentType.blockState(registryAccess))
                                .executes(ctx -> {
                                    Block block = ctx.getArgument("block", BlockStateArgument.class).getBlockState().getBlock();

                                    if (beaverUtilsClient.getConfig().xRayConfig.interestingBlocksAsCollection.contains(BlockUtils.getBlockName(block))) {
                                        ctx.getSource().sendFeedback(
                                                Text.translatable("client_command.xray.already_present_text_1", BlockUtils.getBlockName(block))
                                        );
                                        ctx.getSource().sendFeedback(
                                                Text.translatable("client_command.xray.already_present_text_2", BlockUtils.getBlockName(block))
                                        );
                                        return 0;
                                    }

                                    beaverUtilsClient.getConfig().xRayConfig.interestingBlocksAsCollection.add(BlockUtils.getBlockName(block));

                                    ctx.getSource().sendFeedback(
                                            Text.translatable("client_command.xray.add_success_text", BlockUtils.getBlockName(block))
                                    );

                                    beaverUtilsClient.getNotifier().newNotification(
                                            Notification.builder(Text.translatable("client_command.xray.add_success_text", BlockUtils.getBlockName(block)))
                                                    .parent(Text.translatable("client_command.text"))
                                                    .duration(100)
                                                    .build()
                                    );

                                    return 0;
                                })
                        ).then(ClientCommandManager.literal(Text.translatable("client_command.xray.looking_at.literal").getString())
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
                                            ctx.getSource().sendFeedback(
                                                    Text.translatable("client_command.xray.already_present_text_1", BlockUtils.getBlockName(blockState.getBlock()))
                                            );
                                            ctx.getSource().sendFeedback(
                                                    Text.translatable("client_command.xray.already_present_text_2", BlockUtils.getBlockName(blockState.getBlock()))
                                            );
                                            return 0;
                                        }

                                        beaverUtilsClient.getConfig().xRayConfig.interestingBlocksAsCollection.add(BlockUtils.getBlockName(blockState.getBlock()));

                                        ctx.getSource().sendFeedback(
                                                Text.translatable("client_command.xray.add_success_text", BlockUtils.getBlockName(blockState.getBlock()))
                                        );

                                        beaverUtilsClient.getNotifier().newNotification(
                                                Notification.builder(Text.translatable("client_command.xray.add_success_text", BlockUtils.getBlockName(blockState.getBlock())))
                                                        .parent(Text.translatable("client_command.text"))
                                                        .duration(100)
                                                        .build()
                                        );
                                    } else {
                                        ctx.getSource().sendFeedback(Text.translatable("client_command.xray.looking_at.fail_text"));
                                    }

                                    return 0;
                                })
                        )
                ).then(ClientCommandManager.literal(Text.translatable("client_command.xray.remove.literal").getString())
                        .then(ClientCommandManager.argument("block", BlockStateArgumentType.blockState(registryAccess))
                                .executes(ctx -> {
                                    Block block = ctx.getArgument("block", BlockStateArgument.class).getBlockState().getBlock();

                                    if (!beaverUtilsClient.getConfig().xRayConfig.interestingBlocksAsCollection.contains(BlockUtils.getBlockName(block))) {
                                        ctx.getSource().sendFeedback(
                                                Text.translatable("client_command.xray.not_present_text_1", BlockUtils.getBlockName(block))
                                        );
                                        ctx.getSource().sendFeedback(
                                                Text.translatable("client_command.xray.not_present_text_2", BlockUtils.getBlockName(block))
                                        );
                                        return 0;
                                    }

                                    beaverUtilsClient.getFeatures().xRay.xRayConfig.interestingBlocksAsCollection.remove(BlockUtils.getBlockName(block));

                                    ctx.getSource().sendFeedback(
                                            Text.translatable("client_command.xray.remove_success_text", BlockUtils.getBlockName(block))
                                    );

                                    beaverUtilsClient.getNotifier().newNotification(
                                            Notification.builder(Text.translatable("client_command.xray.remove_success_text", BlockUtils.getBlockName(block)))
                                                    .parent(Text.translatable("client_command.text"))
                                                    .duration(100)
                                                    .build()
                                    );

                                    return 0;
                                })
                        ).then(ClientCommandManager.literal(Text.translatable("client_command.xray.looking_at.literal").getString())
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
                                            ctx.getSource().sendFeedback(
                                                    Text.translatable("client_command.xray.not_present_text_1", BlockUtils.getBlockName(blockState.getBlock()))
                                            );
                                            ctx.getSource().sendFeedback(
                                                    Text.translatable("client_command.xray.not_present_text_2", BlockUtils.getBlockName(blockState.getBlock()))
                                            );
                                            return 0;
                                        }

                                        beaverUtilsClient.getConfig().xRayConfig.interestingBlocksAsCollection.remove(BlockUtils.getBlockName(blockState.getBlock()));

                                        ctx.getSource().sendFeedback(
                                                Text.translatable("client_command.xray.remove_success_text", BlockUtils.getBlockName(blockState.getBlock()))
                                        );

                                        beaverUtilsClient.getNotifier().newNotification(
                                                Notification.builder(Text.translatable("client_command.xray.remove_success_text", BlockUtils.getBlockName(blockState.getBlock())))
                                                        .parent(Text.translatable("client_command.text"))
                                                        .duration(100)
                                                        .build()
                                        );
                                    } else {
                                        ctx.getSource().sendFeedback(Text.translatable("client_command.xray.looking_at.fail_text"));
                                    }

                                    return 0;
                                })
                        )
                ).then(ClientCommandManager.literal(Text.translatable("client_command.xray.list.literal").getString())
                        .executes(ctx -> {
                            if (beaverUtilsClient.getConfig().xRayConfig.interestingBlocksAsCollection.isEmpty()) {
                                ctx.getSource().sendFeedback(
                                        Text.translatable("client_command.xray.list.empty_text_1")
                                );
                                ctx.getSource().sendFeedback(
                                        Text.translatable("client_command.xray.list.empty_text_2")
                                );
                                return 0;
                            }

                            StringBuilder list = new StringBuilder();
                            for (String s : beaverUtilsClient.getConfig().xRayConfig.interestingBlocksAsCollection) {
                                list.append(s).append(", ");
                            }

                            ctx.getSource().sendFeedback(
                                    Text.translatable("client_command.xray.list.list_text_1")
                            );
                            ctx.getSource().sendFeedback(
                                    Text.stringifiedTranslatable("client_command.xray.list.list_text_2", list)
                            );
                            return 0;
                        })
                )
        );
    }
}
