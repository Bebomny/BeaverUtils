package dev.bebomny.beaver.beaverutils.features.features;

import dev.bebomny.beaver.beaverutils.configuration.config.FlightConfig;
import dev.bebomny.beaver.beaverutils.configuration.gui.menus.FlightMenu;
import dev.bebomny.beaver.beaverutils.features.KeyOnOffFeature;
import dev.bebomny.beaver.beaverutils.helpers.PacketHelper;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.minecraft.block.Blocks;
import net.minecraft.client.MinecraftClient;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.math.Vec3i;
import org.lwjgl.glfw.GLFW;

public class Flight extends KeyOnOffFeature {

    private final FlightConfig flightConfig = config.flightConfig;

    private int tickCounter;
    //private final static int TICKLIMIT = 30; //in ticks, max 80 should work on most paper servers // now set in config
    private Vec3d oldPos;

    public Flight() {
        super("Flight");

        addActivationKeybinding(GLFW.GLFW_KEY_V);
        setEnableConfig(flightConfig);
        setOptionsMenu(new FlightMenu());

        if(config.generalConfig.autoEnable)
            setEnabled(enableConfig.enabled);

        this.oldPos = new Vec3d(0.0d,100.0d,0.0d);

        ClientTickEvents.START_CLIENT_TICK.register(this::onUpdate);
    }

    private void onUpdate(MinecraftClient client) {
        if(client.player == null) return;

        //TODO: FIX!

        if(isEnabled()) {
            switch (flightConfig.flightMode) {
                case Creative -> {
                    if(!client.player.getAbilities().allowFlying)
                        client.player.getAbilities().allowFlying = true;

                    if(!client.player.getAbilities().flying) return;

                    this.oldPos = client.player.getPos();

                    if(tickCounter >= flightConfig.floatingTickLimit) {
                        BlockPos blockOneDownPos = client.player.getBlockPos().subtract(new Vec3i(0, 1, 0));

                        if((client.player.getWorld().getBlockState(blockOneDownPos).isAir())
                                || (client.player.getWorld().getBlockState(blockOneDownPos).getBlock().equals(Blocks.WATER)))
                            forceFlyBypass(client);
                        tickCounter = 0;
                    }

                    tickCounter++;
                }

                case Position -> {
                    //TODO flight by manipulating positon
                }
            }
        }
    }

    public void forceFlyBypass(MinecraftClient client) {
        if (client.player == null) return;
        PacketHelper.sendPositionImmediately(client.player.getPos().subtract(0.0d, 0.0433d, 0.0d), false);
    }

    public void changeMode() {
        setMode(flightConfig.flightMode == Mode.Creative ? Mode.Position : Mode.Creative);
    }

    public void setMode(Mode newMode) {
        flightConfig.flightMode = newMode;
    }

    public Mode getMode() {
        return flightConfig.flightMode;
    }

    @Override
    protected void onEnable() {
        super.onEnable();
        client.player.getAbilities().allowFlying = true;
        oldPos = client.player.getPos();
    }

    @Override
    protected void onDisable() {
        super.onDisable();
        client.player.getAbilities().allowFlying = false;
        client.player.getAbilities().flying = false;
    }

    public enum Mode {
        Creative,
        Position
    }
}
