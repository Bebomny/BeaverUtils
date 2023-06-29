package dev.bebomny.beaver.beaverutils.features.features;

import dev.bebomny.beaver.beaverutils.configuration.config.InGameStatsConfig;
import dev.bebomny.beaver.beaverutils.features.SimpleOnOffFeature;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.text.Text;
import net.minecraft.util.math.Vec3d;

import java.util.ArrayList;
import java.util.List;

public class InGameStats extends SimpleOnOffFeature {

    private final InGameStatsConfig inGameStatsConfig = config.inGameStatsConfig;
    private final List<Double> distanceBuffer = new ArrayList<>();
    private float playerSpeed;
    private Vec3d prevPos;

    public InGameStats() {
        super("InGameStats");

        this.playerSpeed = 0.0f;
        this.prevPos = new Vec3d(0, 0, 0);
        setEnableConfig(inGameStatsConfig);

        if(config.generalConfig.autoEnable)
            setEnabled(inGameStatsConfig.enabled);

        ClientTickEvents.END_CLIENT_TICK.register(this::onUpdate);
    }

    private void onUpdate(MinecraftClient client) {
        if (client.player == null)
            return;

        if(distanceBuffer.size() >= inGameStatsConfig.bufferSize)
            distanceBuffer.remove(0);

        if(distanceBuffer.size() < inGameStatsConfig.bufferSize) {
            double distance = client.player.getPos().distanceTo(prevPos); //idk  if i can use this // It seems i cant lolz //using direct distance now
            distanceBuffer.add(distance);
        }

        float completeDistance = 0;

        for (Double d : distanceBuffer)
            completeDistance += d;


        playerSpeed = completeDistance / distanceBuffer.size() * distanceBuffer.size();

        prevPos = client.player.getPos();
    }


    public void onRenderInit(MatrixStack matrices, float tickDelta) {
        if(!isEnabled())
            return;

        client.inGameHud.getTextRenderer().drawWithShadow(
                matrices,
                Text.of(String.format("%.3g", playerSpeed) + " m/s"),
                client.getWindow().getScaledWidth()/2.0f + 100.0f,
                client.getWindow().getScaledHeight() - 14.0f,
                0xFFFFFF0F
        );
    }
}
