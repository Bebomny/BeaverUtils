package dev.bebomny.beaver.beaverutils.helpers;

import dev.bebomny.beaver.beaverutils.client.BeaverUtilsClient;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.font.TextRenderer;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.util.BufferAllocator;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.text.MutableText;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.RotationAxis;
import net.minecraft.util.math.Vec3d;

public class TextRenderUtils {

    //Drawing
    private static final VertexConsumerProvider.Immediate immediate = VertexConsumerProvider.immediate(new BufferAllocator(256));

    //
    private static BeaverUtilsClient beaverUtilsClient = BeaverUtilsClient.getInstance();
    private static MinecraftClient client = MinecraftClient.getInstance();

    public static void drawTextAtPos(Text text, MatrixStack matrixStack, Vec3d centerTextPosition) {
        drawTextWithTextureAtPos(text, matrixStack, centerTextPosition, null, null, null);
    }

    public static void drawTextWithTextureAtPos(Text text, MatrixStack matrixStack, Vec3d centerTextPosition, Identifier texture, Integer texturesInImage, Integer textureNum) {

        matrixStack.push();

        //TODO: Make the pitch adjust itself too
        Vec3d playerPos = client.player.getPos();
        double xDiff = playerPos.x - centerTextPosition.x;
//            double e = target.y - entityBoxCenterPos.y;
        double zDiff = playerPos.z - centerTextPosition.z;
//            double g = Math.sqrt(d * d + f * f);
//            float pitch = MathHelper.wrapDegrees((float)(-(MathHelper.atan2(e, g) * 57.2957763671875)));
        float yaw = MathHelper.wrapDegrees((float) (MathHelper.atan2(zDiff, xDiff) * 57.2957763671875 + 90.0F));

        //Rotate the matrix plain to face the player
        matrixStack.multiply(RotationAxis.NEGATIVE_Y.rotationDegrees(yaw));

//        //Math exam
//        MutableText textWithExtendedInfo = text.copy().append(Text.literal(" | Angle %.1f*".formatted(yaw)));
//        text = textWithExtendedInfo;
//        //Math exam

        //Texture?
        if (texture != null) {
            matrixStack.push(); // Stage 4 - Textures
            matrixStack.translate(0f, 0.3f, 0f);
            //RenderUtils.drawTexture(ARROWS, 8, 4, matrixStack); //4
            RenderUtils.drawTexture(texture, texturesInImage, textureNum, matrixStack);
            matrixStack.pop(); // Stage 4 - Textures
        }

        //TODO:
        // `client.options.getGuiScale().getValue()` doesnt quite work right
        // maybe make a custom scale setting? Or adjust the scale based on distance?
        double scale = 1; // for testing = 1
        matrixStack.scale(-0.025f * (float) scale, -0.025f * (float) scale, 1);
        matrixStack.translate(0, -10, 0);

        int opacity = (int) (client.options.getTextBackgroundOpacity(0.25F) * 255.0F) << 24;
        float halfWidth = ((float) client.textRenderer.getWidth(text) / 2);

        client.textRenderer.draw(
                text, -halfWidth, 0f,
                553648127, false,
                matrixStack.peek().getPositionMatrix(), immediate,
                TextRenderer.TextLayerType.SEE_THROUGH,
                opacity, 0xf000f0);
        immediate.draw();

        client.textRenderer.draw(
                text, -halfWidth, 0f,
                -1, false,
                matrixStack.peek().getPositionMatrix(), immediate,
                TextRenderer.TextLayerType.SEE_THROUGH,
                0, 0xf000f0);
        immediate.draw();

        matrixStack.pop();
    }

    //TODO delete later
    public static void DELETE_LATER_drawTextWithTextureAtPos(Text text, MatrixStack matrixStack, Vec3d centerTextPosition, Identifier texture, Integer texturesInImage, Integer textureNum) {

        matrixStack.push();

        //TODO: Make the pitch adjust itself too
        Vec3d playerPos = client.player.getPos();
        double xDiff = playerPos.x - centerTextPosition.x;
        double zDiff = playerPos.z - centerTextPosition.z;
//            double g = Math.sqrt(d * d + f * f);
//            float pitch = MathHelper.wrapDegrees((float)(-(MathHelper.atan2(e, g) * 57.2957763671875)));
        float yaw = MathHelper.wrapDegrees((float) (MathHelper.atan2(zDiff, xDiff) * 57.2957763671875 + 90.0F));

        //Rotate the matrix plain to face the player
        matrixStack.multiply(RotationAxis.NEGATIVE_Y.rotationDegrees(yaw));

        //Texture?
        if (texture != null) {
            matrixStack.push(); // Stage 4 - Textures
            matrixStack.translate(0f, 0.3f, 0f);
            //RenderUtils.drawTexture(ARROWS, 8, 4, matrixStack); //4
            RenderUtils.drawTexture(texture, texturesInImage, textureNum, matrixStack);
            matrixStack.pop(); // Stage 4 - Textures
        }

        double scale = 1; // for testing = 1
        matrixStack.scale(-0.025f * (float) scale, -0.025f * (float) scale, 1);
        matrixStack.translate(0, -10, 0);

        int opacity = (int) (client.options.getTextBackgroundOpacity(0.25F) * 255.0F) << 24;
        float halfWidth = ((float) client.textRenderer.getWidth(text) / 2);

        client.textRenderer.draw(
                text, -halfWidth, 0f,
                553648127, false,
                matrixStack.peek().getPositionMatrix(), immediate,
                TextRenderer.TextLayerType.SEE_THROUGH,
                opacity, 0xf000f0);
        immediate.draw();

        client.textRenderer.draw(
                text, -halfWidth, 0f,
                -1, false,
                matrixStack.peek().getPositionMatrix(), immediate,
                TextRenderer.TextLayerType.SEE_THROUGH,
                0, 0xf000f0);
        immediate.draw();

        matrixStack.pop();
    }
}
