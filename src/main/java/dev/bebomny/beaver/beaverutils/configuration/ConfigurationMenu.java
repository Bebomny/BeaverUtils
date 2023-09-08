package dev.bebomny.beaver.beaverutils.configuration;

import dev.bebomny.beaver.beaverutils.client.BeaverUtilsClient;
import dev.bebomny.beaver.beaverutils.configuration.config.GeneralConfig;
import dev.bebomny.beaver.beaverutils.configuration.gui.buttons.*;
import dev.bebomny.beaver.beaverutils.features.TriggerFeature;
import dev.bebomny.beaver.beaverutils.features.features.*;
import dev.bebomny.beaver.beaverutils.helpers.TextUtils;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.screen.option.OptionsScreen;
import net.minecraft.client.gui.tooltip.OrderedTextTooltipComponent;
import net.minecraft.client.gui.tooltip.TooltipComponent;
import net.minecraft.client.gui.widget.*;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.screen.ScreenTexts;
import net.minecraft.text.StringVisitable;
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
        //GridWidget gridWidget = new GridWidget();
        //gridWidget.getMainPositioner().marginX(5).marginBottom(4).alignHorizontalCenter();
        //Create an adder to add buttons to the grid
        //GridWidget.Adder adder = gridWidget.createAdder(2);
        int centerX = this.width / 2;
        int startY = this.height / 6  - 12;
        int offsetX = 4;
        int spacing = 2;

        // tp added but not functional
        //now make it work
        QuickTeleport quickTeleport = BeaverUtilsClient.getInstance().features.quickTeleport;
        ButtonWidget teleportDisplayButton = new ButtonWidget(
                centerX - offsetX - 20 - spacing - 20 - spacing - 84, startY,
                84, 20,
                Text.of("Quick TP: §r" + (quickTeleport.getDistance())), //do something chnage numebr ec.
                button -> {
                    quickTeleport.fire(TriggerFeature.FiredBy.MENU);
                },
                createDefaultTooltipSupplier(StringVisitable.plain("§l§c(NOT YET IMPLEMENTED) §r Teleports you in the direction you are facing!"))
        );

        ButtonWidget teleportPlusButton = new ButtonWidget(
                centerX - offsetX - 20 - spacing - 20, startY,
                20, 20,
                Text.of("§l+"),
                button -> {
                    //increase the tp distance
                    quickTeleport.setDistance(quickTeleport.getDistance() + 1);
                    teleportDisplayButton.setMessage(Text.of("Quick TP: §r" + (quickTeleport.getDistance())));
                    //if above certain level display a warning thath it is too far
                }
        );

        ButtonWidget teleportMinusButton = new ButtonWidget(
                centerX - offsetX - 20, startY,
                20, 20,
                Text.of("§l-"),
                button -> {
                    //decrease the tp distance
                    quickTeleport.setDistance(quickTeleport.getDistance() - 1);
                    teleportDisplayButton.setMessage(Text.of("Quick TP: §r" + (quickTeleport.getDistance())));
                    //if below certain level display a warning that it will tp backwards
                }
        );

        this.addDrawableChild(teleportDisplayButton);
        this.addDrawableChild(teleportPlusButton);
        this.addDrawableChild(teleportMinusButton);
        //Quick tp end

        //Fullbright button
        this.addDrawableChild(new FullBrightButton(centerX + offsetX, startY));
        //Fullbright end

        //XrayButton
        this.addDrawableChild(new XRayButton(centerX - offsetX - 128, startY + 4 + 20));
        //XrayButton

        //AutoClicker buttons
        AutoClicker autoClicker = BeaverUtilsClient.getInstance().features.autoClicker;
        ButtonWidget autoClickerEnable = new ButtonWidget(
                centerX + offsetX, startY + 4 + 20,
                128, 20,
                TextUtils.getEnabledDisabledText(autoClicker.getName(), autoClicker.isEnabled()),
                button -> {
                    autoClicker.setEnabled(!autoClicker.isEnabled());
                    button.setMessage(TextUtils.getEnabledDisabledText(autoClicker.getName(), autoClicker.isEnabled()));
                }
        );

        ButtonWidget autoClickerDelayDisplay = new ButtonWidget(
                centerX + offsetX + 128 + spacing, startY + 4 + 20,
                65, 20,
                Text.of(autoClicker.getDelay() + " ticks"),
                button -> {
                    //doesnt do shit, its just a display
                }
        );

        ButtonWidget autoClickerPlusButton = new ButtonWidget(
                centerX + offsetX + 128 + spacing + 65 + spacing, startY + 4 + 20,
                20 ,20,
                Text.of("§l+"),
                button -> {
                    autoClicker.setDelay(autoClicker.getDelay() + 1);
                    autoClickerDelayDisplay.setMessage(Text.of(autoClicker.getDelay() + " ticks"));
                }
        );

        ButtonWidget autoClickerMinusButton = new ButtonWidget(
                centerX + offsetX + 128 + spacing + 65 + spacing + 20 + spacing, startY + 4 + 20,
                20, 20,
                Text.of("§l-"),
                button -> {
                    autoClicker.setDelay(autoClicker.getDelay() - 1);
                    autoClickerDelayDisplay.setMessage(Text.of(autoClicker.getDelay() + " ticks"));
                }
        );

        ButtonWidget autoClickerModeCycleButton = new ButtonWidget(
                centerX + offsetX + 128 + spacing + 65 + spacing + 20 + spacing + 20 + spacing, startY + 4 + 20,
                80, 20,
                Text.of("Mode: " + autoClicker.getMode()),
                button -> {
                    autoClicker.changeMode();
                    button.setMessage(Text.of("Mode: " + autoClicker.getMode()));
                }
        );

        ButtonWidget autoClickerTypeCycleButton = new ButtonWidget(
                centerX + offsetX + 128 + spacing + 65 + spacing + 20 + spacing + 20 + spacing + 80 + spacing, startY + 4 + 20,
                65, 20,
                Text.of("Type: " + autoClicker.getType()),
                button -> {
                    autoClicker.changeType();
                    button.setMessage(Text.of("Type: " + autoClicker.getType()));
                }
        );

        this.addDrawableChild(autoClickerEnable);
        this.addDrawableChild(autoClickerDelayDisplay);
        this.addDrawableChild(autoClickerPlusButton);
        this.addDrawableChild(autoClickerMinusButton);
        this.addDrawableChild(autoClickerModeCycleButton);
        this.addDrawableChild(autoClickerTypeCycleButton);
        //AutoClicker buttons

        //Flight buttons
        Flight flight = BeaverUtilsClient.getInstance().features.flight;
        ButtonWidget flightButton = new ButtonWidget(
                centerX - offsetX - 128, startY + 4 + 20 + 4 + 20,
                128, 20,
                TextUtils.getEnabledDisabledText(flight.getName(), flight.isEnabled()),
                button -> {
                    flight.setEnabled(!flight.isEnabled());
                    button.setMessage(TextUtils.getEnabledDisabledText(flight.getName(), flight.isEnabled()));
                }
        );

        //TODO: To fix
        StringVisitable notImplementedTooltip = StringVisitable.plain("§cNot Implemented Yet!!!");

        ButtonWidget flightMode = new ButtonWidget(
                centerX - offsetX - 128 - spacing - 80, startY + 4 + 20 + 4 + 20,
                80, 20,
                Text.of("Mode: " + flight.getMode()),
                button -> {
                    flight.changeMode();
                    button.setMessage(Text.of("Mode: " + flight.getMode()));
                },
                createDefaultTooltipSupplier(notImplementedTooltip)
        );

        this.addDrawableChild(flightButton);
        this.addDrawableChild(flightMode);
        //Flight buttons

        //Reach buttons
        Reach reach = BeaverUtilsClient.getInstance().features.reach;
        ButtonWidget reachEnable = new ButtonWidget(
                centerX + offsetX, startY + 4 + 20 + 4 + 20,
                108, 20,
                TextUtils.getEnabledDisabledText(reach.getName(), reach.isEnabled()),
                button -> {
                    reach.setEnabled(!reach.isEnabled());
                    button.setMessage(TextUtils.getEnabledDisabledText(reach.getName(), reach.isEnabled()));
                }
        );

        ButtonWidget reachDisplay = new ButtonWidget(
                centerX + offsetX + 108 + spacing, startY + 4 + 20 + 4 + 20,
                65, 20,
                Text.of(reach.getDistance() + " Blocks"),
                button -> {
                    //reset to default
                    reach.setDistance(5.0f);
                    button.setMessage(Text.of(reach.getDistance() + " Blocks"));
                }
        );

        ButtonWidget reachPlus = new ButtonWidget(
                centerX + offsetX + 108 + spacing + 65 + spacing, startY + 4 + 20 + 4 + 20,
                20, 20,
                Text.of("§l+"),
                button -> {
                    //increase the distance
                    reach.setDistance(reach.getDistance() + 0.5f);
                    reachDisplay.setMessage(Text.of(reach.getDistance() + " Blocks"));
                }
        );

        ButtonWidget reachMinus = new ButtonWidget(
                centerX + offsetX + 108 + spacing + 65 + spacing + 20 + spacing, startY + 4 + 20 + 4 + 20,
                20, 20,
                Text.of("§l-"),
                button -> {
                    reach.setDistance(reach.getDistance() - 0.5f);
                    reachDisplay.setMessage(Text.of(reach.getDistance() + " Blocks"));
                }
        );

        this.addDrawableChild(reachEnable);
        this.addDrawableChild(reachDisplay);
        this.addDrawableChild(reachPlus);
        this.addDrawableChild(reachMinus);
        //Reach buttons


        //NoFallDamage
        this.addDrawableChild(new NoFallDmgButton(centerX - offsetX - 128, startY + 4 + 20 + 4 + 20 + 4 + 20));
        //NoFallDamage


        //InGameStats
        this.addDrawableChild(new InGameStatsButton(centerX + offsetX, startY + 4 + 20 + 4 + 20 + 4 + 20));
        //InGameStats


        //AutoPlant Buttons
        AutoPlant autoPlant = BeaverUtilsClient.getInstance().features.autoPlant;
        StringVisitable autoPlantWarning = StringVisitable.plain("§cOnly works on vanilla crops for now");
        ButtonWidget autoPlantButton = new ButtonWidget(
                centerX - offsetX - 128, startY + 4 + 20 + 4 + 20 + 4 + 20 + 4 + 20,
                128, 20,
                TextUtils.getEnabledDisabledText(autoPlant.getName(), autoPlant.isEnabled()),
                button -> {
                    autoPlant.setEnabled(!autoPlant.isEnabled());
                    button.setMessage(TextUtils.getEnabledDisabledText(autoPlant.getName(), autoPlant.isEnabled()));
                },
                createDefaultTooltipSupplier(autoPlantWarning)
        );

        StringVisitable plantRadiusTooltip = StringVisitable.plain("§cFor now its only possible to change this directly in the config!");

        ButtonWidget autoPlantPlantRadiusButton = new ButtonWidget(
                centerX - offsetX - 128 - spacing - 90, startY + 4 + 20 + 4 + 20 + 4 + 20 + 4 + 20,
                90, 20,
                Text.of("Plant Radius: " + autoPlant.getPlantRadius()),
                button -> {
                    //can change in the config for now
                    button.setMessage(Text.of("Plant Radius: " + autoPlant.getPlantRadius()));
                },
                createDefaultTooltipSupplier(plantRadiusTooltip)
        );

        //TODO: fix

        ButtonWidget autoPlantModeButton = new ButtonWidget(
                centerX - offsetX - 128 - spacing - 90 - spacing - 164, startY + 4 + 20 + 4 + 20 + 4 + 20 + 4 + 20,
                164, 20,
                Text.of("Mode: " + autoPlant.getMode()),
                button -> {
                    autoPlant.changeMode();
                    button.setMessage(Text.of("Mode: " + autoPlant.getMode()));
                },
                createDefaultTooltipSupplier(notImplementedTooltip)
        );

        this.addDrawableChild(autoPlantButton);
        this.addDrawableChild(autoPlantPlantRadiusButton);
        this.addDrawableChild(autoPlantModeButton);
        //AutoPlant Buttons

        //Add a DONE button
        ButtonWidget doneButton = new ButtonWidget(
                centerX - 100, this.height - 30,
                200, 20,
                ScreenTexts.DONE,
                button -> {
                    this.client.setScreen(this.parent);
                    beaverUtilsClient.configHandler.saveConfig();
                }
        );

        this.addDrawableChild(doneButton);

        //IDK it was in the Minecraft options Class, so it's probably needed xd //ooooh I know what it does now, pretty cool
        //gridWidget.recalculateDimensions();

        //setting the grid in screen space
        //SimplePositioningWidget.setPos(
        //        gridWidget,
        //        0, this.height / 6 - 12,
        //        this.width, this.height,
        //        0.5f, 0.05f
        //);

        //Adding the grid to the screen
        //this.addDrawableChild(gridWidget);

        ButtonWidget autoEnableButton = new ButtonWidget(
                20, height - 30,
                119, 20,
                TextUtils.getEnabledDisabledText("AutoEnable", generalConfig.autoEnable),
                button -> {
                    generalConfig.autoEnable = !generalConfig.autoEnable;
                    button.setMessage(TextUtils.getEnabledDisabledText("AutoEnable", generalConfig.autoEnable));
                },
                createDefaultTooltipSupplier(StringVisitable.plain("Enables enabled features on config load"))
        );


        ButtonWidget debugButton = new ButtonWidget(
                20, height - 30 - 4 - 20,
                90, 20,
                TextUtils.getEnabledDisabledText("Debug", generalConfig.debug),
                button -> {
                    generalConfig.debug = !generalConfig.debug;
                    button.setMessage(TextUtils.getEnabledDisabledText("Debug", generalConfig.debug));
                },
                createDefaultTooltipSupplier(StringVisitable.plain("§l§a§kSGH §l§aDEBUG §l§a§kSGH §l§adebug §l§aDebug §l§a§kSGH §l§adEbUg §l§a§kSGH §l§fdoesnt do anything xd"))
        );

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

    private ButtonWidget.TooltipSupplier createDefaultTooltipSupplier(StringVisitable text) {
        return (button, matrices, mouseX, mouseY) -> {
            renderOrderedTooltip(matrices, textRenderer.wrapLines(text, width / 100 * 100 / 2), mouseX, mouseY);
        };
    }
}
