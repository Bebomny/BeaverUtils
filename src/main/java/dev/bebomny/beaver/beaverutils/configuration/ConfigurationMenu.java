package dev.bebomny.beaver.beaverutils.configuration;

import dev.bebomny.beaver.beaverutils.client.BeaverUtilsClient;
import dev.bebomny.beaver.beaverutils.features.Feature;
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
                    reachDisplayButton.setMessage(Text.literal(modBeaverUtils.reach.getReachDistance() + " blocks"));
                }).dimensions(
                (this.width/2) + 12 + 128 + 8,
                (44 + 20) + (margin * 3),
                20,
                20
        ).build();

        ButtonWidget reachMinusButton = ButtonWidget.builder(Text.literal("-"),
                button -> {
                    modBeaverUtils.reach.setReachDistance(modBeaverUtils.reach.getReachDistance() - 1);
                    reachDisplayButton.setMessage(Text.literal(modBeaverUtils.reach.getReachDistance() + " blocks"));
                }).dimensions(
                (this.width/2) + 12 + 128 + 16 + 20 + 70,
                (44 + 20) + (margin * 3),
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


        //1 row
        this.addDrawableChild(flightButton);
        this.addDrawableChild(fullBrightButton);
        //2 row
        this.addDrawableChild(noFallButton);
        this.addDrawableChild(autoPlantButton);
        //3 row
        this.addDrawableChild(xrayButton);
        this.addDrawableChild(autoClickerButton);
        this.addDrawableChild(autoClickerPlusButton);
        this.addDrawableChild(autoClickerDisplayButton);
        this.addDrawableChild(autoClickerMinusButton);
        //4 row
        //this.addDrawableChild(autoTotemButton);
        this.addDrawableChild(reachButton);
        this.addDrawableChild(reachPlusButton);
        this.addDrawableChild(reachDisplayButton);
        this.addDrawableChild(reachMinusButton);
        //done button
        this.addDrawableChild(doneButton);
    }

    private Text getText(Feature feature) {
        return Text.literal(feature.getName() + (feature.isEnabled() ? " Enabled" : " Disabled"));
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
        super.render(matrices, mouseX, mouseY, delta);
    }
}
