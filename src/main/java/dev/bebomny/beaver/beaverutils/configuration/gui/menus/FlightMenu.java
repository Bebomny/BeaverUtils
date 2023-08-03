package dev.bebomny.beaver.beaverutils.configuration.gui.menus;

import dev.bebomny.beaver.beaverutils.configuration.ConfigurationMenu;
import dev.bebomny.beaver.beaverutils.configuration.config.FlightConfig;
import dev.bebomny.beaver.beaverutils.features.features.AutoPlant;
import dev.bebomny.beaver.beaverutils.features.features.Flight;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.widget.AxisGridWidget;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.client.gui.widget.GridWidget;
import net.minecraft.client.gui.widget.SimplePositioningWidget;
import net.minecraft.screen.ScreenTexts;
import net.minecraft.text.Text;

public class FlightMenu extends OptionsMenu{

    private final FlightConfig flightConfig;

    public FlightMenu() {
        super(Text.of("Flight Options Menu"));

        this.parent = null;
        this.flightConfig = beaverUtilsClient.getConfig().flightConfig;
    }

    //TODO: todo

    @Override
    protected void init() {
        GridWidget gridWidget = new GridWidget();
        gridWidget.getMainPositioner().marginX(5).marginBottom(4).alignHorizontalCenter();

        GridWidget.Adder adder = gridWidget.createAdder(1);

        //Speed
        adder.add(this.createSpeedIncrementWidget(0, ConfigurationMenu.getYPosition(2)));

        //Add a DONE button
        adder.add(
                ButtonWidget.builder(
                        ScreenTexts.DONE,
                        button -> {
                            this.close();
                            beaverUtilsClient.configHandler.saveConfig();
                        }
                ).width(200).position(-102, ConfigurationMenu.getYPosition(9)).build(),
                2, adder.copyPositioner().marginTop(6)
        );

        SimplePositioningWidget.setPos(
                gridWidget,
                0, 40, //this.height / 6 - 12
                this.width, this.height,
                0.5f, 0.00f //0.05f
        );

        //Adding the grid to the screen
        gridWidget.forEachChild(this::addDrawableChild);
    }

    @Override
    public void render(DrawContext context, int mouseX, int mouseY, float delta) {

        //Speed
        context.drawCenteredTextWithShadow(this.textRenderer, Text.of("§fFlight Speed - by default 0.05"), this.width/2, ConfigurationMenu.getYPosition(3), 0xFF << 24);

        context.drawCenteredTextWithShadow(this.textRenderer, Text.of("§l§cCurrently nothing to see here, yet"), this.width/2, ConfigurationMenu.getYPosition(6), 0xff << 24);

        super.render(context, mouseX, mouseY, delta);
    }

    private AxisGridWidget createSpeedIncrementWidget(int centerX, int y) {
        AxisGridWidget axisGridWidget = new AxisGridWidget(124, ConfigurationMenu.STANDARD_HEIGHT, AxisGridWidget.DisplayAxis.HORIZONTAL);

        Flight flight = beaverUtilsClient.getFeatures().flight;

        ButtonWidget displayWidget = ButtonWidget.builder(
                Text.of("Speed: " + flight.getFlightSpeed()),
                button -> {
                    flight.setFlightSpeed(0.05f);
                    button.setMessage(Text.of("Speed: " + flight.getFlightSpeed()));
                }
        ).width(80).build();

        ButtonWidget plusButton = ButtonWidget.builder(
                Text.of("§l+"),
                button -> {
                    flight.setFlightSpeed(flight.getFlightSpeed() + 0.001f);
                    displayWidget.setMessage(Text.of("Speed: " + flight.getFlightSpeed()));
                }
        ).size(20, 20).build();

        ButtonWidget minusButton = ButtonWidget.builder(
                Text.of("§l-"),
                button -> {
                    flight.setFlightSpeed(flight.getFlightSpeed() - 0.001f);
                    displayWidget.setMessage(Text.of("Speed: " + flight.getFlightSpeed()));
                }
        ).size(20, 20).build();

        axisGridWidget.add(displayWidget);
        axisGridWidget.add(plusButton);
        axisGridWidget.add(minusButton);

        axisGridWidget.refreshPositions();

        axisGridWidget.setPosition(centerX - axisGridWidget.getWidth()/2, y);

        return axisGridWidget;
    }
}
