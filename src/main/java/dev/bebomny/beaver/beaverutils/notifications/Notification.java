package dev.bebomny.beaver.beaverutils.notifications;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.text.MutableText;
import net.minecraft.text.Text;
import org.jetbrains.annotations.NotNull;

import java.awt.*;

public class Notification implements Comparable<Notification> {
    public final MutableText text;
    private final int duration;
    private MutableText parentText;

    //The higher the number, the more important the notification is
    // From "5" and above the notification becomes a "Priority Notification",
    // and is going to be displayed regardless of the current amount of displayed notifications
    // TODO: add a special effect to be noticeable more as the priority increases
    private int priorityValue;
    private int onScreenDecay;
    private LEVEL level;

    public static Builder builder(MutableText text) {
        return new Builder(text);
    }

    protected Notification(MutableText text, int duration) {
        this.text = text;
        this.duration = duration;
        this.level = LEVEL.INFO;
    }

    public void renderAt(MinecraftClient client, DrawContext context, float partialTicks, int x, int y) {
        //TODO: The colors are not applied at all, try to fix them later
        Text notificationTextAssembly = Text.translatable(
                level.getNotificationTextKey(),
                getParentText().getString(), getText());

        Color stockColor = new Color(255, 255, 255);
        int color = onScreenDecay < 30 ?
                ((stockColor.getRed() / 30) * onScreenDecay) << 16
                        | ((stockColor.getGreen() / 30) * onScreenDecay) << 8
                        | ((stockColor.getBlue() / 30) * onScreenDecay)
                :
                stockColor.getRed() << 16
                        | stockColor.getGreen() << 8
                        | stockColor.getBlue();
        int alpha = onScreenDecay < 30 ? ((0xFF / 30) * onScreenDecay) << 24 : 0xFF << 24;

        context.drawCenteredTextWithShadow(
                client.textRenderer,
                notificationTextAssembly,
                x, y,
                color | alpha);
    }

    public void setParentText(MutableText parentText) {
        this.parentText = parentText;
    }

    public void setPriority(int priorityValue) {
        this.priorityValue = priorityValue;
    }

    protected void setInitialOnScreenDecay(int duration) {
        this.onScreenDecay = duration;
    }

    public void setLevel(LEVEL level) {
        this.level = level;
    }

    //Decrements the decay counter by one, should be called every tick!(Client tick!)
    public void progressDecay() {
        this.onScreenDecay--;
    }

    public MutableText getParentText() {
        return parentText;
    }

    public MutableText getText() {
        return text;
    }

    public int getDuration() {
        return duration;
    }

    public int getPriority() {
        return priorityValue;
    }

    public int getOnScreenDecay() {
        return onScreenDecay;
    }

    public LEVEL getLevel() {
        return level;
    }

    @Override
    public int compareTo(@NotNull Notification o) {
        return o.priorityValue - this.priorityValue; //this.priorityValue - o.priorityValue
    }

    public static class Builder {
        private final MutableText text;
        private int duration = 60;
        private MutableText parentText = Text.translatable("notification.parent.missing");
        private int priorityValue = 1;
        private LEVEL level = LEVEL.INFO;

        public Builder(MutableText text) {
            this.text = text;
        }

        public Builder duration(int customDuration) {
            this.duration = customDuration;
            return this;
        }

        public Builder parent(MutableText parent) {
            this.parentText = parent;
            return this;
        }

        public Builder priority(int priorityValue) {
            this.priorityValue = priorityValue;
            return this;
        }

        public Builder level(LEVEL newLevel) {
            this.level = newLevel;
            return this;
        }

        public Notification build() {
            Notification notification = new Notification(text, duration);
            notification.setParentText(parentText);
            notification.setPriority(priorityValue);
            notification.setInitialOnScreenDecay(duration);
            notification.setLevel(level);
            return notification;
        }
    }

    public enum LEVEL {
        INFO,
        DEBUG,
        WARN,
        ERROR;

        public String getNotificationTextKey() {
            return switch (this) {
                case INFO -> "notification.level.info";
                case DEBUG -> "notification.level.debug";
                case WARN -> "notification.level.warn";
                case ERROR -> "notification.level.error";
            };
        }
    }
}
