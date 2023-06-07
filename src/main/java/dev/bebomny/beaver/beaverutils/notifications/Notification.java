package dev.bebomny.beaver.beaverutils.notifications;

import dev.bebomny.beaver.beaverutils.client.BeaverUtilsClient;
import net.minecraft.text.Text;
import org.jetbrains.annotations.Nullable;


public class Notification {
    private final Text text;
    private final int duration; //in ticks
    private final float offset;
    private Categories category;
    @Nullable
    private String customCategory;
    private String callerClassName;

    public static Builder builder(Text text) {
        return new Builder(text);
    }

    protected Notification(Text text, int duration) {
        this.text = text;
        this.duration = duration;
        this.offset = BeaverUtilsClient.getInstance().client.advanceValidatingTextRenderer.getWidth(getText())/2f;
    }

    public void setCategory(Categories category) {
        this.category = category;
    }

    public void setCustomCategory(@Nullable String customCategory) {
        this.customCategory = customCategory;
    }

    private void setCallerClassName(String callerClassName) {
        this.callerClassName = callerClassName;
    }

    public Text getText() {
        return category == null ? text : getTextWithCategory();
    }

    public int getDuration() {
        return duration;
    }

    public float getOffset() {
        return offset;
    }

    public Categories getCategory() {
        return category;
    }

    public @Nullable String getCustomCategory() {
        return customCategory;
    }

    public String getCallerClassName() {
        return callerClassName;
    }

    private Text getTextWithCategory() {
        return category == Categories.CUSTOM ?
                Text.literal("§6§l[" + customCategory +"§6§l]" + " " + text)
                :
                Text.literal("§6§l[" + category +"§6§l]" + " " + text);
    }

    public static class Builder {
        private final Text text;
        private int duration = 60; //in ticks // default - 60
        private Categories category;
        private String callerClassName;

        @Nullable
        private String customCategory;

        public Builder(Text text) {
            this.text = text;
        }

        public Builder duration(int duration) {
            this.duration = duration;
            return this;
        }

        public Builder category(Categories category) {
            this.category = category;
            return this;
        }

        public Builder setCustomCategory(String customCategory) {
            this.category = Categories.CUSTOM;
            this.customCategory = customCategory;
            return this;
        }


        public Notification build() {
            Notification notification = new Notification(text, duration);
            notification.setCategory(category);
            notification.setCustomCategory(customCategory);
            notification.setCallerClassName(Thread.currentThread().getStackTrace()[2].getClassName());
            return notification;
        }

    }

}
