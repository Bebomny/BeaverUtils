package dev.bebomny.beaver.beaverutils.notifications;

import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.util.math.MatrixStack;
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

    public void onRenderInit(MatrixStack matrices, float partialTicks) {
        if(!notificationQueue.isEmpty()) {
            Notification notification = notificationQueue.get(0);

            //offset / x & y coordinates on the screen
            float offset = notification.getOffset();
            float x = (client.getWindow().getScaledWidth()/2f) - offset;
            float y = client.getWindow().getScaledHeight() - 65f;

            //Color logic using Category property
            //Color of the main text is specified in the text filed in the notification
            //here I am only handling the Category/Prefix text
            Categories category = notification.getCategory();
            String customCategory = notification.getCustomCategory();

            Text textPrefix = switch (category) {

                case WARN -> Text.literal("§6§l[" + "§eWARN" +"§6§l]" + "§r ");

                case INFO -> Text.literal("§6§l[" + "§fINFO" +"§6§l]" + "§r ");

                case STATE -> Text.literal("§6§l[" + "§aSTATE - " + notification.getCallerClassName() +"§6§l]" + "§r ");

                case DEBUG -> Text.literal("§6§l[" + "§9DEBUG" +"§6§l]" + "§r ");

                case CUSTOM -> Text.literal("§6§l[" + customCategory +"§6§l]" + "§r ");
            };

            /*
            color -- OLD
            int duration = notification.getDuration();
            int color = ((color1.getRed() / duration) * decay) << 16 | ((color1.getGreen() / duration) * decay) << 8 | ((color1.getBlue() / duration) * decay);
            */

            Color stockColor = new Color(255, 255, 255);
            int color = decay < 20 ?
                    ((stockColor.getRed() / 30) * decay) << 16 | ((stockColor.getGreen() / 30) * decay) << 8 | ((stockColor.getBlue() / 30) * decay)
                    :
                    stockColor.getRed() << 16 | stockColor.getGreen() << 8 | stockColor.getBlue();

            int alpha = decay < 30 ? ((0xFF / 30) * decay) << 24 : 0xFF << 24;

            //Whole message text assembly
            Text text = Text.literal(textPrefix + "" + notification.getText());

            client.inGameHud.getTextRenderer().drawWithShadow(matrices, text, x, y, color | alpha);

        }

        if(decay <= 1 && !notificationQueue.isEmpty())
            notificationQueue.remove(0);
    }

    private void onUpdate(MinecraftClient client) {
        if(!(notificationQueue.isEmpty()))
            decay--;
    }

    public void newNotification(Notification notification) {
        if(!(notificationQueue.isEmpty()))
            notificationQueue.remove(0);

        notificationQueue.add(notification);
        decay = notification.getDuration();
    }
}
