package dev.bebomny.beaver.beaverutils.features.features;

import dev.bebomny.beaver.beaverutils.configuration.config.InGameStatsConfig;
import dev.bebomny.beaver.beaverutils.features.SimpleOnOffFeature;
import dev.bebomny.beaver.beaverutils.helpers.DoubleRollingAverage;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerTickEvents;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.server.MinecraftServer;
import net.minecraft.text.MutableText;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;
import net.minecraft.util.math.Vec3d;

import java.util.ArrayList;
import java.util.List;

public class InGameStats extends SimpleOnOffFeature {

    private static final int SPACING = 5;
    private final InGameStatsConfig inGameStatsConfig = config.inGameStatsConfig;
    //Player speed
    private final List<Double> distanceBuffer = new ArrayList<>();
    private float playerSpeed;
    private Vec3d prevPos;

    //FPS
    private int fpsCounter;

    //TPS
    private static final int TPS = 20;
    private float ticksPerSec;

    //MSPT
    private boolean dedicatedServer;
    private long lastTickNano;
    private final DoubleRollingAverage rollingMsPerTick10sec;
    private final DoubleRollingAverage rollingMsPerTick60sec;
    private final DoubleRollingAverage rollingMsPerTick5min;
    private final DoubleRollingAverage rollingMsPerTick15min;
    private final DoubleRollingAverage[] rollingAverages;
    private double msPerTick;

    public InGameStats() {
        super("InGameStats");

        this.playerSpeed = 0.0f;
        this.prevPos = new Vec3d(0, 0, 0);

        this.ticksPerSec = 0.0F;
        this.fpsCounter = 0;

        this.dedicatedServer = false;
        this.msPerTick = 0.0D;
        this.lastTickNano = System.nanoTime();
        this.rollingMsPerTick10sec = new DoubleRollingAverage(TPS * 10);
        this.rollingMsPerTick60sec = new DoubleRollingAverage(TPS * 60);
        this.rollingMsPerTick5min = new DoubleRollingAverage(TPS * 60 * 5);
        this.rollingMsPerTick15min = new DoubleRollingAverage(TPS * 60 * 15);
        this.rollingAverages = new DoubleRollingAverage[]{rollingMsPerTick10sec, rollingMsPerTick60sec, rollingMsPerTick5min, rollingMsPerTick15min};

        setEnableConfig(inGameStatsConfig);

//        if(config.generalConfig.autoEnable)
//            setEnabled(inGameStatsConfig.enabled);

        ClientTickEvents.END_CLIENT_TICK.register(this::onUpdate);
        ServerTickEvents.END_SERVER_TICK.register(this::onServerUpdate);
    }

    private void onServerUpdate(MinecraftServer minecraftServer) {
        if (minecraftServer.isDedicated()) {
            return;
        }

        this.dedicatedServer = false;
        long now = System.nanoTime();
        msPerTick = (double) (now - lastTickNano) / 1000000;
        for (DoubleRollingAverage rollingAverage : rollingAverages) {
            rollingAverage.add(msPerTick);
        }
        lastTickNano = now;
    }

    private void onUpdate(MinecraftClient client) {
        //Redo speed calculation
        if (client.player == null)
            return;

        if (distanceBuffer.size() >= inGameStatsConfig.bufferSize)
            distanceBuffer.removeFirst();

        if (distanceBuffer.size() < inGameStatsConfig.bufferSize) {
            double distance = client.player.getPos().distanceTo(prevPos); //idk  if i can use this // It seems i cant lolz //using direct distance now
            distanceBuffer.add(distance);
        }

        float completeDistance = .0f;

        for (double d : distanceBuffer)
            completeDistance += (float) d;


        playerSpeed = completeDistance;

        prevPos = client.player.getPos();
        //End of speed calculation

        //TODO Add a command for this too
        //client.player.getWorld().getTickManager().getTickRate()
        //ticksPerSec = client.player.getWorld().getTickManager().getTickRate();
        //msPerTick = client.player.getWorld().getTickManager().getMillisPerTick();
        if (client.player.getServer() == null) {
            ticksPerSec = client.player.getWorld().getTickManager().getTickRate();
            //msPerTick = client.player.getWorld().getTickManager().getMillisPerTick();
        } else {
            ticksPerSec = client.player.getServer().getTickManager().getTickRate();
            //msPerTick = client.player.getServer().getAverageTickTime();
            //msPerTick = (double) client.player.getServer().getAverageNanosPerTick() / 1000000;
        }

        //when on dedicated this will be null
        if (client.getServer() == null) {
            if(!client.isIntegratedServerRunning()) {
                this.dedicatedServer = true;
            }

            long now = System.nanoTime();
            msPerTick = (double) (now - lastTickNano) / 1000000;
            for (DoubleRollingAverage rollingAverage : rollingAverages) {
                rollingAverage.add(msPerTick);
            }
            lastTickNano = now;
        }

        fpsCounter = client.getCurrentFps();
    }


    public void onRenderInit(DrawContext context, float tickDelta) {
        if (!isEnabled())
            return;
        //TODO: Add color formatting
        // Anchored spacing

        MutableText playerSpeedText = Text
                .literal(String.format("%.2f", playerSpeed))
                .formatted(Formatting.GREEN)
                .append(Text
                        .literal(" m/s")
                        .formatted(Formatting.BLUE));

        context.drawTextWithShadow(
                client.textRenderer,
                playerSpeedText,
                (int) (client.getWindow().getScaledWidth() / 2.0f + 100.0f),
                (int) (client.getWindow().getScaledHeight() - 14.0f),
                0xFFFFFF0F
        );


        /*
        FPS
            ... - 37 -> Green
            36- 16 -> Yellow
            15 - 0 -> Red
         */
        Formatting fpsFormatting;
        if (fpsCounter >= 59)
            fpsFormatting = Formatting.GREEN;
        else if (fpsCounter >= 30)
            fpsFormatting = Formatting.YELLOW;
        else
            fpsFormatting = Formatting.RED;

        //TODO: Add as translatable? for ex: "%.1f TPS" - the formatting needs to be dynamic so maybe not?
        MutableText fpsText = Text
                .literal(String.format("%d", fpsCounter))
                .formatted(fpsFormatting)
                .append(Text
                        .literal(" FPS")
                        .formatted(Formatting.BLUE));

        //Wide number for anchoring the following stats -> TPS and msPerTick
        MutableText widePlayerSpeed = Text.literal("888 m/s");
        int previousTextLength = Math.max(client.textRenderer.getWidth(playerSpeedText), client.textRenderer.getWidth(widePlayerSpeed));

        context.drawTextWithShadow(
                client.textRenderer,
                fpsText,
                (int) (client.getWindow().getScaledWidth() / 2.0f + 100.0f + SPACING + previousTextLength),
                client.getWindow().getScaledHeight() - 14,
                0xFFFFFF0F
        );

        /*
        Ticks
            20 - 17 -> Green
            16.9 - 12 -> Yellow
            11.9 - 0 -> Red
         */
        Formatting tpsFormatting;
        if (ticksPerSec >= 17f)
            tpsFormatting = Formatting.GREEN;
        else if (ticksPerSec >= 12f)
            tpsFormatting = Formatting.YELLOW;
        else
            tpsFormatting = Formatting.RED;

        //TODO: Add as translatable? for ex: "%.1f TPS" - the formatting needs to be dynamic so maybe not?
        MutableText tpsText = Text
                .literal(String.format("%.1f", ticksPerSec))
                .formatted(tpsFormatting)
                .append(Text
                        .literal(" TPS")
                        .formatted(Formatting.BLUE));

        //Wide number for anchoring the following stats -> TPS and msPerTick
        previousTextLength += SPACING;
        previousTextLength += client.textRenderer.getWidth(fpsText);

        context.drawTextWithShadow(
                client.textRenderer,
                tpsText,
                (int) (client.getWindow().getScaledWidth() / 2.0f + 100.0f + SPACING + previousTextLength),
                client.getWindow().getScaledHeight() - 14,
                0xFFFFFF0F
        );

        /*
        ms per tick
        one tick -> 50ms - baseline
            50 - 58.8ms -> Green
            58.9 - 83.3 -> Yellow
            83.4 - Integer.MAX_VALUE -> Red
         */

        Formatting msPerTickFormatting;
        if (rollingMsPerTick10sec.average() <= 58.8f)
            msPerTickFormatting = Formatting.GREEN;
        else if (rollingMsPerTick10sec.average() <= 83.3f)
            msPerTickFormatting = Formatting.YELLOW;
        else
            msPerTickFormatting = Formatting.RED;

        MutableText msPerTickText = Text
                .literal(String.format("%.2f", rollingMsPerTick10sec.average())) //msPerTick
                .formatted(msPerTickFormatting)
                .append(Text
                        .literal(" MSPT(10sec)")
                        .formatted(Formatting.BLUE));


        previousTextLength += SPACING; //spacing
        previousTextLength += client.textRenderer.getWidth(tpsText);
        context.drawTextWithShadow(
                client.textRenderer,
                msPerTickText,
                (int) (client.getWindow().getScaledWidth() / 2.0f + 100.0f + SPACING + previousTextLength),
                (int) (client.getWindow().getScaledHeight() - 14.0f),
                0xFFFFFF0F
        );
    }

    public boolean isDedicatedServer() {
        return dedicatedServer;
    }

    public DoubleRollingAverage getRollingMsPerTick10sec() {
        return rollingMsPerTick10sec;
    }

    public DoubleRollingAverage getRollingMsPerTick60sec() {
        return rollingMsPerTick60sec;
    }

    public DoubleRollingAverage getRollingMsPerTick5min() {
        return rollingMsPerTick5min;
    }

    public DoubleRollingAverage getRollingMsPerTick15min() {
        return rollingMsPerTick15min;
    }

    public double getMsPerLastTick() {
        return msPerTick;
    }

    public float getTicksPerSec() {
        return ticksPerSec;
    }
}
