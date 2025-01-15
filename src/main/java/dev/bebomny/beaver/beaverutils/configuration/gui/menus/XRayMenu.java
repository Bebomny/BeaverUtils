package dev.bebomny.beaver.beaverutils.configuration.gui.menus;

import dev.bebomny.beaver.beaverutils.configuration.ConfigurationMenu;
import dev.bebomny.beaver.beaverutils.configuration.config.XRayConfig;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.client.gui.widget.GridWidget;
import net.minecraft.client.gui.widget.SimplePositioningWidget;
import net.minecraft.client.gui.widget.TextWidget;
import net.minecraft.screen.ScreenTexts;
import net.minecraft.text.Text;

public class XRayMenu extends OptionsMenu{

    private final XRayConfig xRayConfig;

    public XRayMenu() {
        super(Text.of("XRay Options Menu"));

        this.parent = null;
        this.xRayConfig = beaverUtilsClient.getConfig().xRayConfig;
    }

    //TODO: todo

    @Override
    protected void initWidgets() {
        GridWidget gridWidget = new GridWidget();
        gridWidget.getMainPositioner().marginX(5).marginBottom(4).alignHorizontalCenter();

        GridWidget.Adder adder = gridWidget.createAdder(2);

        //Nothing to see here text widget
        Text nothingToSeeHereText = Text.of("§l§cCurrently nothing to see here, yet");
        adder.add(new TextWidget(
                -(this.textRenderer.getWidth(nothingToSeeHereText)/2),
                ConfigurationMenu.getYPosition(1),
                this.textRenderer.getWidth(nothingToSeeHereText), 20,
                nothingToSeeHereText, this.textRenderer));

        //Add a DONE button
        adder.add(
                ButtonWidget.builder(
                        ScreenTexts.DONE,
                        button -> {
                            this.close();
                            beaverUtilsClient.configHandler.saveConfig();
                        }
                ).width(200).position(-102, ConfigurationMenu.getYPosition(6) + 14).build(),
                2, adder.copyPositioner().marginTop(6)
        );

        SimplePositioningWidget.setPos(
                gridWidget,
                0, this.height / 6 - 12,
                this.width, this.height,
                0.5f, 0.05f
        );

        //Adding the grid to the screen
        gridWidget.forEachChild(this::addDrawableChild);
    }

    @Override
    public void render(DrawContext context, int mouseX, int mouseY, float delta) {
        //Add a list with all blocks

        //context.drawCenteredTextWithShadow(this.textRenderer, Text.of("§l§cCurrently nothing to see here, yet"), this.width/2, ConfigurationMenu.getYPosition(6), 0xff << 24);

        super.render(context, mouseX, mouseY, delta);
    }
}
