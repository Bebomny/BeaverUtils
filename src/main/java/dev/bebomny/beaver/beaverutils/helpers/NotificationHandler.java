package dev.bebomny.beaver.beaverutils.helpers;

import dev.bebomny.beaver.beaverutils.client.BeaverUtilsClient;
import dev.bebomny.beaver.beaverutils.mixins.InGameHudMixin;
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
            //offset / x & y coordinates on the screen
            float offset = notificationQueue.get(0).getOffset();
            float x = (client.getWindow().getScaledWidth()/2f) - offset;
            float y = client.getWindow().getScaledHeight() - 60f;

            //color
            Color color1 = new Color(0xFFFFFF);
            int duration = notificationQueue.get(0).getDuration();
            int color = ((color1.getRed() / duration) * decay) << 16 | ((color1.getGreen() / duration) * decay) << 8 | ((color1.getBlue() / duration) * decay);
            int alpha = decay < 30 ? ((0xFF / 30) * decay) << 24 : 0xFF << 24;


            client.inGameHud.getTextRenderer().drawWithShadow(matrices, notificationQueue.get(0).getText(), x, y, color | alpha);

        }

        if(decay <= 0 && !notificationQueue.isEmpty())
            notificationQueue.remove(0);

    }

    private void onUpdate(MinecraftClient client) {
        if(!(notificationQueue.isEmpty()))
            decay--;
    }

    public void newNotification(Text text) {
        if(!(notificationQueue.isEmpty()))
            notificationQueue.remove(0);

        Notification notification = new Notification(text);
        notificationQueue.add(notification);
        decay = notification.getDuration();
    }

    public void newNotificationWithCustomDuration(Text text, int duration) {
        if(!(notificationQueue.isEmpty()))
            notificationQueue.remove(0);

        Notification notification = new Notification(text, duration);
        notificationQueue.add(notification);
        decay = notification.getDuration();
    }
}
