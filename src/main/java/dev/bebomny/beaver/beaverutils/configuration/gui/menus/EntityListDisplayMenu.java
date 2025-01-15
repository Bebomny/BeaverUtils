package dev.bebomny.beaver.beaverutils.configuration.gui.menus;

import dev.bebomny.beaver.beaverutils.configuration.ConfigurationMenu;
import dev.bebomny.beaver.beaverutils.configuration.config.EntityListDisplayConfig;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.widget.TextWidget;
import net.minecraft.text.Text;

public class EntityListDisplayMenu extends OptionsMenu{

    public final EntityListDisplayConfig entityListDisplayConfig;
    public EntityListDisplayMenu() {
        super(Text.of("Entity List Display Options Menu"));

        this.parent = null;
        this.entityListDisplayConfig = beaverUtilsClient.getConfig().entityListDisplayConfig;
    }

    @Override
    protected void initWidgets() {
        TextWidget nothingToSeeHereWidget = new TextWidget(
                0, ConfigurationMenu.getYPosition(6),
                this.width, 9,
                Text.of("§l§cCurrently nothing to see here, yet"),
                this.textRenderer);
        this.addDrawableChild(nothingToSeeHereWidget);
    }

    @Override
    public void render(DrawContext context, int mouseX, int mouseY, float delta) {
        //context.drawCenteredTextWithShadow(this.textRenderer, Text.of("§l§cCurrently nothing to see here, yet"), this.width/2, ConfigurationMenu.getYPosition(6), 0xff << 24);

        super.render(context, mouseX, mouseY, delta);
    }
}
