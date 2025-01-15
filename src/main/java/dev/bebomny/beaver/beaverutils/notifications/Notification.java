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
    private int customX;
    private int customY;

    protected Notification(MutableText text, int duration) {
        this.text = text;
        this.duration = duration;
        this.level = LEVEL.INFO;
    }

    public static Builder builder(MutableText text) {
        return new Builder(text);
    }

    public void renderAt(MinecraftClient client, DrawContext context, float partialTicks, int x, int y) {
        //TODO: The colors are not applied at all, try to fix them later
        Text notificationTextAssembly = Text.translatable(
                level.getNotificationTranslationKey(),
                getParentText().getString(), getText());

        Color stockColor = new Color(255, 255, 255);
        int decayTimeBound = Math.min(getDuration(), 30);
        int color = onScreenDecay < decayTimeBound ?
                ((stockColor.getRed() / decayTimeBound) * onScreenDecay) << 16
                        | ((stockColor.getGreen() / decayTimeBound) * onScreenDecay) << 8
                        | ((stockColor.getBlue() / decayTimeBound) * onScreenDecay)
                :
                stockColor.getRed() << 16
                        | stockColor.getGreen() << 8
                        | stockColor.getBlue();
        int alpha = onScreenDecay < decayTimeBound ? ((0xFF / decayTimeBound) * onScreenDecay) << 24 : 0xFF << 24;

        context.drawCenteredTextWithShadow(
                client.textRenderer,
                notificationTextAssembly,
                x, y,
                color | alpha);
    }

    protected void setInitialOnScreenDecay(int duration) {
        this.onScreenDecay = duration;
    }

    //Decrements the decay counter by one, should be called every tick!(Client tick!)
    public void progressDecay() {
        this.onScreenDecay--;
    }

    public MutableText getParentText() {
        return parentText;
    }

    public void setParentText(MutableText parentText) {
        this.parentText = parentText;
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

    public void setPriority(int priorityValue) {
        this.priorityValue = priorityValue;
    }

    public int getOnScreenDecay() {
        return onScreenDecay;
    }

    public LEVEL getLevel() {
        return level;
    }

    public void setLevel(LEVEL level) {
        this.level = level;
    }

    @Override
    public int compareTo(@NotNull Notification o) {
        return o.priorityValue - this.priorityValue; //this.priorityValue - o.priorityValue
    }

    public int getCustomX() {
        return customX;
    }

    public void setCustomX(int customX) {
        this.customX = customX;
    }

    public int getCustomY() {
        return customY;
    }

    public void setCustomY(int customY) {
        this.customY = customY;
    }

    public enum LEVEL {
        INFO,
        DEBUG,
        WARN,
        ERROR;

        public String getNotificationTranslationKey() {
            return switch (this) {
                case INFO -> "notification.level.info";
                case DEBUG -> "notification.level.debug";
                case WARN -> "notification.level.warn";
                case ERROR -> "notification.level.error";
            };
        }
    }

    public static class Builder {
        private final MutableText text;
        private int duration = 60;
        private MutableText parentText = Text.translatable("notification.parent.missing");
        private int priorityValue = 1;
        private LEVEL level = LEVEL.INFO;
        private int customX = 0;
        private int customY = 0;

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

        public Builder customPosition(int customX, int customY) {
            this.customX = customX;
            this.customY = customY;
            return this;
        }

        public Notification build() {
            Notification notification = new Notification(text, duration);
            notification.setParentText(parentText);
            notification.setPriority(priorityValue);
            notification.setInitialOnScreenDecay(duration);
            notification.setLevel(level);
            notification.setCustomX(customX);
            notification.setCustomY(customY);
            return notification;
        }
    }
}
