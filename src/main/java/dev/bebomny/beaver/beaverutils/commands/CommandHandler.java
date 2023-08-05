package dev.bebomny.beaver.beaverutils.commands;

import com.mojang.brigadier.Command;
import com.mojang.brigadier.builder.ArgumentBuilder;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import dev.bebomny.beaver.beaverutils.client.BeaverUtilsClient;
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

        beaverUtilsClient.LOGGER.info("Registered " + clientCommands.size() + " commands!");
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
