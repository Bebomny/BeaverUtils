package dev.bebomny.beaver.beaverutils.configuration.gui.menus;

import dev.bebomny.beaver.beaverutils.configuration.ConfigurationMenu;
import dev.bebomny.beaver.beaverutils.configuration.config.FlightConfig;
import dev.bebomny.beaver.beaverutils.features.features.Flight;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.screen.GameMenuScreen;
import net.minecraft.client.gui.tooltip.Tooltip;
import net.minecraft.client.gui.widget.*;
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
    protected void initWidgets() {
        GridWidget gridWidget = new GridWidget();
        gridWidget.getMainPositioner().marginX(5).marginBottom(4).alignHorizontalCenter();

        GridWidget.Adder adder = gridWidget.createAdder(1);

        //Speed
        Text speedText = Text.of("§fFlight Speed - by default 0.05");
        adder.add(new TextWidget(
                -(this.textRenderer.getWidth(speedText)/2),
                ConfigurationMenu.getYPosition(1),
                this.textRenderer.getWidth(speedText), 20,
                speedText, this.textRenderer));
        adder.add(this.createSpeedIncrementWidget(0, ConfigurationMenu.getYPosition(2)));

        //Control Boost
        adder.add(this.createCtrlBoostWidget(0, ConfigurationMenu.getYPosition(3)));

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
        //context.drawCenteredTextWithShadow(this.textRenderer, Text.of("§fFlight Speed - by default 0.05"), this.width/2, ConfigurationMenu.getYPosition(3), 0xFF << 24);

        //context.drawCenteredTextWithShadow(this.textRenderer, Text.of("§l§cCurrently nothing to see here, yet"), this.width/2, ConfigurationMenu.getYPosition(6), 0xff << 24);

        super.render(context, mouseX, mouseY, delta);
    }

    private AxisGridWidget createSpeedIncrementWidget(int centerX, int y) {
        AxisGridWidget axisGridWidget = new AxisGridWidget(124, ConfigurationMenu.STANDARD_HEIGHT, AxisGridWidget.DisplayAxis.HORIZONTAL);

        Flight flight = beaverUtilsClient.getFeatures().flight;

        ButtonWidget displayWidget = ButtonWidget.builder(
                Text.of("Speed: " + String.format("%.2f", flight.getNormalFlightSpeed())),
                button -> {
                    flight.setNormalFlightSpeed(0.05f);
                    button.setMessage(Text.of("Speed: " + String.format("%.2f", flight.getNormalFlightSpeed())));
                }
        ).width(80).build();

        ButtonWidget plusButton = ButtonWidget.builder(
                Text.of("§l+"),
                button -> {
                    flight.setNormalFlightSpeed(flight.getNormalFlightSpeed() + 0.001f);
                    displayWidget.setMessage(Text.of("Speed: " + String.format("%.2f", flight.getNormalFlightSpeed())));
                }
        ).size(20, 20).build();

        ButtonWidget minusButton = ButtonWidget.builder(
                Text.of("§l-"),
                button -> {
                    flight.setNormalFlightSpeed(flight.getNormalFlightSpeed() - 0.001f);
                    displayWidget.setMessage(Text.of("Speed: " + String.format("%.2f", flight.getNormalFlightSpeed())));
                }
        ).size(20, 20).build();

        axisGridWidget.add(displayWidget);
        axisGridWidget.add(plusButton);
        axisGridWidget.add(minusButton);

        axisGridWidget.refreshPositions();

        axisGridWidget.setPosition(centerX - axisGridWidget.getWidth()/2, y);

        return axisGridWidget;
    }

    private AxisGridWidget createCtrlBoostWidget(int centerX, int y) {
        AxisGridWidget ctrlBoostVerticalAxisGridWidget = new AxisGridWidget(
                174,
                ConfigurationMenu.STANDARD_HEIGHT * 2 + ConfigurationMenu.SPACING,
                AxisGridWidget.DisplayAxis.VERTICAL);

        Flight flight = beaverUtilsClient.getFeatures().flight;

        Tooltip enableTooltip = Tooltip.of(Text.of(String.format(
                "If enabled, when pressing %s provides a large boost to flight speed",
                client.options.sprintKey.getDefaultKey().getLocalizedText().getLiteralString())));

        ButtonWidget enableWidget = ButtonWidget.builder(
                Text.of("Control Boost: " + (flight.isCtrlBoostEnabled() ? "Enabled" : "Disabled")),
                button -> {
                    flight.setCtrlBoostState(!flight.isCtrlBoostEnabled());
                    button.setMessage(Text.of("Control Boost: " + (flight.isCtrlBoostEnabled() ? "Enabled" : "Disabled")));
                }
        ).width(174).tooltip(enableTooltip).build();

        AxisGridWidget boostSpeedAxisGridWidget = new AxisGridWidget(174, ConfigurationMenu.STANDARD_HEIGHT, AxisGridWidget.DisplayAxis.HORIZONTAL);

        ButtonWidget displayWidget = ButtonWidget.builder(
                Text.of("Boost Speed: " + String.format("%.2f", flight.getBoostFlightSpeed())),
                button -> {
                    flight.setBoostFlightSpeed(0.12f);
                    button.setMessage(Text.of("Boost Speed: " + String.format("%.2f", flight.getBoostFlightSpeed())));
                }
        ).width(130).build();

        ButtonWidget plusButton = ButtonWidget.builder(
                Text.of("§l+"),
                button -> {
                    flight.setBoostFlightSpeed(flight.getBoostFlightSpeed() + 0.01f);
                    displayWidget.setMessage(Text.of("Boost Speed: " + String.format("%.2f", flight.getBoostFlightSpeed())));
                }
        ).size(20, 20).build();

        ButtonWidget minusButton = ButtonWidget.builder(
                Text.of("§l-"),
                button -> {
                    flight.setBoostFlightSpeed(flight.getBoostFlightSpeed() - 0.01f);
                    displayWidget.setMessage(Text.of("Boost Speed: " + String.format("%.2f", flight.getBoostFlightSpeed())));
                }
        ).size(20, 20).build();

        boostSpeedAxisGridWidget.add(displayWidget);
        boostSpeedAxisGridWidget.add(plusButton);
        boostSpeedAxisGridWidget.add(minusButton);

        boostSpeedAxisGridWidget.refreshPositions();

        ctrlBoostVerticalAxisGridWidget.add(enableWidget);
        ctrlBoostVerticalAxisGridWidget.add(boostSpeedAxisGridWidget);

        ctrlBoostVerticalAxisGridWidget.refreshPositions();

        ctrlBoostVerticalAxisGridWidget.setPosition(centerX - ctrlBoostVerticalAxisGridWidget.getWidth()/2, y);

        return ctrlBoostVerticalAxisGridWidget;
    }
}
