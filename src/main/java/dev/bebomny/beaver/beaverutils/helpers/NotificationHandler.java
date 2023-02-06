package dev.bebomny.beaver.beaverutils.helpers;

import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.text.Text;

import java.util.ArrayList;
import java.util.List;

import java.awt.Color;


public class NotificationHandler {
    private final MinecraftClient client;
    private final List<Notification> notificationQueue = new ArrayList<>();
    private int decay;

    public NotificationHandler(MinecraftClient client) {
        this.client = client;
        ClientTickEvents.END_CLIENT_TICK.register(this::onUpdate);
    }

    public void onRenderInit(MatrixStack matrices, float partialTicks) {
        if(!(notificationQueue.isEmpty())) {
            Notification notification = notificationQueue.get(0);

            //offset / x & y coordinates on the screen
            float offset = notification.getOffset();
            float x = (client.getWindow().getScaledWidth()/2f) - offset;
            float y = client.getWindow().getScaledHeight() - 60f;

            //color
            Color color1 = notification.getColor();
            //int duration = notification.getDuration();
            //int color = ((color1.getRed() / duration) * decay) << 16 | ((color1.getGreen() / duration) * decay) << 8 | ((color1.getBlue() / duration) * decay);
            int color = color1.getRed() << 16 | color1.getGreen() << 8 | color1.getBlue();
            int alpha = decay < 30 ? ((0xFF / 30) * decay) << 24 : 0xFF << 24;



            client.inGameHud.getTextRenderer().drawWithShadow(matrices, notification.getText(), x, y, color | alpha);

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
