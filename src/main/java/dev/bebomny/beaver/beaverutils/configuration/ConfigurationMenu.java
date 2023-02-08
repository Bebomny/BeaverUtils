package dev.bebomny.beaver.beaverutils.configuration;

import dev.bebomny.beaver.beaverutils.client.BeaverUtilsClient;
import dev.bebomny.beaver.beaverutils.features.Feature;
import net.minecraft.client.gui.screen.GameMenuScreen;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.client.option.GameOptions;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.screen.ScreenTexts;
import net.minecraft.text.Text;

public class ConfigurationMenu extends Screen {

    private final Screen parent;
    private final BeaverUtilsClient modBeaverUtils;
    private final GameOptions settings;
    private final int margin = (6 + 20);
    private final int MARGIN = 4;

    public ConfigurationMenu(Screen parent, GameOptions options) {
        super(Text.literal("BeaverUtils Options"));
        this.parent = parent;
        this.settings = options;
        this.modBeaverUtils = BeaverUtilsClient.getInstance();
    }

    protected void init() {

        //flight button
        ButtonWidget flightButton = ButtonWidget.builder(getText(modBeaverUtils.flight),
                button -> {
                    modBeaverUtils.flight.setEnabled(!modBeaverUtils.flight.isEnabled());
                    button.setMessage(getText(modBeaverUtils.flight));
                }).dimensions(
                (this.width/2) - 12 - 128,
                20 + (24 + 20),
                128,
                20
        ).build();

        //fullbright button
        ButtonWidget fullBrightButton = ButtonWidget.builder(getText(modBeaverUtils.fullBright),
                button -> {
                    modBeaverUtils.fullBright.setEnabled(!modBeaverUtils.fullBright.isEnabled());
                    button.setMessage(getText(modBeaverUtils.fullBright));
                }).dimensions(
                (this.width/2) + 12,
                20 + (24 + 20),
                128,
                20
        ).build();

        //nofall button
        ButtonWidget noFallButton = ButtonWidget.builder(getText(modBeaverUtils.noFall),
                button -> {
                    modBeaverUtils.noFall.setEnabled(!modBeaverUtils.noFall.isEnabled());
                    button.setMessage(getText(modBeaverUtils.noFall));
                }).dimensions(
                (this.width/2) - 12 - 128,
                (44 + 20) + (margin * 1),
                128,
                20
        ).build();

        //autoplant button
        ButtonWidget autoPlantButton = ButtonWidget.builder(getText(modBeaverUtils.autoPlant),
                button -> {
                    modBeaverUtils.autoPlant.setEnabled(!modBeaverUtils.autoPlant.isEnabled());
                    button.setMessage(getText(modBeaverUtils.autoPlant));
                }).dimensions(
                (this.width/2) + 12,
                (44 + 20) + (margin * 1),
                128,
                20
        ).build();

        //xray button
        ButtonWidget xrayButton = ButtonWidget.builder(getText(modBeaverUtils.xRay),
                button -> {
                    modBeaverUtils.xRay.setEnabled(!modBeaverUtils.xRay.isEnabled());
                    button.setMessage(getText(modBeaverUtils.xRay));
                }).dimensions(
                (this.width/2) - 12 - 128,
                (44 + 20) + (margin * 2),
                128,
                20
        ).build();

        //AutoClicker button
        ButtonWidget autoClickerButton = ButtonWidget.builder(getText(modBeaverUtils.autoClicker),
                button -> {
                    modBeaverUtils.autoClicker.setEnabled(!modBeaverUtils.autoClicker.isEnabled());
                    button.setMessage(getText(modBeaverUtils.autoClicker));
                }).dimensions(
                (this.width/2) + 12,
                (44 + 20) + (margin * 2),
                128,
                20
        ).build();

        ButtonWidget autoClickerDisplayButton = ButtonWidget.builder(Text.literal(modBeaverUtils.autoClicker.delay + " ticks"),
                button -> {

                }).dimensions(
                (this.width/2) + 12 + 128 + 12 + 20,
                (44 + 20) + (margin * 2),
                50,
                20
        ).build();

        ButtonWidget autoClickerPlusButton = ButtonWidget.builder(Text.literal("+"),
                button -> {
                    modBeaverUtils.autoClicker.setDelay(modBeaverUtils.autoClicker.delay + 1);
                    autoClickerDisplayButton.setMessage(Text.literal(modBeaverUtils.autoClicker.delay + " ticks"));
                }).dimensions(
                (this.width/2) + 12 + 128 + 8,
                (44 + 20) + (margin * 2),
                20,
                20
        ).build();

        ButtonWidget autoClickerMinusButton = ButtonWidget.builder(Text.literal("-"),
                button -> {
                    modBeaverUtils.autoClicker.setDelay(modBeaverUtils.autoClicker.delay - 1);
                    autoClickerDisplayButton.setMessage(Text.literal(modBeaverUtils.autoClicker.delay + " ticks"));
                }).dimensions(
                (this.width/2) + 12 + 128 + 16 + 20 + 50,
                (44 + 20) + (margin * 2),
                20,
                20
        ).build();

        ButtonWidget autoClickerModeButton = ButtonWidget.builder(Text.literal(modBeaverUtils.autoClicker.getMode().toString()),
                button -> {
                    modBeaverUtils.autoClicker.changeMode();
                    button.setMessage(Text.literal(modBeaverUtils.autoClicker.getMode().toString()));
                }).dimensions(
                (this.width/2) + 12 + 128 + 20 + 20 + 50 + 20,
                (44 + 20) + (margin * 2),
                54,
                20
        ).build();

        ButtonWidget autoClickerTypeButton = ButtonWidget.builder(Text.literal(modBeaverUtils.autoClicker.getType().toString()),
                button -> {
                    modBeaverUtils.autoClicker.changeType();
                    button.setMessage(Text.literal(modBeaverUtils.autoClicker.getType().toString()));
                }).dimensions(
                (this.width/2) + 12 + 128 + 24 + 20 + 50 + 20 + 54,
                (44 + 20) + (margin * 2),
                40,
                20
        ).build();

        //elytraSpeedControl
        ButtonWidget elytraSpeedControlButton = ButtonWidget.builder(getText(modBeaverUtils.elytraSpeedControl),
                button -> {
                    modBeaverUtils.elytraSpeedControl.setEnabled(!modBeaverUtils.elytraSpeedControl.isEnabled());
                    button.setMessage(getText(modBeaverUtils.elytraSpeedControl));
                }).dimensions(
                (this.width/2) - 12 - 128,
                (44 + 20) + (margin * 3),
                128,
                20
        ).build();

        //elytraSpeedControl instantFly
        ButtonWidget elytraSpeedControlInstantFlyButton = ButtonWidget.builder(getTextOnOff("InstantFly", modBeaverUtils.elytraSpeedControl.instantFly),
                button -> {
                    modBeaverUtils.elytraSpeedControl.setInstantFly(!modBeaverUtils.elytraSpeedControl.instantFly);
                    button.setMessage(getTextOnOff("InstantFly", modBeaverUtils.elytraSpeedControl.instantFly));
                }).dimensions(
                (this.width/2) - 12 - 128 - 4 - 80,
                (44 + 20) + (margin * 3),
                80,
                20
        ).build();

        //reach
        ButtonWidget reachButton = ButtonWidget.builder(getText(modBeaverUtils.reach),
                button -> {
                    modBeaverUtils.reach.setEnabled(!modBeaverUtils.reach.isEnabled());
                    button.setMessage(getText(modBeaverUtils.reach));
                }).dimensions(
                (this.width/2) + 12,
                (44 + 20) + (margin * 3),
                128,
                20
        ).build();

        ButtonWidget reachDisplayButton = ButtonWidget.builder(Text.literal(modBeaverUtils.reach.getReachDistance() + " blocks"),
                button -> {

                }).dimensions(
                (this.width/2) + 12 + 128 + 12 + 20,
                (44 + 20) + (margin * 3),
                70,
                20
        ).build();

        ButtonWidget reachPlusButton = ButtonWidget.builder(Text.literal("+"),
                button -> {
                    modBeaverUtils.reach.setReachDistance(modBeaverUtils.reach.getReachDistance() + 1);
                    reachDisplayButton.setMessage(Text.literal(modBeaverUtils.reach.getReachDistance() + " Blocks"));
                }).dimensions(
                (this.width/2) + 12 + 128 + 8,
                (44 + 20) + (margin * 3),
                20,
                20
        ).build();

        ButtonWidget reachMinusButton = ButtonWidget.builder(Text.literal("-"),
                button -> {
                    modBeaverUtils.reach.setReachDistance(modBeaverUtils.reach.getReachDistance() - 1);
                    reachDisplayButton.setMessage(Text.literal(modBeaverUtils.reach.getReachDistance() + " Blocks"));
                }).dimensions(
                (this.width/2) + 12 + 128 + 16 + 20 + 70,
                (44 + 20) + (margin * 3),
                20,
                20
        ).build();

        ButtonWidget extendedReachDisplayButton = ButtonWidget.builder(Text.literal(modBeaverUtils.reach.getMaxExtendedReachDistance() + " Blocks"),
                button -> {
                    button.setMessage(Text.literal(modBeaverUtils.reach.getMaxExtendedReachDistance() + " Blocks"));
                }).dimensions(
                (this.width/2) + 12 + 128 + 16 + 20 + 70 + 20 + 10 + 20 + 4,
                (44 + 20) + (margin * 3) + 10,
                90,
                20
        ).build();

        ButtonWidget extendedReachPlusButton = ButtonWidget.builder(Text.literal("+"),
                button -> {
                    modBeaverUtils.reach.setMaxExtendedReachDistance(modBeaverUtils.reach.getMaxExtendedReachDistance() + 10);
                    extendedReachDisplayButton.setMessage(Text.literal(modBeaverUtils.reach.getMaxExtendedReachDistance() + " Blocks"));
                }).dimensions(
                (this.width/2) + 12 + 128 + 16 + 20 + 20 + 70 + 10,
                (44 + 20) + (margin * 3) + 10,
                20,
                20
        ).build();

        ButtonWidget extendedReachMinusButton = ButtonWidget.builder(Text.literal("-"),
                button -> {
                    modBeaverUtils.reach.setMaxExtendedReachDistance(modBeaverUtils.reach.getMaxExtendedReachDistance() - 10);
                    extendedReachDisplayButton.setMessage(Text.literal(modBeaverUtils.reach.getMaxExtendedReachDistance() + " Blocks"));
                }).dimensions(
                (this.width/2) + 12 + 128 + 16 + 20 + 70 + 10 + 20 + 20 + 8 + 90,
                (44 + 20) + (margin * 3) + 10,
                20,
                20
        ).build();



        //"Done" button
        ButtonWidget doneButton = ButtonWidget.builder(ScreenTexts.DONE,
                button -> {
                    this.client.setScreen(this.parent);
                }).dimensions(
                (this.width/2) - 140,
                this.height - 50,
                280,
                20
        ).build();


        //1st row
        this.addDrawableChild(flightButton);
        this.addDrawableChild(fullBrightButton);
        //2nd row
        this.addDrawableChild(noFallButton);
        this.addDrawableChild(autoPlantButton);
        //3rd row
        this.addDrawableChild(xrayButton);
        this.addDrawableChild(autoClickerButton);
        this.addDrawableChild(autoClickerPlusButton);
        this.addDrawableChild(autoClickerDisplayButton);
        this.addDrawableChild(autoClickerMinusButton);
        this.addDrawableChild(autoClickerModeButton);
        this.addDrawableChild(autoClickerTypeButton);
        //4th row
        this.addDrawableChild(elytraSpeedControlButton);
        this.addDrawableChild(elytraSpeedControlInstantFlyButton);
        this.addDrawableChild(reachButton);
        this.addDrawableChild(reachPlusButton);
        this.addDrawableChild(reachDisplayButton);
        this.addDrawableChild(reachMinusButton);
        this.addDrawableChild(extendedReachPlusButton);
        this.addDrawableChild(extendedReachDisplayButton);
        this.addDrawableChild(extendedReachMinusButton);
        //5th row
        //this.addDrawableChild(autoTotemButton);

        //done button
        this.addDrawableChild(doneButton);
    }

    private Text getText(Feature feature) {
        return Text.literal(feature.getName() + (feature.isEnabled() ? " Enabled" : " Disabled"));
    }

    private Text getTextOnOff(String name, boolean enabled) {
        return Text.literal(name + (enabled ? " ON" : " OFF"));
    }

    @Override
    public void close() {
        assert client != null;
        client.setScreen(this.parent);
    }

    @Override
    public void render(final MatrixStack matrices, final int mouseX, final int mouseY, final float delta) {
        this.renderBackground(matrices);
        drawCenteredText(matrices, this.textRenderer, this.title, this.width / 2, 20, 16777215);
        drawCenteredTextWithShadow(
                matrices, this.textRenderer,
                Text.literal("MaxExtendedReachDistance").asOrderedText(),
                (this.width/2) + 12 + 128 + 16 + 20 + 70 + 20 + 10 + 20 + 4 + 45,
                (44 + 20) + (margin * 3) - 2, 16777215);
        super.render(matrices, mouseX, mouseY, delta);
    }
}
