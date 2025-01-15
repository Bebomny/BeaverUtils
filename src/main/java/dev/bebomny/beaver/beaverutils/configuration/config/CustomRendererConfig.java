package dev.bebomny.beaver.beaverutils.configuration.config;

import com.google.gson.annotations.Expose;
import dev.bebomny.beaver.beaverutils.features.features.CustomRenderer;
import net.minecraft.util.math.Vec3d;

public class CustomRendererConfig extends EnableConfigOption {

    @Expose
    public Vec3d originPosition = new Vec3d(0, 0, 0);

    @Expose
    public boolean showOriginPoint = true;

    @Expose
    public CustomRenderer.RenderShape renderShape = CustomRenderer.RenderShape.GRID2D;

    @Expose
    public int radiusOrSpacing = 4;

    @Expose
    public boolean followTerrain = true;

    @Expose
    public int hudPosX = 30;

    @Expose
    public int hudPosY = 80;

    public CustomRendererConfig() {

    }
}
