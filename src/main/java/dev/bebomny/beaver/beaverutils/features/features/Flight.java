package dev.bebomny.beaver.beaverutils.features.features;

import dev.bebomny.beaver.beaverutils.configuration.config.FlightConfig;
import dev.bebomny.beaver.beaverutils.configuration.gui.menus.FlightMenu;
import dev.bebomny.beaver.beaverutils.features.KeyOnOffFeature;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.minecraft.block.Blocks;
import net.minecraft.client.MinecraftClient;
import net.minecraft.entity.player.PlayerAbilities;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.math.Vec3i;
import org.lwjgl.glfw.GLFW;

public class Flight extends KeyOnOffFeature {

    private final FlightConfig flightConfig = config.flightConfig;

    private int tickCounter;
    //private final static int TICKLIMIT = 30; //in ticks, max 80 should work on most paper servers // now set in config
    private boolean scheduledEnableDisableCheck;
    private Vec3d oldPos;

    public Flight() {
        super("Flight");

        addActivationKeybinding(GLFW.GLFW_KEY_V);
        setEnableConfig(flightConfig);
        setOptionsMenu(new FlightMenu());

        this.oldPos = new Vec3d(0.0d, 100.0d, 0.0d);

        ClientTickEvents.START_CLIENT_TICK.register(this::onUpdate);
    }

    private void onUpdate(MinecraftClient client) {
        if (client.player == null) return;

        PlayerAbilities abilities = client.player.getAbilities();

        if (this.scheduledEnableDisableCheck) {
            if (isEnabled()) {
                abilities.allowFlying = true;
                oldPos = client.player.getPos();
            } else {
                abilities.allowFlying = false;
                abilities.flying = false;
            }
            this.scheduledEnableDisableCheck = false;
        }

        //TODO: FIX! somethign is not right

        if (isEnabled()) {

            abilities.setFlySpeed(getFlightSpeed());

            switch (flightConfig.flightMode) {
                case Creative -> {

                    //Something is wrong here //maybe fixed? needs checking

                    if (!abilities.allowFlying)
                        abilities.allowFlying = true;

                    if (!abilities.flying) return;

                    if (client.player.getPos().getY() >= oldPos.getY() - 0.0433D)
                        tickCounter++;

                    this.oldPos = client.player.getPos();

//                    if(tickCounter >= flightConfig.floatingTickLimit) {
//                        BlockPos blockOneDownPos = client.player.getBlockPos().subtract(new Vec3i(0, 1, 0));
//
//                        if((client.player.getWorld().getBlockState(blockOneDownPos).isAir())
//                                || (client.player.getWorld().getBlockState(blockOneDownPos).getBlock().equals(Blocks.WATER)))
//                            forceFlyBypass(client);
//                        tickCounter = 0;
//                    }


                    forceFlyBypass(client);
                }

                case Position -> {
                    //TODO flight by manipulating positon
                }
            }
        }
    }

    public void forceFlyBypass(MinecraftClient client) {
        if (client.player == null) return;
//        PacketHelper.sendPositionImmediately(client.player.getPos().subtract(0.0d, 0.0433d, 0.0d), false);

        if (tickCounter >= flightConfig.floatingTickLimit)
            tickCounter = 0;

        switch (tickCounter) {
            case 0 -> {
                BlockPos blockOneDownPos = client.player.getBlockPos().subtract(new Vec3i(0, 1, 0));

                boolean isSneakingWithoutJumping =
                        client.options.sneakKey.isPressed()
                                && !client.options.jumpKey.isPressed();

                boolean isBlockBelowAirOrWater =
                        client.player.getWorld().getBlockState(blockOneDownPos).isAir()
                                || client.player.getWorld().getBlockState(blockOneDownPos).getBlock().equals(Blocks.WATER);

                // Skip this iteration if the player is already descending or on the ground
                if (isSneakingWithoutJumping || isBlockBelowAirOrWater)
                    tickCounter = 3;
                else
                    setVelocityY(client, -0.0433d);
            }
            case 1 -> setVelocityY(client, 0.0433d);
            case 2 -> setVelocityY(client, 0.0d);
        }

        tickCounter++;
    }

    private void setVelocityY(MinecraftClient client, double value) {
        if (client.player == null) return;
        Vec3d modifiedVelocity = client.player.getVelocity();
        modifiedVelocity.add(0.0d, value, 0.0d);
        client.player.setVelocity(modifiedVelocity);
    }

    public void changeMode() {
        setMode(flightConfig.flightMode == Mode.Creative ? Mode.Position : Mode.Creative);
    }

    public float getFlightSpeed() {
        return flightConfig.flightSpeed;
    }

    public void setFlightSpeed(float newSpeed) {
        flightConfig.flightSpeed = newSpeed;
    }

    public Mode getMode() {
        return flightConfig.flightMode;
    }

    public void setMode(Mode newMode) {
        flightConfig.flightMode = newMode;
    }

    @Override
    protected void onEnable() {
        super.onEnable();
        this.scheduledEnableDisableCheck = true;
    }

    @Override
    protected void onDisable() {
        super.onDisable();
        this.scheduledEnableDisableCheck = true;
    }

    public enum Mode {
        Creative,
        Position
    }
}
