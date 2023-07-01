package dev.bebomny.beaver.beaverutils.configuration;

import dev.bebomny.beaver.beaverutils.client.BeaverUtilsClient;
import dev.bebomny.beaver.beaverutils.configuration.config.GeneralConfig;
import dev.bebomny.beaver.beaverutils.configuration.gui.buttons.*;
import dev.bebomny.beaver.beaverutils.helpers.TextUtils;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.tooltip.Tooltip;
import net.minecraft.client.gui.widget.*;
import net.minecraft.screen.ScreenTexts;
import net.minecraft.text.Text;
import net.minecraft.util.math.MathHelper;

public class ConfigurationMenu extends Screen{

    private final Screen parent;
    private final BeaverUtilsClient beaverUtilsClient;
    private final Config config;
    private final GeneralConfig generalConfig;

    final int BUTTON_HEIGHT = 20;
    final int SPACING = 4;

    private float rowCounter;

    public ConfigurationMenu(Screen parent) {
        super(Text.of("BeaverUtils Options"));
        this.parent = parent;
        this.beaverUtilsClient = BeaverUtilsClient.getInstance();
        this.config = beaverUtilsClient.getConfig();
        this.generalConfig = config.generalConfig;
    }

    @Override
    protected void init() {
        //Create a grid widget and configure it appropriately
        GridWidget gridWidget = new GridWidget();
        gridWidget.getMainPositioner().marginX(5).marginBottom(4).alignHorizontalCenter();
        //Create an adder to add buttons to the grid
        GridWidget.Adder adder = gridWidget.createAdder(2);

        //Empty space first(Tp here later or maybe now?) // tp added but not functional
        //adder.add(EmptyWidget.ofHeight(20));
        adder.add(new QuickTeleportButton(-(128 + 4), calculateYPosition(0))); //now make it work

        //Fullbright button
        adder.add(new FullBrightButton(0, calculateYPosition(0)));

        //XrayButton
        adder.add(new XRayButton(-(128 + 4), calculateYPosition(1)));

        //AutoClicker buttons
        adder.add(new AutoClickerButton(0, calculateYPosition(1)));

        //Flight buttons
        adder.add(new FlightButton(-(128 + 4), calculateYPosition(2)));

        //Reach buttons
        adder.add(new ReachButton(0, calculateYPosition(2)));

        //NoFallDamage
        adder.add(new NoFallDmgButton(-(128 + 4), calculateYPosition(3)));

        //InGameStats
        adder.add(new InGameStatsButton(0, calculateYPosition(3)));

        //AutoPlant Buttons
        adder.add(new AutoPlantButton(-(128 + 4), calculateYPosition(4)));

        //Add a DONE button
        adder.add(
                ButtonWidget.builder(
                    ScreenTexts.DONE,
                    button -> {
                        this.client.setScreen(this.parent);
                        beaverUtilsClient.configHandler.saveConfig();
                    }
                ).width(200).position(-102, calculateYPosition(6) + 14).build(),
                2, adder.copyPositioner().marginTop(6)
        );

        //IDK it was in the Minecraft options Class, so it's probably needed xd //ooooh I know what it does now, pretty cool //it is fucked up in this version(1.20.1), not recommended
        //gridWidget.refreshPositions();

        //setting the grid in screen space
        SimplePositioningWidget.setPos(
                gridWidget,
                0, this.height / 6 - 12,
                this.width, this.height,
                0.5f, 0.05f
        );

        //
        //rowCounter = 0;
        //gridWidget.forEachElement(clickableWidget -> clickableWidget.setY(getNextYPosition()));
        //gridWidget.forEachElement(widget -> widget.setX(widget.getX()));

        //Adding the grid to the screen
        gridWidget.forEachChild(this::addDrawableChild);

        Tooltip autoEnableTooltip = Tooltip.of(Text.of("Enables enabled features on config load"));

        ButtonWidget autoEnableButton = ButtonWidget.builder(
                TextUtils.getEnabledDisabledText("AutoEnable", generalConfig.autoEnable),
                button -> {
                    generalConfig.autoEnable = !generalConfig.autoEnable;
                    button.setMessage(TextUtils.getEnabledDisabledText("AutoEnable", generalConfig.autoEnable));
                }
        ).dimensions(20, height - 30, 119, 20).tooltip(autoEnableTooltip).build();

        Tooltip debugTooltip = Tooltip.of(Text.of("§l§a§kSGH §l§aDEBUG §l§a§kSGH §l§adebug §l§aDebug §l§a§kSGH §l§adEbUg §l§a§kSGH §l§fdoesnt do anything xd"));

        ButtonWidget debugButton = ButtonWidget.builder(
                TextUtils.getEnabledDisabledText("Debug", generalConfig.debug),
                button -> {
                    generalConfig.debug = !generalConfig.debug;
                    button.setMessage(TextUtils.getEnabledDisabledText("Debug", generalConfig.debug));
                }
        ).dimensions(20, height - 30 - 4 - 20, 90, 20).tooltip(debugTooltip).build();

        this.addDrawableChild(autoEnableButton);
        this.addDrawableChild(debugButton);
    }

    private int getNextYPosition() {
        rowCounter += 0.5;
        return calculateYPosition(MathHelper.floor(rowCounter));
    }

    private int calculateYPosition(int row) {
        return row * (BUTTON_HEIGHT + SPACING);
    }

    @Override
    public void close() {
        if (client != null)
            client.setScreen(parent);
        else
            super.close();
    }

    @Override
    public void render(DrawContext context, int mouseX, int mouseY, float delta) {
        //Render the dimmed background
        this.renderBackground(context);

        //Add tittle
        context.drawCenteredTextWithShadow(this.textRenderer, this.title, this.width / 2, 15, 16777215);

        super.render(context, mouseX, mouseY, delta);
    }
}
