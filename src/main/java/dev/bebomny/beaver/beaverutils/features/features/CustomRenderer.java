package dev.bebomny.beaver.beaverutils.features.features;

import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.serialization.Codec;
import dev.bebomny.beaver.beaverutils.configuration.config.CustomRendererConfig;
import dev.bebomny.beaver.beaverutils.configuration.gui.menus.CustomRendererMenu;
import dev.bebomny.beaver.beaverutils.features.SimpleOnOffFeature;
import dev.bebomny.beaver.beaverutils.helpers.RenderUtils;
import dev.bebomny.beaver.beaverutils.helpers.TextRenderUtils;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.rendering.v1.HudRenderCallback;
import net.fabricmc.fabric.api.client.rendering.v1.WorldRenderContext;
import net.fabricmc.fabric.api.client.rendering.v1.WorldRenderEvents;
import net.minecraft.block.AirBlock;
import net.minecraft.block.TorchBlock;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.font.TextRenderer;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.render.Camera;
import net.minecraft.client.render.RenderTickCounter;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.util.BufferAllocator;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.text.Text;
import net.minecraft.util.BlockMirror;
import net.minecraft.util.StringIdentifiable;
import net.minecraft.util.math.*;
import org.lwjgl.opengl.GL11;

import java.util.ArrayList;
import java.util.List;
import java.util.function.BiConsumer;
import java.util.function.Consumer;

public class CustomRenderer extends SimpleOnOffFeature {
    //TODO: Yeet this into RenderUtils with a worldDrawText function
    //Drawing
    private final VertexConsumerProvider.Immediate immediate = VertexConsumerProvider.immediate(new BufferAllocator(256));

    private final CustomRendererConfig customRendererConfig = config.customRendererConfig;

    private static int hudLineHeight = 14;
    private static final int hudEntryWidthMin = 100;
    private static int titleColumnWidth = 100;
    private static int valueColumnWidth = 70;

    public CustomRenderer() {
        super("CustomRenderer");

        setEnableConfig(customRendererConfig);
        setOptionsMenu(new CustomRendererMenu());

        ClientTickEvents.END_CLIENT_TICK.register(this::onUpdate);
        HudRenderCallback.EVENT.register(this::onHudRenderInit);
        WorldRenderEvents.BEFORE_DEBUG_RENDER.register(this::onRenderWorld);
    }

    private void onUpdate(MinecraftClient minecraftClient) {
    }

    private void onHudRenderInit(DrawContext drawContext, RenderTickCounter renderTickCounter) {
        //TODO: Render a simple hud with all the settings
        //  Origin position

        if (!isEnabled())
            return;

        if (client.textRenderer == null)
            return;

        List<HudEntry> hudEntryList = new ArrayList<>();

        hudLineHeight = Math.max(client.textRenderer.fontHeight + 2 + 2, hudLineHeight);

        int textBackgroundColor = this.client.options.getTextBackgroundColor(553648127);
        int textColor = 16777215;

        RenderSystem.enableBlend();
        int entryPosX = beaverUtilsClient.getFeatures().entityListDisplay.isEnabled() ?
                customRendererConfig.hudPosX + beaverUtilsClient.getFeatures().entityListDisplay.getTotalWidth() + 10 :
                customRendererConfig.hudPosX;
        int entryPosY = customRendererConfig.hudPosY + 4;

        hudEntryList.add(new HudEntry(
                Text.of("Origin Position:"),
                Text.of("x: %.1f, y: %.1f, z: %.1f".formatted(getOriginPosition().getX(), getOriginPosition().getY(), getOriginPosition().getZ()))));
        hudEntryList.add(new HudEntry(
                Text.of("Selected Shape:"),
                Text.of(getRenderShape().name())));
        hudEntryList.add(new HudEntry(
                Text.of((getRenderShape() == RenderShape.GRID2D) || (getRenderShape() == RenderShape.GRID3D) ? "Spacing:" : "Radius:"),
                Text.of("%d".formatted(getRadiusOrSpacing()))));

        hudEntryList.forEach(entry -> {
            titleColumnWidth = Math.max(titleColumnWidth, client.textRenderer.getWidth(entry.titleText()) + 8);
            valueColumnWidth = Math.max(valueColumnWidth, client.textRenderer.getWidth(entry.valueText()) + 8);
        });

        drawContext.fill(
                entryPosX - 4,
                entryPosY - 4,
                entryPosX + titleColumnWidth + valueColumnWidth + 4,
                entryPosY + (hudLineHeight * hudEntryList.size()) + 4,
                Integer.MIN_VALUE);

        for (int i = 0; i < hudEntryList.size(); i++) {
            drawContext.drawTextWithShadow(client.textRenderer, hudEntryList.get(i).titleText, entryPosX, entryPosY + (hudLineHeight * i), textColor);
            drawContext.drawTextWithShadow(client.textRenderer, hudEntryList.get(i).valueText, entryPosX + titleColumnWidth, entryPosY + (hudLineHeight * i), textColor);
            drawContext.drawHorizontalLine(entryPosX - 2, entryPosX + titleColumnWidth + valueColumnWidth + 2, entryPosY + (hudLineHeight * i) + hudLineHeight - 4, 0x6FAFAFAF);
        }
    }

    private void onRenderWorld(WorldRenderContext worldRenderContext) {
        //TODO: draw something in the origin position and add text if possible
        // Also check if th origin is out of player view distance to not make unnecessary draw calls

        MatrixStack matrixStack = worldRenderContext.matrixStack();

        if (!isEnabled() || (matrixStack == null) || client.player == null)
            return;

        // Stage 1 - Custom Renderer Complete Rendering Logic
        matrixStack.push();

//        //OpenGL settings
//        RenderSystem.enableBlend();
//        RenderSystem.defaultBlendFunc();
//        RenderSystem.blendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
//        RenderSystem.disableDepthTest();

        // Stage 2.1 - Origin Position Marker
        matrixStack.push();

        renderOriginPositionPointMarker(matrixStack);

        matrixStack.pop(); // Stage 2.1 - Origin Position Marker

        // Stage 2.2 - Grid / Circle / Sphere rendering
        matrixStack.push();

        if(getRenderShape() == RenderShape.GRID2D) {
            //executeOn2DSpiralFromPosition(originPosition.getX(), originPosition.getY(), originPosition.getZ(), this::renderGrid, matrixStack);
            executeOn2DSpiralFromPosition(0, 0, 0, this::renderGrid, matrixStack);
        }
        matrixStack.pop();

        matrixStack.pop(); // Stage 1 - Custom Renderer Complete Rendering Logic
    }

    private void renderGrid(Vec3d position, MatrixStack matrixStack) {
        if(position == null || (position.equals(new Vec3d(0, 0, 0))) || client.world == null)
            return;

        Vec3i intPosition = new Vec3i(
                (int)Math.floor(position.getX()),
                (int)Math.floor(position.getY()),
                (int)Math.floor(position.getZ()));

        if(intPosition.getX() % getRadiusOrSpacing() == 0 && intPosition.getZ() % getRadiusOrSpacing() == 0) {
            matrixStack.push();

            //OpenGL settings
            RenderSystem.enableBlend();
            RenderSystem.defaultBlendFunc();
            RenderSystem.blendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
            RenderSystem.disableDepthTest();
            RenderSystem.disableCull();

            Camera camera = MinecraftClient.getInstance().gameRenderer.getCamera();

            Vec3d renderPosition = getOriginPosition().add(position);
            Vec3i intRenderPosition = new Vec3i((int)Math.floor(renderPosition.getX()), (int)Math.floor(renderPosition.getY()), (int)Math.floor(renderPosition.getZ()));

            if (doFollowTerrain()) {
                int maxSteps = 3;
                int steps = 0;
                while ((client.world.getBlockState(new BlockPos(intRenderPosition).subtract(new Vec3i(0, 1, 0))).getBlock() instanceof AirBlock)
                        || (client.world.getBlockState(new BlockPos(intRenderPosition).subtract(new Vec3i(0, 1, 0))).getBlock() instanceof TorchBlock)) {

                    if (client.world.getBlockState(new BlockPos(intRenderPosition)).getBlock() instanceof TorchBlock)
                        break;

                    if (steps >= maxSteps)
                        break;

                    if (client.world.getBlockState(new BlockPos(intRenderPosition)).getBlock() instanceof AirBlock) {
                        intRenderPosition = intRenderPosition.subtract(new Vec3i(0, 1, 0));
                        renderPosition = renderPosition.subtract(new Vec3d(0D, 1D, 0D));
                    } else if (!(client.world.getBlockState(new BlockPos(intRenderPosition)).getBlock() instanceof AirBlock)) {
                        intRenderPosition = intRenderPosition.add(new Vec3i(0, 1, 0));
                        renderPosition = renderPosition.add(new Vec3d(0D, 1D, 0D));
                    }
                    steps++;
                }
            }

            int colorARGB = switch (client.world.getBlockState(new BlockPos(intRenderPosition)).getBlock()) {
                case AirBlock ignored -> 0xFF668AFF; //Bluish
                case TorchBlock ignored -> 0xFF54FF6E; //Greenish
                default -> 0xFFD9FF70; //Yellowish
            };

            matrixStack.translate(renderPosition.getX() - camera.getPos().x, renderPosition.getY() - camera.getPos().y, renderPosition.getZ() - camera.getPos().z);
            matrixStack.translate(-0.5D, 0D, -0.5D);

            Box blockBox = new Box(0, 0, 0, 1, 1, 1);
            RenderUtils.drawOutlinedBox(blockBox, 2f, matrixStack, colorARGB);
            //RenderUtils.drawOutlinedBox(matrixStack);

            // OpenGL setting resets
            RenderSystem.setShaderColor(1, 1, 1, 1);
            RenderSystem.enableDepthTest();
            RenderSystem.disableBlend();
            RenderSystem.enableCull();

            matrixStack.pop();
        }

    }

    private void executeOn2DSpiralFromPosition(double startX, double startY, double startZ, BiConsumer<Vec3d, MatrixStack> function, MatrixStack matrixStack) {
        int maxRange = 32; // Maximum range for both x and z coordinates (boundaries of the grid)
        double x = startX;
        double z = startZ;
        double y = startY;

        // Bounds that will grow as we spiral outward
        double left = startZ;
        double right = startZ;
        double top = startX;
        double bottom = startX;

        // Direction vectors for right, down, left, up movements in (x, z) plane
        int[][] directions = {{0, 1}, {1, 0}, {0, -1}, {-1, 0}};
        int dir = 0;  // Start by moving "right" in terms of x and z

        // Control the expansion of the spiral; stops when boundaries exceed the grid limits
        while (left >= -maxRange && right <= maxRange && top >= -maxRange && bottom <= maxRange) {
            // Move in the current direction
            // Exit loop to change direction
            do {
                // Apply the function to the current node
                function.accept(new Vec3d(x, y, z).add(startX, startY, startZ), matrixStack);

                // Determine the next position in the current direction (spiral traversal)
                x += directions[dir][0];
                z += directions[dir][1];

                // Check if we should change direction based on the boundaries
            } while ((dir != 0 || z <= right) && (dir != 1 || x <= bottom) &&
                    (dir != 2 || z >= left) && (dir != 3 || x >= top));

            // Update the boundaries after completing a movement in a certain direction
            if (dir == 0) {  // Moving right (increasing z)
                right += 1;
            } else if (dir == 1) {  // Moving down (increasing x)
                bottom += 1;
            } else if (dir == 2) {  // Moving left (decreasing z)
                left -= 1;
            } else if (dir == 3) {  // Moving up (decreasing x)
                top -= 1;
            }

            // Switch to the next direction (right -> down -> left -> up)
            dir = (dir + 1) % 4;
        }
    }

    private void renderOriginPositionPointMarker(MatrixStack matrixStack) {

        if(client.player.squaredDistanceTo(getOriginPosition()) > 16384) //128^2
            return;

        matrixStack.push(); // Stage 1 - OpenGL settings

        //OpenGL settings
        RenderSystem.enableBlend();
        RenderSystem.defaultBlendFunc();
        RenderSystem.blendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
        RenderSystem.disableDepthTest();
        RenderSystem.disableCull();

        Camera camera = MinecraftClient.getInstance().gameRenderer.getCamera();

        matrixStack.push(); // Stage 2 - Translations into correct position

        Box markerBox = new Box(0d, 0, 0d, 0.4d, 1.6d, 0.4d);
        Vec3d markerPos = getOriginPosition();

        Vec3d markerPosCorner = markerPos.subtract(markerBox.getLengthX() / 2, 0, markerBox.getLengthZ() / 2);
        matrixStack.translate(markerPosCorner.getX() - camera.getPos().x, markerPosCorner.getY() - camera.getPos().y, markerPosCorner.getZ() - camera.getPos().z);

        // Stage 3.1 - Marker Box outline
        matrixStack.push();
        //TODO: scale the line thickness with distance
        RenderUtils.drawOutlinedBox(markerBox, 8f, matrixStack, 0xFF00FF00);
        //RenderUtils.drawOutlinedBoxRainbow(entityBox, matrixStack);
        matrixStack.pop(); // Stage 3.1 - Marker Box outline


        // Stage 3.2 - Marker Text Display
        matrixStack.push();
        Vec3d center = markerBox.getCenter();
        matrixStack.translate(center.getX(), markerBox.getLengthY(), center.getZ());

        //matrixStack.translate(0f, 0.3f, 0f);
        TextRenderUtils.drawTextAtPos(
                Text.literal("Origin Marker"),
                matrixStack,
                markerPos);

        matrixStack.pop(); // Stage 3.2 - Marker Text Display

        matrixStack.pop(); // Stage 2 - Translations into correct position

        // OpenGL setting resets
        RenderSystem.setShaderColor(1, 1, 1, 1);
        RenderSystem.enableDepthTest();
        RenderSystem.disableBlend();
        RenderSystem.enableCull();

        matrixStack.pop(); // Stage 1 - OpenGL settings
    }

    public Vec3d getOriginPosition() {
        return customRendererConfig.originPosition;
    }

    public void setOriginPosition(Vec3d newOriginPosition) {
        customRendererConfig.originPosition = newOriginPosition;
    }

    public boolean getShowOriginPoint() {
        return customRendererConfig.showOriginPoint;
    }

    public void setShowOriginPoint(boolean newShowOriginPoint) {
        customRendererConfig.showOriginPoint = newShowOriginPoint;
    }

    public int getRadiusOrSpacing() {
        return customRendererConfig.radiusOrSpacing;
    }

    public void setRadiusOrSpacing(int newRadiusOrSpacing) {
        customRendererConfig.radiusOrSpacing = Math.max(newRadiusOrSpacing, 1);
    }

    public boolean doFollowTerrain() {
        return customRendererConfig.followTerrain;
    }

    public void setFollowTerrain(boolean newFollowTerrain) {
        customRendererConfig.followTerrain = newFollowTerrain;
    }

    public RenderShape getRenderShape() {
        return customRendererConfig.renderShape;
    }

    public void setRenderShape(RenderShape newRenderShape) {
        customRendererConfig.renderShape = newRenderShape;
    }

    public void cycleRenderShape() {
        customRendererConfig.renderShape = customRendererConfig.renderShape == RenderShape.GRID2D ? RenderShape.GRID3D
                : customRendererConfig.renderShape == RenderShape.GRID3D ? RenderShape.CIRCLE
                : customRendererConfig.renderShape == RenderShape.CIRCLE ? RenderShape.SPHERE : RenderShape.GRID2D;
    }

    public enum RenderShape implements StringIdentifiable {
        GRID2D,
        GRID3D,
        CIRCLE,
        SPHERE;

        public static final Codec<RenderShape> CODEC = StringIdentifiable.createCodec(RenderShape::values);

        @Override
        public String asString() {
            return this.name().toLowerCase();
        }
    }

    private record HudEntry(Text titleText, Text valueText) { }
}
