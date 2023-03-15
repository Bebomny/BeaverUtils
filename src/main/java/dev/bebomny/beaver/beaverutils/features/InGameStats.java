package dev.bebomny.beaver.beaverutils.features;

import dev.bebomny.beaver.beaverutils.client.BeaverUtilsClient;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.text.Text;
import net.minecraft.util.math.Vec3d;

import java.util.ArrayList;

public class InGameStats extends Feature{

    private final MinecraftClient client;
    private final BeaverUtilsClient modBeaverUtils;

    //displayed
    private double playerSpeed;
    private ArrayList<Vec3d> previousPosBuffer;

    public InGameStats(MinecraftClient client, BeaverUtilsClient mod) {
        super("InGameStats", mod);
        this.client = client;
        this.modBeaverUtils = mod;
        this.playerSpeed = 0;
        this.previousPosBuffer = new ArrayList<>();
    }

    @Override
    protected void onUpdate(MinecraftClient client) {
        if(client.player == null)
            return;

        if(previousPosBuffer.size() >= 5) {
            previousPosBuffer.add(client.player.getPos());
            Vec3d previousPos = previousPosBuffer.remove(0);

            this.playerSpeed = previousPos.distanceTo(client.player.getPos()) * 2;
        }

        if(previousPosBuffer.size() < 5)
            previousPosBuffer.add(client.player.getPos());

    }



    public void onRenderInit(MatrixStack matrices, float partialTicks) {
        if(!isEnabled() || client.player == null)
            return;

        client.inGameHud.getTextRenderer().drawWithShadow(
                matrices,
                Text.literal(String.format("%.3g", playerSpeed) + " m/s"),
                (client.getWindow().getScaledWidth()/2f) + 100f,
                client.getWindow().getScaledHeight() - 13f,
                0xFFFFFF0F);
    }
}
