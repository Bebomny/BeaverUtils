package dev.bebomny.beaver.beaverutils.configuration;

import dev.bebomny.beaver.beaverutils.client.BeaverUtilsClient;
import dev.bebomny.beaver.beaverutils.configuration.config.GeneralConfig;
import dev.bebomny.beaver.beaverutils.configuration.gui.buttons.*;
import dev.bebomny.beaver.beaverutils.helpers.TextUtils;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.screen.option.OptionsScreen;
import net.minecraft.client.gui.widget.*;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.screen.ScreenTexts;
import net.minecraft.text.Text;

public class ConfigurationMenu extends Screen{

    private final Screen parent;
    private final BeaverUtilsClient beaverUtilsClient;
    private final Config config;
    private final GeneralConfig generalConfig;

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
        adder.add(new QuickTeleportButton()); //now make it work

        //Fullbright button
        adder.add(new FullBrightButton());

        //XrayButton
        adder.add(new XRayButton());

        //AutoClicker buttons
        adder.add(new AutoClickerButton());

        //Flight buttons
        adder.add(new FlightButton());

        //Reach buttons
        adder.add(new ReachButton());

        //NoFallDamage
        adder.add(new NoFallDmgButton());

        //InGameStats
        adder.add(new InGameStatsButton());

        //AutoPlant Buttons
        adder.add(new AutoPlantButton());

        //Add a DONE button
        adder.add(
                ButtonWidget.builder(
                    ScreenTexts.DONE,
                    button -> {
                        this.client.setScreen(this.parent);
                        beaverUtilsClient.configHandler.saveConfig();
                    }
                ).width(200).build(),
                2, adder.copyPositioner().marginTop(6)
        );

        //IDK it was in the Minecraft options Class, so it's probably needed xd //ooooh I know what it does now, pretty cool
        gridWidget.recalculateDimensions();

        //setting the grid in screen space
        SimplePositioningWidget.setPos(
                gridWidget,
                0, this.height / 6 - 12,
                this.width, this.height,
                0.5f, 0.05f
        );

        //Adding the grid to the screen
        this.addDrawableChild(gridWidget);

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

    @Override
    public void close() {
        if (client != null)
            client.setScreen(parent);
        else
            super.close();
    }

    @Override
    public void render(final MatrixStack matrices, final int mouseX, final int mouseY, final float delta) {
        //Render the dimmed background
        this.renderBackground(matrices);

        //Add tittle
        drawCenteredText(matrices, this.textRenderer, this.title, this.width / 2, 20, 16777215);

        super.render(matrices, mouseX, mouseY, delta);
    }
}
