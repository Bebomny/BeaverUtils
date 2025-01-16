package dev.bebomny.beaver.beaverutils.features.features.autoclicker;

import net.minecraft.client.font.TextRenderer;
import net.minecraft.client.gui.widget.AxisGridWidget;

public interface ClickAction {
    void performAction(long ticksPast);

    AxisGridWidget createMouseActionClickControlSubMenu(int centerX, int y, TextRenderer textRenderer);
}
