package dev.bebomny.beaver.beaverutils.notifications;

import dev.bebomny.beaver.beaverutils.client.BeaverUtilsClient;
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

            //ADD
            //importance
            //if above certain  level it will add it to the stack and display a new notificationb under
            //with a certain max


            //Color logic using Category property - Maybe move it to the notification class?
            //the styling of the main notification text is handled directly in the text String
            //here I am only handling the Category/Prefix text
            Categories category = notification.getCategory();
            String customCategory = notification.getCustomCategory();

            String textPrefix = switch (category) {

                case WARN -> "§6§l[" + "§eWARN" +"§6§l]" + "§r ";

                case INFO -> "§6§l[" + "§fINFO" +"§6§l]" + "§r ";

                case STATE -> "§6§l[" + "§2STATE " + "§0-" + " §2" + notification.getCallerClassName() + "§6§l]" + "§r ";

                case DEBUG -> "§6§l[" + "§3DEBUG" +"§6§l]" + "§r ";

                case CUSTOM -> "§6§l[" + customCategory +"§6§l]" + "§r ";

                case COMMAND -> "§6§l[" + "§5COMMAND" +"§6§l]" + "§r ";

                case CONFIG_UPDATE -> "§6§l[" + "§1CONFIG UPDATED" +"§6§l]" + "§r ";

                case FEATURE -> "§6§l[" + "§1FEATURE" +"§6§l]" + "§r ";

                case NONE -> "";

                default -> "§c§l[YOU SHOULD NOT SEE THIS, SOMETHING WENT HORRIBLY WRONG]" + "§r ";
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
