package dev.bebomny.beaver.beaverutils.helpers;

import dev.bebomny.beaver.beaverutils.client.BeaverUtilsClient;
import net.minecraft.text.OrderedText;
import net.minecraft.text.Text;

public class Notification {

    private final Text text;
    private final float offset;

    private final int duration;

    public Notification(Text text, int duration) {
        this.text = text;
        this.duration = duration;
        //this.offset = BeaverUtilsClient.getInstance().client.textRenderer.getWidth(text.toString()) / 2f;
        OrderedText orderedText = text.asOrderedText();
        this.offset = BeaverUtilsClient.getInstance().client.advanceValidatingTextRenderer.getWidth(getText())/2f;
    }

    public Notification(Text text) {
        this.text = text;
        this.duration = 60; //ticks
        this.offset = BeaverUtilsClient.getInstance().client.advanceValidatingTextRenderer.getWidth(getText())/2f;
    }

    public Text getText() {
        return text;
    }

    public float getOffset() {
        return offset;
    }

    public int getDuration() {
        return duration;
    }
}
