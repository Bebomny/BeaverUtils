package dev.bebomny.beaver.beaverutils.commands.commands;

import com.mojang.brigadier.arguments.IntegerArgumentType;
import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.builder.ArgumentBuilder;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.mojang.brigadier.suggestion.SuggestionProvider;
import com.mojang.brigadier.suggestion.Suggestions;
import com.mojang.brigadier.suggestion.SuggestionsBuilder;
import dev.bebomny.beaver.beaverutils.client.BeaverUtilsClient;
import dev.bebomny.beaver.beaverutils.commands.ClientCommand;
import net.fabricmc.fabric.api.client.command.v2.ClientCommandManager;
import net.fabricmc.fabric.api.client.command.v2.FabricClientCommandSource;
import net.minecraft.client.network.ClientCommandSource;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.command.CommandRegistryAccess;
import net.minecraft.command.suggestion.SuggestionProviders;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.text.MutableText;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;
import net.minecraft.util.math.Box;

import java.util.Collection;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;

public class EntityCountCommand extends ClientCommand {

    public EntityCountCommand() {
        super("entityCount", "Counts the chosen entity in a certain radius");
    }

    @Override
    public void build(ArgumentBuilder<FabricClientCommandSource, ?> builder, CommandRegistryAccess registryAccess) {
        builder.then(ClientCommandManager.literal("count_entities")
                        .then(ClientCommandManager.argument("entity", StringArgumentType.string())
                                .suggests(new EntityTypeSuggestionProvider())
                                .then(ClientCommandManager.argument("radius", IntegerArgumentType.integer())
                                        .executes(context -> {
                                            ClientPlayerEntity playerEntity = beaverUtilsClient.client.player;

                                            //When executing this command the player should never be null
                                            // cause the player executes it
                                            assert playerEntity != null;
                                            int radius = context.getArgument("radius", Integer.class);
                                            Box playerBox = new Box(playerEntity.getBlockPos()).expand(radius);

                                            //StringArgumentType is used here, instead of EntityArgumentType,
                                            // because EntityArgumentType requires this command to be server side to
                                            // access entities, so this works as a workaround
                                            String chosenEntityString = context.getArgument("entity", String.class).toLowerCase();
                                            Optional<EntityType<?>> optionalChosenEntityType = EntityType.get(chosenEntityString);

                                            if (optionalChosenEntityType.isEmpty()) {
                                                MutableText entityNotFoundMessage = Text
                                                        .literal("Entity with name ")
                                                        .formatted(Formatting.RED)
                                                        .append(Text
                                                                .literal(chosenEntityString)
                                                                .formatted(Formatting.RED))
                                                        .append(Text
                                                                .literal(" not found!")
                                                                .formatted(Formatting.RED));
                                                context.getSource().sendFeedback(entityNotFoundMessage);
                                                return 0;
                                            }

                                            EntityType<?> chosenEntityType = optionalChosenEntityType.get();
                                            //check pie chart

                                            List<Entity> entitiesFoundList = playerEntity.getWorld()
                                                    .getEntitiesByClass(Entity.class, playerBox, entity -> entity.getType().equals(chosenEntityType));
//                                    long entitiesFoundCount = entitiesFoundList.stream()
//                                            .filter(entity ->
//                                                    entity.getName()
//                                                            .getString()
//                                                            .equalsIgnoreCase(chosenEntityString)
//                                            ).count();


//                                    entitiesFoundList.forEach(entity -> {
//                                        context.getSource().sendFeedback(Text.literal(entity.getType().getName().getString()));
//                                    });
                                            //TODO: Add as translatable?
                                            // for example for an empty found list: "§7Found §4%d §7entities matching §9%s §7in a §9%d §7 block radius around the player!"
                                            // example with found entities: "§7Found §a%d §7entities matching §9%s §7in a §9%d §7 block radius around the player!"
                                            MutableText countMessage = Text
                                                    .literal("Found ")
                                                    .formatted(Formatting.GRAY)
                                                    .append(Text
                                                            .literal(String.valueOf(entitiesFoundList.size()))
                                                            .formatted(entitiesFoundList.isEmpty() ? Formatting.RED : Formatting.GREEN))
                                                    .append(Text
                                                            .literal(" entities matching ")
                                                            .formatted(Formatting.GRAY))
                                                    .append(Text
                                                            .literal(chosenEntityString)
                                                            .formatted(Formatting.BLUE))
                                                    .append(Text
                                                            .literal(" in a ")
                                                            .formatted(Formatting.GRAY))
                                                    .append(Text
                                                            .literal(String.valueOf(radius))
                                                            .formatted(Formatting.BLUE))
                                                    .append(Text
                                                            .literal(" block radius around the player!")
                                                            .formatted(Formatting.GRAY));

                                            context.getSource().sendFeedback(countMessage);
                                            return 0;
                                        })))
        );
    }

    public static class EntityTypeSuggestionProvider implements SuggestionProvider<FabricClientCommandSource> {

        @Override
        public CompletableFuture<Suggestions> getSuggestions(CommandContext<FabricClientCommandSource> context, SuggestionsBuilder builder) throws CommandSyntaxException {
            ClientPlayerEntity playerEntity = BeaverUtilsClient.getInstance().client.player;
            List<String> entitySuggestions = playerEntity
                    .getWorld()
                    .getEntitiesByClass(
                            Entity.class,
                            new Box(playerEntity.getBlockPos()).expand(128),
                            entity -> true)
                    .stream()
                    .distinct()
                    .map(entity -> entity
                            .getType()
                            .getName()
                            .getString())
                    .toList();

            entitySuggestions.forEach(builder::suggest);

            return builder.buildFuture();
        }
    }
}
