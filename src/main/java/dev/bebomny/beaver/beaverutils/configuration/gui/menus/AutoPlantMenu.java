package dev.bebomny.beaver.beaverutils.configuration.gui.menus;

import dev.bebomny.beaver.beaverutils.configuration.ConfigurationMenu;
import dev.bebomny.beaver.beaverutils.configuration.config.AutoPlantConfig;
import dev.bebomny.beaver.beaverutils.features.features.AutoPlant;
import dev.bebomny.beaver.beaverutils.features.features.ElytraSpeedControl;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.widget.*;
import net.minecraft.screen.ScreenTexts;
import net.minecraft.text.Text;

public class AutoPlantMenu extends OptionsMenu {

    private final AutoPlantConfig autoPlantConfig;

    public AutoPlantMenu() {
        super(Text.of("AutoPlant Options Menu"));

        this.parent = null;
        this.autoPlantConfig = beaverUtilsClient.getConfig().autoPlantConfig;
    }

    //TODO: todo

    @Override
    protected void init() {
        GridWidget gridWidget = new GridWidget();
        gridWidget.getMainPositioner().marginX(5).marginBottom(4).alignHorizontalCenter();

        GridWidget.Adder adder = gridWidget.createAdder(1);

        //Radius
        adder.add(this.createRadiusIncrementWidget(0, ConfigurationMenu.getYPosition(2)));

        //Mode
        adder.add(this.createModeCyclingButtonWidget(0, ConfigurationMenu.getYPosition(4)));

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
        //Maybe add some flowers for this menu here, you know autoPLANT so maybe some fun looking crops?

        //Radius
        context.drawCenteredTextWithShadow(this.textRenderer, Text.of("§fAutoPlant Radius - by default 2"), this.width/2, ConfigurationMenu.getYPosition(3), 0xFF << 24);

        //Mode
        context.drawCenteredTextWithShadow(this.textRenderer, Text.of("§fAutoPlant Mode - by default DontLookAt"), this.width/2, ConfigurationMenu.getYPosition(5), 0xFF << 24);

        context.drawCenteredTextWithShadow(this.textRenderer, Text.of("§l§cCurrently nothing to see here, yet"), this.width/2, ConfigurationMenu.getYPosition(7), 0xff << 24);

        super.render(context, mouseX, mouseY, delta);
    }

    private CyclingButtonWidget<AutoPlant.Mode> createModeCyclingButtonWidget(int centerX, int y) {
        AutoPlant autoPlant = beaverUtilsClient.getFeatures().autoPlant;

        return CyclingButtonWidget.<AutoPlant.Mode>builder(AutoPlant.Mode::getText)
                .values(AutoPlant.Mode.values())
                .initially(AutoPlant.Mode.DONTLOOKAT)
                .build(
                        centerX - 160/2, y,
                        160, 20,
                        Text.of("Mode: "),
                        (button, value) -> {autoPlant.setMode(value);}
                );
    }

    private AxisGridWidget createRadiusIncrementWidget(int centerX, int y) {
        AxisGridWidget axisGridWidget = new AxisGridWidget(124, ConfigurationMenu.STANDARD_HEIGHT, AxisGridWidget.DisplayAxis.HORIZONTAL);

        AutoPlant autoPlant = beaverUtilsClient.getFeatures().autoPlant;

        ButtonWidget displayWidget = ButtonWidget.builder(
                Text.of("Radius: " + autoPlant.getPlantRadius()),
                button -> {
                    autoPlant.setPlantRadius(2);
                    button.setMessage(Text.of("Radius: " + autoPlant.getPlantRadius()));
                }
        ).width(80).build();

        ButtonWidget plusButton = ButtonWidget.builder(
                Text.of("§l+"),
                button -> {
                    autoPlant.setPlantRadius(autoPlant.getPlantRadius() + 1);
                    displayWidget.setMessage(Text.of("Radius: " + autoPlant.getPlantRadius()));
                }
        ).size(20, 20).build();

        ButtonWidget minusButton = ButtonWidget.builder(
                Text.of("§l-"),
                button -> {
                    autoPlant.setPlantRadius(autoPlant.getPlantRadius() - 1);
                    displayWidget.setMessage(Text.of("Radius: " + autoPlant.getPlantRadius()));
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
