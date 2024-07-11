package dev.bebomny.beaver.beaverutils.notifications;

import dev.bebomny.beaver.beaverutils.client.BeaverUtilsClient;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.font.TextRenderer;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.text.OrderedText;
import net.minecraft.text.Text;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public class NotificationHandler {

    private final MinecraftClient client;
    private final List<Notification> notificationQueue = new ArrayList<>();
    private int decay;

    public NotificationHandler(MinecraftClient client) {
        this.client = client;
        ClientTickEvents.END_CLIENT_TICK.register(this::onUpdate);
    }

    public void onRenderInit(DrawContext context, float partialTicks) {
        if(!notificationQueue.isEmpty()) {
            Notification notification = notificationQueue.get(0);

            //ADD
            //importance
            //if above certain  level it will add it to the stack and display a new notificationb under
            //with a certain max


            //Color logic using Category property - Maybe move it to the notification class?
            //the styling of the main notification text is handled directly in the text String
            //here I am only handling the Category/Prefix text
            Categories category = notification.getCategory();
            String customCategory = notification.getCustomCategory();

            Text textPrefix = switch (category) {

                case WARN -> Text.translatable("notification.level.warn");

                case INFO -> Text.translatable("notification.level.info");

                case STATE -> Text.translatable("notification.level.state", notification.getCallerClassName());

                case DEBUG -> Text.translatable("notification.level.debug");

                case CUSTOM -> Text.translatable("notification.level.custom", customCategory);

                case COMMAND -> Text.translatable("notification.level.command");

                case CONFIG_UPDATE -> Text.translatable("notification.level.config_update");

                case FEATURE -> notification.getCallerClassName() == null ?
                        Text.translatable("notification.level.feature")
                        : Text.translatable("notification.level.feature.with_caller_class_name", notification.getCallerClassName());


                case NONE -> Text.translatable("notification.level.none");

                default -> Text.translatable("notification.level.notification_error");
            };

            Color stockColor = new Color(255, 255, 255);
            int color = decay < 20 ?
                    ((stockColor.getRed() / 30) * decay) << 16 | ((stockColor.getGreen() / 30) * decay) << 8 | ((stockColor.getBlue() / 30) * decay)
                    :
                    stockColor.getRed() << 16 | stockColor.getGreen() << 8 | stockColor.getBlue();

            int alpha = decay < 30 ? ((0xFF / 30) * decay) << 24 : 0xFF << 24;

            //Whole message text assembly
            String text = textPrefix + notification.getText();

            //offset / x & y coordinates on the screen
            float offset = BeaverUtilsClient.getInstance().client.advanceValidatingTextRenderer.getWidth(text)/2f;
            float x = (client.getWindow().getScaledWidth()/2f) - offset;
            float y = client.getWindow().getScaledHeight() - 65f;

            //Drawing to the screen using the inGameHud Object
                //client.inGameHud.getTextRenderer().drawWithShadow(matrices, text, x, y, color | alpha); //FROM 1.19.3!!!
            context.drawTextWithShadow(client.textRenderer, text, (int) x, (int) y, color | alpha);
            //TODO: FIXX HERE
        }

        if(decay <= 1 && !notificationQueue.isEmpty())
            notificationQueue.remove(0);
    }

    private void onUpdate(MinecraftClient client) {
        if(!(notificationQueue.isEmpty()))
            decay--;
    }

    public void newNotification(Notification notification) {
        //Check if it's a debug category notification and if debug notifications are enabled
        //If they are disabled skip this notification completely
        if(notification.getCategory() == Categories.DEBUG && !BeaverUtilsClient.getInstance().getConfig().generalConfig.debug)
            return;

        if(!(notificationQueue.isEmpty()))
            notificationQueue.remove(0);

        notificationQueue.add(notification);
        decay = notification.getDuration();
    }
}
