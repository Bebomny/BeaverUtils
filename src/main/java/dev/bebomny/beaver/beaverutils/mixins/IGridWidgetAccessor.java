package dev.bebomny.beaver.beaverutils.mixins;

import net.minecraft.client.gui.widget.ClickableWidget;
import net.minecraft.client.gui.widget.GridWidget;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

import java.util.List;

@Mixin(GridWidget.class)
public interface IGridWidgetAccessor {
    @Accessor
    List<ClickableWidget> getChildren();
}
