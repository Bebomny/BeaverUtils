package dev.bebomny.beaver.beaverutils.commands.commands;

import com.mojang.brigadier.builder.ArgumentBuilder;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.serialization.Codec;
import dev.bebomny.beaver.beaverutils.commands.ClientCommand;
import dev.bebomny.beaver.beaverutils.features.features.CustomRenderer;
import net.fabricmc.fabric.api.client.command.v2.ClientCommandManager;
import net.fabricmc.fabric.api.client.command.v2.FabricClientCommandSource;
import net.minecraft.command.CommandRegistryAccess;
import net.minecraft.command.argument.BlockPosArgumentType;
import net.minecraft.command.argument.EnumArgumentType;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.text.Text;
import net.minecraft.util.BlockMirror;
import net.minecraft.util.StringIdentifiable;
import net.minecraft.util.math.Vec3d;

import java.util.function.Supplier;

public class CustomRendererCommand extends ClientCommand {
    public CustomRendererCommand() {
        super(Text.of("customrenderer"), Text.of("Custom renderer helper commands"));
    }

    @Override
    public void build(ArgumentBuilder<FabricClientCommandSource, ?> builder, CommandRegistryAccess registryAccess) {
        CustomRenderer customRenderer = beaverUtilsClient.getFeatures().customRenderer;

        builder.then(ClientCommandManager.literal("customrenderer")
                .then(ClientCommandManager.literal("setorigin")
                        .then(ClientCommandManager.literal("playerposition")
                                .executes(context -> {
                                    //TODO: check where exactly is this gonna be placed, in the center of a block or directly where the player was standing?
                                    //Vec3d playerPos = context.getSource().getPosition(); // not exactly players position but where the command was executed
                                    Vec3d playerPos = beaverUtilsClient.client.player.getBlockPos().toCenterPos();
                                    playerPos = new Vec3d(playerPos.x, Math.floor(playerPos.y), playerPos.z);

                                    customRenderer.setOriginPosition(playerPos);
                                    beaverUtilsClient.configHandler.saveConfig();
                                    context.getSource().sendFeedback(Text.of("§aCustom renderer origin position set at: §9x: %.1f, y: %.1f, z: %.1f".formatted(playerPos.getX(), playerPos.getY(), playerPos.getZ())));
                                    return 0;
                                }))
                        .then(ClientCommandManager.argument("blockPos", BlockPosArgumentType.blockPos())
                                .executes(context -> {
                                    context.getSource().sendFeedback(Text.of("§4Command not implemented yet"));
                                    return 0;
                                })))
                .then(ClientCommandManager.literal("highlight")
                        .executes(context -> {
                            //TODO: highlight a position?
                            context.getSource().sendFeedback(Text.of("§4Command not implemented yet"));
                            return 0;
                        }))
                .then(ClientCommandManager.literal("step")
                        .executes(context -> {
                            context.getSource().sendFeedback(Text.of("§4Command not implemented yet"));
                            return 0;
                        }))
                .then(ClientCommandManager.literal("reset")
                        .executes(context -> {
                            context.getSource().sendFeedback(Text.of("§4Command not implemented yet"));
                            return 0;
                        }))
                .then(ClientCommandManager.literal("shape")
                        .then(ClientCommandManager.argument("shape", RenderShapeArgumentType.renderShape())
                                .executes(context -> {
                                    CustomRenderer.RenderShape newShape = context.getArgument("shape", CustomRenderer.RenderShape.class);

                                    customRenderer.setRenderShape(newShape);
                                    beaverUtilsClient.configHandler.saveConfig();

                                    context.getSource().sendFeedback(Text.of("§aCustom Renderer changed shape to §9%s".formatted(newShape.name().toLowerCase())));
                                    return 0;
                                })))
        );
    }

    public static class RenderShapeArgumentType extends EnumArgumentType<CustomRenderer.RenderShape> {

        public static EnumArgumentType<CustomRenderer.RenderShape> renderShape() {
            return new RenderShapeArgumentType();
        }

        protected RenderShapeArgumentType() {
            super(StringIdentifiable.createCodec(CustomRenderer.RenderShape::values), CustomRenderer.RenderShape::values);
        }

        public static CustomRenderer.RenderShape getRenderShape(CommandContext<ServerCommandSource> context, String id) {
            return (CustomRenderer.RenderShape)context.getArgument(id, CustomRenderer.RenderShape.class);
        }
    }
}
