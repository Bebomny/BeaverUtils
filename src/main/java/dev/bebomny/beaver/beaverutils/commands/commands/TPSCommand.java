package dev.bebomny.beaver.beaverutils.commands.commands;

import com.mojang.brigadier.builder.ArgumentBuilder;
import dev.bebomny.beaver.beaverutils.client.BeaverUtilsClient;
import dev.bebomny.beaver.beaverutils.commands.ClientCommand;
import dev.bebomny.beaver.beaverutils.features.features.InGameStats;
import net.fabricmc.fabric.api.client.command.v2.ClientCommandManager;
import net.fabricmc.fabric.api.client.command.v2.FabricClientCommandSource;
import net.minecraft.command.CommandRegistryAccess;
import net.minecraft.text.MutableText;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;

public class TPSCommand extends ClientCommand {

    public TPSCommand() {
        super(Text.translatable("client_command.tps.name"), Text.translatable("client_command.tps.description"));
    }

    @Override
    public void build(ArgumentBuilder<FabricClientCommandSource, ?> builder, CommandRegistryAccess registryAccess) {
        builder.then(ClientCommandManager.literal("tps")
                .executes(context -> {
                    InGameStats inGameStats = BeaverUtilsClient.getInstance().getFeatures().inGameStats;

                    MutableText response = Text.translatable("client_command.tps.prefix")
                            .append("\n")
                            .append(inGameStats.isDedicatedServer() ? Text.translatable("client_command.tps.dedicated_server_1") : Text.translatable("client_command.tps.integrated_server_1"))
                            .append("\n")
                            .append(inGameStats.isDedicatedServer() ? Text.translatable("client_command.tps.dedicated_server_2") : Text.translatable("client_command.tps.integrated_server_2"))
                            .append("\n")
                            .append("\n")
                            .append(Text.translatable("client_command.tps.tps_message"))
                            .append("\n")
                            .append(Text.translatable("client_command.tps.time_intervals"))
                            .append("\n")
                            //.append(Text.translatable("client_command.tps.formatted_values", ))
                            .append(Text.literal("TPS not implemented, yet!"))
                            .append("\n")
                            .append("\n")
                            .append(Text.translatable("client_command.tps.mspt_message"))
                            .append("\n")
                            .append(Text.translatable("client_command.tps.time_intervals"))
                            .append("\n")
                            .append(Text.translatable("client_command.tps.formatted_values",
                                    formatMSPT(inGameStats.getRollingMsPerTick10sec().average()),
                                    formatMSPT(inGameStats.getRollingMsPerTick60sec().average()),
                                    formatMSPT(inGameStats.getRollingMsPerTick5min().average()),
                                    formatMSPT(inGameStats.getRollingMsPerTick15min().average()),
                                    formatMSPT(inGameStats.getMsPerLastTick())))
                            .append("\n");

                    context.getSource().sendFeedback(response);

                    return 0;
                }));
    }

    private Text formatTPS(double value) {
        return Text.empty();
    }

    private Text formatMSPT(double value) {
        Formatting msPerTickFormatting;
        if (value <= 58.8f)
            msPerTickFormatting = Formatting.GREEN;
        else if (value <= 83.3f)
            msPerTickFormatting = Formatting.YELLOW;
        else
            msPerTickFormatting = Formatting.RED;

        return Text.literal(String.format("%.2f", value)).formatted(msPerTickFormatting);
    }
}
