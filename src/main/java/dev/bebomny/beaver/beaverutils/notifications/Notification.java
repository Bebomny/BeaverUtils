package dev.bebomny.beaver.beaverutils.notifications;

import dev.bebomny.beaver.beaverutils.client.BeaverUtilsClient;
import net.minecraft.text.Text;
import org.jetbrains.annotations.Nullable;

import java.util.Objects;


public class Notification {
    private final String text;
    private final int duration; //in ticks
    private int customXOffset;
    private Categories category;
    @Nullable
    private String customCategory;
    private String callerClassName;

    public static Builder builder(String text) {
        return new Builder(text);
    }

    protected Notification(String text, int duration) {
        this.text = text;
        this.duration = duration;
    }

    public void setCustomXOffset(int customXOffset) {
        this.customXOffset = customXOffset;
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

    public String getText() {
        return text;
    } //return category == null ? text : getTextWithCategory();

    public int getDuration() {
        return duration;
    }

    public float getCustomXOffset() {
        return customXOffset;
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

    private Text getTextWithCategory() { //useless for now
        return category == Categories.CUSTOM ?
                Text.of("§6§l[" + customCategory +"§6§l]" + " " + text)
                :
                Text.of("§6§l[" + category +"§6§l]" + " " + text);
    }

    public static class Builder {
        private final String text;
        private int duration = 60; //in ticks // default - 60
        private int customXOffset;
        @Nullable
        private Categories category;
        private String callerClassName;

        @Nullable
        private String customCategory;

        public Builder(String text) {
            this.text = text;
        }

        public Builder duration(int duration) {
            this.duration = duration;
            return this;
        }

        /**
         * Allows to set a category for the notification, by default is set to {@code Categories.NONE}.
         * The second parameter must be set if the category selected is {@code Categories.STATE} otherwise can be {@code null}
         * @param category a category under which the notification will be displayed
         * @param callerName The name to be displayed as the callerClassName
         * @return {@code this}
         */
        public Builder category(Categories category, @Nullable String callerName) {
            this.category = category;
            this.callerClassName = callerName;
            return this;
        }

        public Builder customCategory(String customCategory) {
            this.category = Categories.CUSTOM;
            this.customCategory = customCategory;
            return this;
        }

        public Builder customXOffset(int customXOffset) {
            this.customXOffset = customXOffset;
            return this;
        }

        public Notification build() {
            Notification notification = new Notification(text, duration);
            notification.setCustomXOffset(customXOffset);
            notification.setCategory(Objects.requireNonNullElse(category, Categories.NONE)); //this is cool
            notification.setCustomCategory(customCategory);
            notification.setCallerClassName(callerClassName); //Thread.currentThread().getStackTrace()[2].getClassName()
            return notification;
        }

    }

}
