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

        previousPosBuffer.add(client.player.getPos());

        if(previousPosBuffer.size() >= 5) {
            /*
            for(Vec3d pos : previousPosBuffer) {
                this.playerSpeed += Math.abs(pos.distanceTo(client.player.getPos()));
            }

            this.playerSpeed = this.playerSpeed / previousPosBuffer.size();
             */
            Vec3d previousPos = previousPosBuffer.remove(0);
            this.playerSpeed = Math.abs(previousPos.distanceTo(client.player.getPos()) * 2);
        }



    }



    public void onRenderInit(MatrixStack matrices, float partialTicks) {
        if(!isEnabled() || client.player == null)
            return;

        client.inGameHud.getTextRenderer().drawWithShadow(
                matrices,
                Text.literal(String.format("%.3g", playerSpeed) + " m/s"),
                (client.getWindow().getScaledWidth()/2f) + 100f,
                client.getWindow().getScaledHeight() - 14f,
                0xFFFFFF0F);
    }
}
