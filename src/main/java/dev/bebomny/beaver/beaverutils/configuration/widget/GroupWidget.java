package dev.bebomny.beaver.beaverutils.configuration.widget;

import dev.bebomny.beaver.beaverutils.configuration.widget.ListWidget;
import dev.bebomny.beaver.beaverutils.configuration.widget.ListWidget.Entry;
import dev.bebomny.beaver.beaverutils.helpers.BeaverDrawContext;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.Element;
import net.minecraft.client.sound.PositionedSoundInstance;
import net.minecraft.sound.SoundEvents;
import net.minecraft.text.Text;

import java.util.ArrayList;
import java.util.List;

public class GroupWidget extends Entry {

    protected final MinecraftClient client = MinecraftClient.getInstance();
    public final String id;
    public final Text text;
    public final List<EntryWidget> children = new ArrayList<>();
    public boolean collapsed = false;

    public GroupWidget(String id, Text text) {
        this.id = id;
        this.text = text;
    }

    @Override
    public void render(DrawContext drawContext, int index, int x, int y, int width, int height, int mouseX, int mouseY, boolean hovered, float delta) {
        BeaverDrawContext context = BeaverDrawContext.wrap(drawContext);
        context.drawCenteredTextWithShadow(text, x + width/2, y + 3, -1);

        String collapse = collapsed ? "[+]" : "[-]";
        int cx = x + width / 2 - client.textRenderer.getWidth(text) / 2 - 20;
        context.drawTextWithShadow(Text.literal(collapse), cx, y + 3, -1);

    }

    public boolean isCollapsed() {
        return collapsed;
    }

    @Override
    public int getHeight() {
        for(EntryWidget entryWidget : children) {
            if(entryWidget.isVisible()) {
                return 20;
            }
        }
        return 0;
    }

    @Override
    public boolean mouseClicked(double mouseX, double mouseY, int button) {
        if(isMouseOver(mouseX, mouseY)) {
            collapsed = !collapsed;
            MinecraftClient.getInstance().getSoundManager().play(PositionedSoundInstance.master(SoundEvents.UI_BUTTON_CLICK, 1.0f));
            return true;
        }
        return false;
    }

    @Override
    public List<? extends Element> children() {
        return List.of();
    }
}
