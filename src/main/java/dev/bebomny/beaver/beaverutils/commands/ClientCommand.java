package dev.bebomny.beaver.beaverutils.commands;

import com.mojang.brigadier.builder.ArgumentBuilder;
import dev.bebomny.beaver.beaverutils.client.BeaverUtilsClient;
import net.fabricmc.fabric.api.client.command.v2.FabricClientCommandSource;
import net.minecraft.command.CommandRegistryAccess;
import net.minecraft.text.Text;
import org.slf4j.Logger;

public abstract class ClientCommand {

    protected final BeaverUtilsClient beaverUtilsClient;
    protected final Logger LOGGER;
    private final Text name;
    private final Text description;

    public ClientCommand(Text name, Text description) {
        this.name = name;
        this.description = description;
        this.beaverUtilsClient = BeaverUtilsClient.getInstance();
        this.LOGGER = BeaverUtilsClient.getLogger("Command");
    }

    public abstract void build(ArgumentBuilder<FabricClientCommandSource, ?> builder, CommandRegistryAccess registryAccess);

    public Text getName() {
        return name;
    }

    public Text getDescription() {
        return description;
    }
}
