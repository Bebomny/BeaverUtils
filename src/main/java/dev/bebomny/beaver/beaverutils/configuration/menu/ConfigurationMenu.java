package dev.bebomny.beaver.beaverutils.configuration.menu;

import dev.bebomny.beaver.beaverutils.client.BeaverUtilsClient;
import dev.bebomny.beaver.beaverutils.configuration.Config;
import dev.bebomny.beaver.beaverutils.configuration.config.GeneralConfig;
import dev.bebomny.beaver.beaverutils.configuration.gui.buttons.AutoClickerButton;
import dev.bebomny.beaver.beaverutils.configuration.gui.buttons.FeatureOptionsButtonWidget;
import dev.bebomny.beaver.beaverutils.configuration.gui.buttons.QuickTeleportButton;
import dev.bebomny.beaver.beaverutils.configuration.gui.buttons.ReachButton;
//import dev.bebomny.beaver.beaverutils.configuration.widget.ListWidget;
import dev.bebomny.beaver.beaverutils.features.SimpleOnOffFeature;
import dev.bebomny.beaver.beaverutils.helpers.TextUtils;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.tooltip.Tooltip;
import net.minecraft.client.gui.widget.AxisGridWidget;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.client.gui.widget.GridWidget;
import net.minecraft.client.gui.widget.SimplePositioningWidget;
import net.minecraft.screen.ScreenTexts;
import net.minecraft.text.Text;

public class ConfigurationMenu extends Screen {


    private final Screen parent;
    private final BeaverUtilsClient beaverUtilsClient;
    //public ListWidget list;

    public static final int STANDARD_HEIGHT = 20;
    public static final int STANDARD_WIDTH = 150;
    public static final int WIDTH_SPACING = 2;
    public static final int SPACING = 4;

    public ConfigurationMenu(Screen parent) {
        super(Text.of("BeaverUtils Options"));
        this.parent = parent;
        this.beaverUtilsClient = BeaverUtilsClient.getInstance();
    }

    @Override
    protected void init() {
        //search - button
        //hide all - button
        //
        //done button
    }

    @Override
    public void close() {
        //beaverUtilsClient.configHandler.saveConfig();

        if (client != null)
            client.setScreen(parent);
        else
            super.close();
    }

    @Override
    public void render(DrawContext context, int mouseX, int mouseY, float delta) {
        //Render the dimmed background
        this.renderBackground(context, mouseX, mouseY, delta);

        //Add tittle
        context.drawCenteredTextWithShadow(this.textRenderer, this.title, this.width / 2, 15, 16777215);

        super.render(context, mouseX, mouseY, delta);
    }
}
