package dev.bebomny.beaver.beaverutils.commands;

import com.mojang.brigadier.builder.ArgumentBuilder;
import dev.bebomny.beaver.beaverutils.client.BeaverUtilsClient;
import net.fabricmc.fabric.api.client.command.v2.FabricClientCommandSource;
import net.minecraft.command.CommandRegistryAccess;
import org.slf4j.Logger;

public abstract class ClientCommand {

    protected final BeaverUtilsClient beaverUtilsClient;
    protected final Logger LOGGER;
    private final String name;
    private final String description;

    public ClientCommand(String name, String description) {
        this.name = name;
        this.description = description;
        this.beaverUtilsClient = BeaverUtilsClient.getInstance();
        this.LOGGER = beaverUtilsClient.getLogger("Command");
    }

    public abstract void build(ArgumentBuilder<FabricClientCommandSource, ?> builder, CommandRegistryAccess registryAccess);

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }
}
