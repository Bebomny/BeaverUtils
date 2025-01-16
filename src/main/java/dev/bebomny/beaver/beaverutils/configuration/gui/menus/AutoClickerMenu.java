package dev.bebomny.beaver.beaverutils.configuration.gui.menus;

import dev.bebomny.beaver.beaverutils.client.BeaverUtilsClient;
import dev.bebomny.beaver.beaverutils.configuration.ConfigurationMenu;
import dev.bebomny.beaver.beaverutils.features.features.AutoClicker;
import dev.bebomny.beaver.beaverutils.features.features.autoclicker.ClickAction;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.client.gui.widget.GridWidget;
import net.minecraft.client.gui.widget.SimplePositioningWidget;
import net.minecraft.screen.ScreenTexts;
import net.minecraft.text.Text;

public class AutoClickerMenu extends OptionsMenu {


    public AutoClickerMenu() {
        super(Text.of("AutoClicker Options Menu"));

        this.parent = null;
    }

    @Override
    protected void initWidgets() {
        GridWidget gridWidget = new GridWidget();
        gridWidget.getMainPositioner().marginX(5).marginBottom(4).alignHorizontalCenter();

        GridWidget.Adder adder = gridWidget.createAdder(1);

        AutoClicker autoClicker = BeaverUtilsClient.getInstance().features.autoClicker;

        int actionCounter = 1;
        for(ClickAction clickAction : autoClicker.getMouseClickActions()) {
            adder.add(clickAction.createMouseActionClickControlSubMenu(0, ConfigurationMenu.getYPosition(4 * actionCounter) + (10 * actionCounter), this.textRenderer));
            actionCounter++;
        }

        //Add a DONE button
        adder.add(
                ButtonWidget.builder(
                        ScreenTexts.DONE,
                        button -> {
                            this.close();
                            beaverUtilsClient.configHandler.saveConfig();
                        }
                ).width(200).position(-102, ConfigurationMenu.getYPosition(13)).build(),
                2, adder.copyPositioner().marginTop(6)
        );

        SimplePositioningWidget.setPos(
                gridWidget,
                0, 40, //this.height / 6 - 12
                this.width, this.height,
                0.5f, 0.00f //0.05f
        );

        gridWidget.forEachChild(this::addDrawableChild);
    }
}
