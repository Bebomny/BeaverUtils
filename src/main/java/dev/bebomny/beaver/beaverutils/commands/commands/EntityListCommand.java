package dev.bebomny.beaver.beaverutils.commands.commands;

import com.mojang.brigadier.arguments.IntegerArgumentType;
import com.mojang.brigadier.builder.ArgumentBuilder;
import dev.bebomny.beaver.beaverutils.commands.ClientCommand;
import net.fabricmc.fabric.api.client.command.v2.ClientCommandManager;
import net.fabricmc.fabric.api.client.command.v2.FabricClientCommandSource;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.command.CommandRegistryAccess;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.text.MutableText;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;
import net.minecraft.util.math.Box;

import java.util.*;
import java.util.stream.Collectors;

public class EntityListCommand extends ClientCommand {

    public EntityListCommand() {
        super("entityList", "Lists all entities in a specified radius");
    }

    @Override
    public void build(ArgumentBuilder<FabricClientCommandSource, ?> builder, CommandRegistryAccess registryAccess) {
        builder.then(ClientCommandManager.literal("list_entities")
                .then(ClientCommandManager.argument("radius", IntegerArgumentType.integer())
                        .executes(context -> {
                            ClientPlayerEntity playerEntity = beaverUtilsClient.client.player;

                            int radius = context.getArgument("radius", Integer.class);
                            Box playerBox = new Box(playerEntity.getBlockPos()).expand(radius);

                            List<Entity> entitiesFoundList = playerEntity.getWorld().getEntitiesByClass(Entity.class, playerBox, entity -> true);
                            /*Map<Entity, Long> entitiesFoundMap = entitiesFoundList.stream()
                                    .collect(Collectors.groupingBy(entity -> entity, Collectors.counting()))
                                    .entrySet()
                                    .stream()
                                    .sorted(Map.Entry.<Entity, Long>comparingByValue(Comparator.reverseOrder()))
                                    .collect(Collectors.toMap(
                                            Map.Entry::getKey,
                                            Map.Entry::getValue,
                                            (e1, e2) -> e1,
                                            LinkedHashMap::new
                                    ));*/

                            //entitiesFoundList.getFirst().getType()

                            Map<EntityType<?>, Integer> entitiesFoundMap = new LinkedHashMap<>();
                            for (Entity entity : entitiesFoundList) {
                                if (!entitiesFoundMap.containsKey(entity.getType()))
                                    entitiesFoundMap.put(entity.getType(), 1);
                                else
                                    entitiesFoundMap.put(entity.getType(), entitiesFoundMap.get(entity.getType()) + 1);
                            }


                            //Found 50 types, in total 160 entities!
                            MutableText totalText = Text
                                    .literal("Found ")
                                    .formatted(Formatting.GRAY)
                                    .append(Text
                                            .literal(String.valueOf(entitiesFoundMap.size()))
                                            .formatted(Formatting.GREEN))
                                    .append(Text
                                            .literal(" types, in total ")
                                            .formatted(Formatting.GRAY))
                                    .append(Text
                                            .literal(String.valueOf(entitiesFoundList.size()))
                                            .formatted(Formatting.GREEN))
                                    .append(Text
                                            .literal(" entities in a ")
                                            .formatted(Formatting.GRAY))
                                    .append(Text
                                            .literal(String.valueOf(radius))
                                            .formatted(Formatting.BLUE))
                                    .append(Text
                                            .literal(" block radius!")
                                            .formatted(Formatting.GRAY));

                            context.getSource().sendFeedback(totalText);

                            entitiesFoundMap.forEach((entityType, count) -> {
                                MutableText singularEntityText = Text
                                        .literal("Entity: ")
                                        .formatted(Formatting.GRAY)
                                        .append(Text
                                                .literal(entityType.getName().getString())
                                                .formatted(Formatting.BLUE))
                                        .append(Text
                                                .literal(" | Count: ")
                                                .formatted(Formatting.GRAY))
                                        .append(Text
                                                .literal(String.valueOf(count))
                                                .formatted(Formatting.GREEN));
                                context.getSource().sendFeedback(singularEntityText);
                            });

                            return 0;
                        })
                )
        );
    }
}
