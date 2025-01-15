package dev.bebomny.beaver.beaverutils.configuration;

import dev.bebomny.beaver.beaverutils.client.BeaverUtilsClient;
import dev.bebomny.beaver.beaverutils.configuration.config.GeneralConfig;
import dev.bebomny.beaver.beaverutils.configuration.gui.buttons.*;
import dev.bebomny.beaver.beaverutils.features.SimpleOnOffFeature;
import dev.bebomny.beaver.beaverutils.helpers.TextUtils;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.screen.GameMenuScreen;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.tooltip.Tooltip;
import net.minecraft.client.gui.widget.*;
import net.minecraft.screen.ScreenTexts;
import net.minecraft.text.Text;

public class ConfigurationMenu extends Screen{

    private final Screen parent;
    private final BeaverUtilsClient beaverUtilsClient;
    private final Config config;
    private final GeneralConfig generalConfig;

    public static final int STANDARD_HEIGHT = 20;
    public static final int STANDARD_WIDTH = 160;
    public static final int WIDTH_SPACING = 2;
    public static final int SPACING = 4;

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
        adder.add(new QuickTeleportButton(-(STANDARD_WIDTH + WIDTH_SPACING), getYPosition(0))); //now make it work

        //FullBright button
        adder.add(this.createEnableButton(beaverUtilsClient.getFeatures().fullBright, WIDTH_SPACING, getYPosition(0)));
        //adder.add(new FullBrightButton(2, calculateYPosition(0)));

        //XRayButton
        adder.add(this.createCombinedButtons(beaverUtilsClient.getFeatures().xRay, -(STANDARD_WIDTH + WIDTH_SPACING), getYPosition(1)));
        //adder.add(new XRayButton(-(STANDARD_WIDTH + WIDTH_SPACING), calculateYPosition(1)));

        //AutoClicker buttons
        adder.add(new AutoClickerButton(WIDTH_SPACING, getYPosition(1))); //For now, it stays

        //Flight buttons
        adder.add(this.createCombinedButtons(beaverUtilsClient.getFeatures().flight, -(STANDARD_WIDTH + WIDTH_SPACING), getYPosition(2)));
        //adder.add(new FlightButton(-(STANDARD_WIDTH + WIDTH_SPACING), calculateYPosition(2)));

        //Reach buttons
        adder.add(new ReachButton(WIDTH_SPACING, getYPosition(2))); //For now, it stays

        //NoFallDamage
        adder.add(this.createEnableButton(beaverUtilsClient.getFeatures().noFallDmg, -(STANDARD_WIDTH + WIDTH_SPACING), getYPosition(3)));
        //adder.add(new NoFallDmgButton(-(STANDARD_WIDTH + WIDTH_SPACING), calculateYPosition(3)));

        //InGameStats
        adder.add(this.createEnableButton(beaverUtilsClient.getFeatures().inGameStats, WIDTH_SPACING, getYPosition(3)));
        //adder.add(new InGameStatsButton(2, calculateYPosition(3)));

        //AutoPlant Buttons
        adder.add(this.createCombinedButtons(beaverUtilsClient.getFeatures().autoPlant, -(STANDARD_WIDTH + WIDTH_SPACING), getYPosition(4)));
        //adder.add(new AutoPlantButton(-(128 + 4), calculateYPosition(4)));

        //EntityListDisplay Buttons
        adder.add(this.createCombinedButtons(beaverUtilsClient.getFeatures().entityListDisplay, WIDTH_SPACING, getYPosition(4)));

        //Elytra Speed Control Buttons
        adder.add(this.createCombinedButtons(beaverUtilsClient.getFeatures().elytraSpeedControl, -(STANDARD_WIDTH + WIDTH_SPACING), getYPosition(5)));

        //Custom Renderer
        adder.add(this.createCombinedButtons(beaverUtilsClient.getFeatures().customRenderer, WIDTH_SPACING, getYPosition(5)));

        //Add a DONE button
        adder.add(
                ButtonWidget.builder(
                    ScreenTexts.DONE,
                    button -> this.close()
                ).width(200).position(-102, getYPosition(6) + 14).build(),
                2, adder.copyPositioner().marginTop(6)
        );

        //IDK it was in the Minecraft options Class, so it's probably needed xd //ooh I know what it does now, pretty cool //it is fucked up in this version(1.20.1), not recommended
        //its working as intended but, I used it a bit differently, so it's not working as I would like it to, but I found a way around this
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


        //Tittle

        TextWidget tittleText = new TextWidget(0, 40, this.width, 9, this.title, this.textRenderer);
        this.addDrawableChild(tittleText);

    }

    public static int getYPosition(int row) {
        return row * (STANDARD_HEIGHT + SPACING);
    }

    @Override
    public void close() {
        beaverUtilsClient.configHandler.saveConfig();

        if (client != null)
            client.setScreen(parent);
        else
            super.close();
    }

    @Override
    public void render(DrawContext context, int mouseX, int mouseY, float delta) {
        //Render the dimmed background
        renderBackground(context, mouseX, mouseY, delta);
        //Add tittle
        //context.drawCenteredTextWithShadow(textRenderer, this.title, this.width / 2, 15, 16777215);

        super.render(context, mouseX, mouseY, delta);
    }

    private AxisGridWidget createCombinedButtons(SimpleOnOffFeature feature, int x, int y) {
        ButtonWidget mainButton = createEnableButton(feature, 0, 0);
        mainButton.setWidth(STANDARD_WIDTH - WIDTH_SPACING - 20);

        FeatureOptionsButtonWidget optionsButton = new FeatureOptionsButtonWidget(
                0, 0,
                button -> this.client.setScreen(feature.getOptionsMenu(this))
        );

        AxisGridWidget axisGridWidget = new AxisGridWidget(STANDARD_WIDTH, STANDARD_HEIGHT, AxisGridWidget.DisplayAxis.HORIZONTAL);

        // Put the settings button on the outer-side
        if (x < 0) {
            axisGridWidget.add(optionsButton);
            axisGridWidget.add(mainButton);
        } else {
            axisGridWidget.add(mainButton);
            axisGridWidget.add(optionsButton);
        }

        axisGridWidget.refreshPositions();
        axisGridWidget.setPosition(x, y);
        return axisGridWidget;
    }

    private ButtonWidget createEnableButton(SimpleOnOffFeature feature, int x, int y) {
        return ButtonWidget.builder(
                TextUtils.getEnabledDisabledText(feature.getName(), feature.isEnabled()),
                button -> {
                    feature.setEnabled(!feature.isEnabled());
                    button.setMessage(TextUtils.getEnabledDisabledText(feature.getName(), feature.isEnabled()));
                }
        ).dimensions(x, y, ConfigurationMenu.STANDARD_WIDTH, 20).tooltip(feature.getMainToolTip()).build();
    }
}
