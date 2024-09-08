package dev.bebomny.beaver.beaverutils.commands.commands;

import com.mojang.brigadier.builder.ArgumentBuilder;
import dev.bebomny.beaver.beaverutils.client.BeaverUtilsClient;
import dev.bebomny.beaver.beaverutils.commands.ClientCommand;
import net.fabricmc.fabric.api.client.command.v2.ClientCommandManager;
import net.fabricmc.fabric.api.client.command.v2.FabricClientCommandSource;
import net.minecraft.client.MinecraftClient;
import net.minecraft.command.CommandRegistryAccess;
import net.minecraft.entity.Entity;
import net.minecraft.text.Text;
import net.minecraft.util.math.Box;

import java.util.List;

public class TestCommand extends ClientCommand {

    public TestCommand() {
        super(Text.translatable("client_command.test.name"), Text.translatable("client_command.test.description"));
    }

    @Override
    public void build(ArgumentBuilder<FabricClientCommandSource, ?> builder, CommandRegistryAccess registryAccess) {
        builder.then(ClientCommandManager.literal(getName().getString())
                .executes(context -> {
                    context.getSource().sendFeedback(Text.literal("§a Test!"));
                    MinecraftClient client = BeaverUtilsClient.getInstance().client;
                    //client.player.getServer().getWorlds().forEach(serverWorld -> context.getSource().sendFeedback(Text.literal("§a World: " + serverWorld.asString())));

//                    Box playerRenderDistanceBox = new Box(client.player.getBlockPos()).expand(64.0d);
//                    List<Entity> entitiesFoundList = client.player.getWorld().getEntitiesByClass(Entity.class, playerRenderDistanceBox, entity -> true);
//                    context.getSource().sendFeedback(Text.literal("§a Found: " + entitiesFoundList.size() + " entities!"));
//
//
//                    //Exclude the player
//                    for(int i = 0; i < Math.min(100, entitiesFoundList.size()); i++) {
//                        context.getSource().sendFeedback(Text.literal("§a Entity: " + entitiesFoundList.get(i).getName().getString()));
//                    }
                    context.getSource().sendFeedback(Text.literal("§a Test!"));

                    List<Long> latestTimeMeasurements = beaverUtilsClient.getFeatures().entityListDisplay.timeMeasurements;
                    int measurementCount = beaverUtilsClient.getFeatures().entityListDisplay.measurementCountLimit;

                    long totalTime = latestTimeMeasurements.stream().reduce(Math::addExact).orElse(1L);
                    double avgTime = (double) totalTime / measurementCount;
                    double avgTimeMs = (totalTime * 1e-6) / measurementCount;

                    context.getSource().sendFeedback(Text.literal("§a List size: " + latestTimeMeasurements.size()));
//                    context.getSource().sendFeedback(
//                            Text.literal(String.format("§b Time average %.2fns in last %d ticks. Last tick %dns",
//                                    avgTime,
//                                    measurementCount,
//                                    latestTimeMeasurements.getFirst())));
                    context.getSource().sendFeedback(
                            Text.literal(String.format("§b Time average %.2fms in last %d ticks. Last tick %.2fms",
                                    avgTimeMs,
                                    measurementCount,
                                    latestTimeMeasurements.getFirst() * 1e-6)));

                    return 0;
                })
        );
    }
}
