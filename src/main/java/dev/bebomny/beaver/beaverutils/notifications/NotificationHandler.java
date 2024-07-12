package dev.bebomny.beaver.beaverutils.notifications;

import dev.bebomny.beaver.beaverutils.client.BeaverUtilsClient;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawContext;

import java.util.ArrayList;
import java.util.List;
import java.util.PriorityQueue;
import java.util.Queue;

public class NotificationHandler {
    private final MinecraftClient client;
    private final BeaverUtilsClient beaverUtilsClient;
    private final Queue<Notification> notificationQueue = new PriorityQueue<>();
    private final List<Notification> displayedNotifications = new ArrayList<>();

    public NotificationHandler(MinecraftClient client) {
        this.client = client;
        this.beaverUtilsClient = BeaverUtilsClient.getInstance();
        ClientTickEvents.END_CLIENT_TICK.register(this::onUpdate);
    }

    public void onRenderInit(DrawContext context, float partialTicks) {

        //TODO: allow for manual change of these values later
        float x = client.getWindow().getScaledWidth() / 2f;
        float y = client.getWindow().getScaledHeight() - 65f;

        for (int i = 0; i < displayedNotifications.size(); i++) {
            Notification notification = displayedNotifications.get(i);

            float notificationY = y - (i * client.advanceValidatingTextRenderer.fontHeight);
            notification.renderAt(client, context, partialTicks, (int) x, (int) notificationY);
        }
    }

    private void onUpdate(MinecraftClient client) {
        //If the displayedNotifications list is empty, add a new one and keep them under certain limit,
        // If a priority one comes in display it regardless of the limit.
        //if (!(displayedNotifications.isEmpty()))
        displayedNotifications.forEach(Notification::progressDecay);

        displayedNotifications.removeIf(notification -> notification.getOnScreenDecay() <= 0);

        for (Notification notification : notificationQueue) {
            if (notification.getPriority() >= 5) {
                displayedNotifications.addLast(notificationQueue.poll());
            }

            if (displayedNotifications.size() < beaverUtilsClient.getConfig().generalConfig.softNotificationMax) {
                displayedNotifications.addLast(notificationQueue.poll());
                return;
            }
        }

    }

    public void newNotification(Notification notification) {
        //If notification comes in as DEBUG message, check if debug is enabled
        // then add it to the queue.
        //TODO: add a debug check
        // if(notification.getLevel() == Notification.LEVEL.DEBUG && {config.general.debug} != true)
        //   return;

        //Check if currently displayed notifications are under the limit, and maybe add it directly?
        notificationQueue.add(notification);

        //TODO: add a limit and remove some notifications if it has been reached
        // Temp solution
        // dont remove from the queue, try to remove from the current displayed stack quicker
        if (notificationQueue.size() > 32)
            notificationQueue.removeIf(notification1 -> true);
    }
}
