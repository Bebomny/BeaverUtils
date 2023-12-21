package dev.bebomny.beaver.beaverutils.configuration.widget;

import dev.bebomny.beaver.beaverutils.configuration.annotations.ConfigGroup;
import dev.bebomny.beaver.beaverutils.helpers.BeaverDrawContext;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.Drawable;
import net.minecraft.client.gui.Element;
import net.minecraft.client.gui.tooltip.TooltipComponent;
import net.minecraft.text.Text;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Supplier;

public abstract class EntryWidget extends ListWidget.Entry {
    private final Text name;
    private final List<TooltipComponent> tooltip;
    protected final Supplier<String> search;
    private final int height;
    public ConfigGroup group;
    public boolean endGroup = false;
    private List<? extends Element> children = List.of();
    public List<GroupWidget> parentGroups = new ArrayList<>();


    public EntryWidget(Text name, List<TooltipComponent> tooltip, Supplier<String> search, int height) {
        this.name = name;
        this.tooltip = tooltip;
        this.search = search;
        this.height = height;
    }

    public void setChildren(List<? extends Element> newChildren) {
        this.children = newChildren;
    }

    public void updateDimensions(int x, int y, int width, int height) {

    }

    @Override
    public void render(DrawContext drawContext, int index, int x, int y, int width, int height, int mouseX, int mouseY, boolean hovered, float delta) {
        BeaverDrawContext context = BeaverDrawContext.wrap(drawContext);
        if(group != null) {
            context.fill(x + 4, y + height/2 - 1, 6, 2, 0xffffffff);
            if (endGroup) {
                context.fill(x + 2, y - 4, 2, height / 2 + 5, 0xffffffff);
            } else {
                context.fill(x + 2, y - 4, 2, height + 4, 0xffffffff);
            }
            x += 10;
            width -= 10;
        }

        updateDimensions(x, y, width, height);
        context.fill(x, y, width, height, 0x66000000);
        context.drawTextWithShadow(this.name, x + 6, y + 10 - (parentList.client.textRenderer.fontHeight / 2), 0xFFFFFF);
        for(Element element : children()) {
            if(element instanceof Drawable drawable) {
                drawable.render(drawContext, mouseX, mouseY, delta);
            }
        }
    }

    @Override
    public List<TooltipComponent> getTootTip(int mouseX, int mouseY) {
        return this.tooltip;
    }

    public String getSearchableText() {
        return name.toString();
    }

    public boolean isParentVisible() {
        for(GroupWidget groupWidget : parentGroups) {
            if(groupWidget.isCollapsed()) {
                return false;
            }
        }
        return true;
    }

    public boolean isVisible() {
        String s = search.get().toLowerCase();
        if(getSearchableText().toLowerCase().contains(s)) {
            return true;
        }
        for(GroupWidget groupWidget : parentGroups) {
            if(groupWidget.toString().toLowerCase().contains(s)) {
                return true;
            }
        }
        return false;
    }



    @Override
    public int getHeight() {
        if(isParentVisible() && isVisible()) {
            return height;
        }
        return 0;
    }

    @Override
    public List<? extends Element> children() {
        return children;
    }
}
