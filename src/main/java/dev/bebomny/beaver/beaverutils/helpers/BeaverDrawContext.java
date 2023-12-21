package dev.bebomny.beaver.beaverutils.helpers;

import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.render.GameRenderer;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.text.OrderedText;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;

public class BeaverDrawContext {
    private final MinecraftClient client = MinecraftClient.getInstance();
    private final DrawContext drawContext;

    private BeaverDrawContext(DrawContext drawContext) {
        this.drawContext = drawContext;
    }

    public static BeaverDrawContext wrap(DrawContext drawContext) {
        return new BeaverDrawContext(drawContext);
    }

    public DrawContext raw() {
        return drawContext;
    }

    public MatrixStack matrices() {
        return drawContext.getMatrices();
    }

    public void push() {
        matrices().push();
    }

    public void pop() {
        matrices().pop();
    }

    public void drawTexture(Identifier texture, int x, int y, int u, int v, int width, int height) {
        drawTexture(texture, x, y, width, height, u, v, width, height, 256, 256);
    }

    public void drawTexture(Identifier texture, int x, int y, int z, float u, float v, int width, int height) {
        drawTexture(texture, x, y, z, u, v, width, height, 256, 256);
    }

    public void drawTexture(Identifier texture, int x, int y, int z, float u, float v, int width, int height, int textureWidth, int textureHeight) {
        RenderSystem.setShader(GameRenderer::getPositionTexProgram);
        drawContext.drawTexture(texture, x, y, z, u, v, width, height, textureWidth, textureHeight);
    }

    public void drawTexture(Identifier texture, int x, int y, int width, int height, float u, float v, int regionWidth, int regionHeight, int textureWidth, int textureHeight) {
        RenderSystem.setShader(GameRenderer::getPositionTexProgram);
        drawContext.drawTexture(texture, x, y, width, height, u, v, regionWidth, regionHeight, textureWidth, textureHeight);
    }

    public void fill(int x, int y, int width, int height, int color) {
        drawContext.fill(x, y, x + width, y + height, color);
    }

    public void drawTextWithShadow(Text text, int x, int y) {
        drawTextWithShadow(text, x, y, -1);
    }

    public void drawTextWithShadow(Text text, int x, int y, int color) {
        drawContext.drawText(client.textRenderer, text, x, y, color, true);
    }

    public void drawTextWithShadow(OrderedText text, int x, int y, int color) {
        drawContext.drawText(client.textRenderer, text, x, y, color, true);
    }

    public void drawCenteredTextWithShadow(Text text, int x, int y) {
        drawCenteredTextWithShadow(text, x, y, -1);
    }

    public void drawCenteredTextWithShadow(Text text, int x, int y, int color) {
        drawContext.drawCenteredTextWithShadow(client.textRenderer, text.asOrderedText(), x, y, color);
    }
}
