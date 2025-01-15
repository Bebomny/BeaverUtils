package dev.bebomny.beaver.beaverutils.configuration.gui.menus;

import dev.bebomny.beaver.beaverutils.configuration.ConfigurationMenu;
import dev.bebomny.beaver.beaverutils.configuration.config.CustomRendererConfig;
import dev.bebomny.beaver.beaverutils.features.features.CustomRenderer;
import dev.bebomny.beaver.beaverutils.features.features.ElytraSpeedControl;
import dev.bebomny.beaver.beaverutils.helpers.TextUtils;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.widget.*;
import net.minecraft.screen.ScreenTexts;
import net.minecraft.text.Text;
import net.minecraft.util.math.Vec3d;

public class CustomRendererMenu extends OptionsMenu {

    public final CustomRendererConfig customRendererConfig;

    public CustomRendererMenu() {
        super(Text.of("Custom Renderer Options Menu"));

        this.customRendererConfig = beaverUtilsClient.getConfig().customRendererConfig;
    }

    @Override
    protected void initWidgets() {
//        TextWidget nothingToSeeHereWidget = new TextWidget(
//                0, ConfigurationMenu.getYPosition(6),
//                this.width, 9,
//                Text.of("§l§cCurrently nothing to see here, yet"),
//                this.textRenderer);
//        this.addDrawableChild(nothingToSeeHereWidget);

        CustomRenderer customRenderer = beaverUtilsClient.getFeatures().customRenderer;

        Vec3d originPosition = customRenderer.getOriginPosition();
        Text textOriginPos = Text.of("§l§aOrigin Position: x: %.1f, y: %.1f, z: %.1f".formatted(originPosition.getX(), originPosition.getY(), originPosition.getZ()));
        TextWidget originPositionWidget = new TextWidget(
                -(client.textRenderer.getWidth(textOriginPos) / 2), ConfigurationMenu.getYPosition(2),
                client.textRenderer.getWidth(textOriginPos), 9,
                textOriginPos,
                this.textRenderer);

        ButtonWidget followTerrainButtonWidget = ButtonWidget.builder(
                Text.of("Follow Terrain " + (customRenderer.doFollowTerrain() ? "Enabled" : "Disabled")),
                button -> {
                    customRenderer.setFollowTerrain(!customRenderer.doFollowTerrain());
                    button.setMessage(Text.of("Follow Terrain " + (customRenderer.doFollowTerrain() ? "Enabled" : "Disabled")));
                }
        ).size(174, 20).position(-(174/2), ConfigurationMenu.getYPosition(4)).build();


        GridWidget gridWidget = new GridWidget();
        gridWidget.getMainPositioner().marginX(5).marginBottom(4).alignHorizontalCenter();

        GridWidget.Adder adder = gridWidget.createAdder(1);

        //Radius/Spacing increment
        adder.add(originPositionWidget);
        adder.add(this.createRadiusOrSpacingIncrementWidget(0, ConfigurationMenu.getYPosition(3)));
        adder.add(followTerrainButtonWidget);

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

        gridWidget.forEachChild(this::addDrawableChild);
    }

    @Override
    public void render(DrawContext context, int mouseX, int mouseY, float delta) {
        //context.drawCenteredTextWithShadow(this.textRenderer, Text.of("§l§cCurrently nothing to see here, yet"), this.width/2, ConfigurationMenu.getYPosition(6), 0xff << 24);

        super.render(context, mouseX, mouseY, delta);
    }

    private AxisGridWidget createRadiusOrSpacingIncrementWidget(int centerX, int y) {
        AxisGridWidget axisGridWidget = new AxisGridWidget(174, ConfigurationMenu.STANDARD_HEIGHT, AxisGridWidget.DisplayAxis.HORIZONTAL);

        CustomRenderer customRenderer = beaverUtilsClient.getFeatures().customRenderer;

        ButtonWidget displayWidget = ButtonWidget.builder(
                Text.of("Radius/Spacing: " + customRenderer.getRadiusOrSpacing()),
                button -> {
                    customRenderer.setRadiusOrSpacing(5);
                    button.setMessage(Text.of("Radius/Spacing: " + customRenderer.getRadiusOrSpacing()));
                }
        ).width(130).build();

        ButtonWidget plusButton = ButtonWidget.builder(
                Text.of("§l+"),
                button -> {
                    customRenderer.setRadiusOrSpacing(customRenderer.getRadiusOrSpacing() + 1);
                    displayWidget.setMessage(Text.of("Radius/Spacing: " + customRenderer.getRadiusOrSpacing()));
                }
        ).size(20, 20).build();

        ButtonWidget minusButton = ButtonWidget.builder(
                Text.of("§l-"),
                button -> {
                    customRenderer.setRadiusOrSpacing(customRenderer.getRadiusOrSpacing() - 1);
                    displayWidget.setMessage(Text.of("Radius/Spacing: " + customRenderer.getRadiusOrSpacing()));
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
