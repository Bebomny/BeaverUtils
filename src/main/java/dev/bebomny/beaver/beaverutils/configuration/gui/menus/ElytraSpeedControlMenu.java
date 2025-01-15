package dev.bebomny.beaver.beaverutils.configuration.gui.menus;

import dev.bebomny.beaver.beaverutils.configuration.ConfigurationMenu;
import dev.bebomny.beaver.beaverutils.features.features.ElytraSpeedControl;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.tooltip.Tooltip;
import net.minecraft.client.gui.widget.*;
import net.minecraft.screen.ScreenTexts;
import net.minecraft.text.Text;

public class ElytraSpeedControlMenu extends OptionsMenu{

    public ElytraSpeedControlMenu() {
        super(Text.of("Elytra Speed Control Options Menu"));

        this.parent = null;
    }

    //TODO: todo

    @Override
    protected void initWidgets() {
        GridWidget gridWidget = new GridWidget();
        gridWidget.getMainPositioner().marginX(5).marginBottom(4).alignHorizontalCenter();

        GridWidget.Adder adder = gridWidget.createAdder(1);

        //Speed Increment
        Text speedIncrementText = Text.of("§fSpeed Increment per Tick - Recommended to leave at §c0.05");
        adder.add(new TextWidget(
                -(this.textRenderer.getWidth(speedIncrementText)/2),
                ConfigurationMenu.getYPosition(1),
                this.textRenderer.getWidth(speedIncrementText), 20,
                speedIncrementText, this.textRenderer));
        adder.add(this.createSpeedIncrementWidget(0, ConfigurationMenu.getYPosition(2)));

        //Control Boost
        adder.add(this.createCtrlBoostSpeedIncrementWidget(0, ConfigurationMenu.getYPosition(3)));

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

        //Speed Increment Text
        //context.drawCenteredTextWithShadow(this.textRenderer, Text.of("§fSpeed Increment per Tick - Recommended to leave at §c0.05"), this.width/2, ConfigurationMenu.getYPosition(3), 0xFF << 24);

        //For now leave it
        //context.drawCenteredTextWithShadow(this.textRenderer, Text.of("§l§cCurrently nothing to see here, yet"), this.width/2, ConfigurationMenu.getYPosition(7), 0xff << 24);

        super.render(context, mouseX, mouseY, delta);
    }

    private AxisGridWidget createSpeedIncrementWidget(int centerX, int y) {
        AxisGridWidget axisGridWidget = new AxisGridWidget(174, ConfigurationMenu.STANDARD_HEIGHT, AxisGridWidget.DisplayAxis.HORIZONTAL);

        ElytraSpeedControl elytraSpeedControl = beaverUtilsClient.getFeatures().elytraSpeedControl;

        ButtonWidget displayWidget = ButtonWidget.builder(
                Text.of("SpeedIncrement: " + elytraSpeedControl.getNormalSpeedIncrement()),
                button -> {
                    elytraSpeedControl.setSpeedIncrement(0.05f);
                    button.setMessage(Text.of("SpeedIncrement: " + elytraSpeedControl.getNormalSpeedIncrement()));
                }
        ).width(130).build();

        ButtonWidget plusButton = ButtonWidget.builder(
                Text.of("§l+"),
                button -> {
                    elytraSpeedControl.setSpeedIncrement(elytraSpeedControl.getNormalSpeedIncrement() + 0.001f);
                    displayWidget.setMessage(Text.of("SpeedIncrement: " + elytraSpeedControl.getNormalSpeedIncrement()));
                }
        ).size(20, 20).build();

        ButtonWidget minusButton = ButtonWidget.builder(
                Text.of("§l-"),
                button -> {
                    elytraSpeedControl.setSpeedIncrement(elytraSpeedControl.getNormalSpeedIncrement() - 0.001f);
                    displayWidget.setMessage(Text.of("SpeedIncrement: " + elytraSpeedControl.getNormalSpeedIncrement()));
                }
        ).size(20, 20).build();

        axisGridWidget.add(displayWidget);
        axisGridWidget.add(plusButton);
        axisGridWidget.add(minusButton);

        axisGridWidget.refreshPositions();

        axisGridWidget.setPosition(centerX - axisGridWidget.getWidth()/2, y);

        return axisGridWidget;
    }

    private AxisGridWidget createCtrlBoostSpeedIncrementWidget(int centerX, int y) {
        AxisGridWidget ctrlBoostVerticalAxisGridWidget = new AxisGridWidget(
                174,
                ConfigurationMenu.STANDARD_HEIGHT * 2 + ConfigurationMenu.SPACING,
                AxisGridWidget.DisplayAxis.VERTICAL);

        ElytraSpeedControl elytraSpeedControl = beaverUtilsClient.getFeatures().elytraSpeedControl;

        Tooltip enableTooltip = Tooltip.of(Text.of(String.format(
                "If enabled, when pressing %s provides a large boost to speed increments",
                client.options.sprintKey.getDefaultKey().getLocalizedText().getLiteralString())));

        ButtonWidget enableWidget = ButtonWidget.builder(
                Text.of("Control Boost: " + (elytraSpeedControl.isCtrlBoostEnabled() ? "Enabled" : "Disabled")),
                button -> {
                    elytraSpeedControl.setCtrlBoostState(!elytraSpeedControl.isCtrlBoostEnabled());
                    button.setMessage(Text.of("Control Boost: " + (elytraSpeedControl.isCtrlBoostEnabled() ? "Enabled" : "Disabled")));
                }
        ).width(174).tooltip(enableTooltip).build();

        AxisGridWidget boostSpeedAxisGridWidget = new AxisGridWidget(174, ConfigurationMenu.STANDARD_HEIGHT, AxisGridWidget.DisplayAxis.HORIZONTAL);

        ButtonWidget displayWidget = ButtonWidget.builder(
                Text.of("Boost Speed: " + String.format("%.2f", elytraSpeedControl.getBoostSpeedIncrement())),
                button -> {
                    elytraSpeedControl.setBoostSpeedIncrement(0.12f);
                    button.setMessage(Text.of("Boost Speed: " + String.format("%.2f", elytraSpeedControl.getBoostSpeedIncrement())));
                }
        ).width(130).build();

        ButtonWidget plusButton = ButtonWidget.builder(
                Text.of("§l+"),
                button -> {
                    elytraSpeedControl.setBoostSpeedIncrement(elytraSpeedControl.getBoostSpeedIncrement() + 0.01f);
                    displayWidget.setMessage(Text.of("Boost Speed: " + String.format("%.2f", elytraSpeedControl.getBoostSpeedIncrement())));
                }
        ).size(20, 20).build();

        ButtonWidget minusButton = ButtonWidget.builder(
                Text.of("§l-"),
                button -> {
                    elytraSpeedControl.setBoostSpeedIncrement(elytraSpeedControl.getBoostSpeedIncrement() - 0.01f);
                    displayWidget.setMessage(Text.of("Boost Speed: " + String.format("%.2f", elytraSpeedControl.getBoostSpeedIncrement())));
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
