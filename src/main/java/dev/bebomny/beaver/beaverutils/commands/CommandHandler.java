package dev.bebomny.beaver.beaverutils.commands;

import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import dev.bebomny.beaver.beaverutils.client.BeaverUtilsClient;
import dev.bebomny.beaver.beaverutils.commands.commands.EntityCountCommand;
import dev.bebomny.beaver.beaverutils.commands.commands.EntityListCommand;
import dev.bebomny.beaver.beaverutils.commands.commands.TestCommand;
import dev.bebomny.beaver.beaverutils.commands.commands.XRayCommand;
import net.fabricmc.fabric.api.client.command.v2.ClientCommandManager;
import net.fabricmc.fabric.api.client.command.v2.ClientCommandRegistrationCallback;
import net.fabricmc.fabric.api.client.command.v2.FabricClientCommandSource;

import java.util.ArrayList;
import java.util.List;

public class CommandHandler {

    private final BeaverUtilsClient beaverUtilsClient;
    private static final String PREFIX = "beaverutils";
    public static final List<ClientCommand> clientCommands = new ArrayList<>();

    public CommandHandler() {
        this.beaverUtilsClient = BeaverUtilsClient.getInstance();

        registerClientside(new XRayCommand());
        registerClientside(new TestCommand());
        registerClientside(new EntityCountCommand());
        registerClientside(new EntityListCommand());

        beaverUtilsClient.LOGGER.atInfo().log("Registered " + clientCommands.size() + " commands!");
    }

    public static void registerClientside(ClientCommand command) {
        ClientCommandRegistrationCallback.EVENT.register(
            (dispatcher, registryAccess) -> {
                LiteralArgumentBuilder<FabricClientCommandSource> builder = ClientCommandManager.literal(PREFIX);
                command.build(builder, registryAccess);
                dispatcher.register(builder);
            });
        clientCommands.add(command);
    }

}
