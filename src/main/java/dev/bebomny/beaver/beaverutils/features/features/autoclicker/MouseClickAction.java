package dev.bebomny.beaver.beaverutils.features.features.autoclicker;

import com.google.gson.annotations.Expose;
import dev.bebomny.beaver.beaverutils.client.BeaverUtilsClient;
import dev.bebomny.beaver.beaverutils.configuration.ConfigurationMenu;
import dev.bebomny.beaver.beaverutils.notifications.NotificationHandler;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.font.TextRenderer;
import net.minecraft.client.gui.widget.AxisGridWidget;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.client.gui.widget.TextWidget;
import net.minecraft.text.Text;

public abstract class MouseClickAction implements ClickAction {

    @Expose
    protected MouseButton mouseButton;
    @Expose
    protected boolean actionEnabled;
    @Expose
    protected int delay;
    @Expose
    protected ClickType type;

    MinecraftClient client;
    NotificationHandler notifier;

    protected MouseClickAction(MouseButton mouseButton, int delay, ClickType type) {
        this.mouseButton = mouseButton;
        this.delay = delay;
        this.type = type;
        this.actionEnabled = false;

        this.client = BeaverUtilsClient.getInstance().client;
        this.notifier = BeaverUtilsClient.getInstance().getNotifier();
    }

    public static MouseClickAction defaultMouseClickAction(MouseButton defaultMouseButton) {
        if (defaultMouseButton == MouseButton.LEFT) {
            return new LeftMouseClickActionImpl(30, ClickType.CLICK);
        }

        if (defaultMouseButton == MouseButton.RIGHT) {
            return new RightMouseClickActionImpl(30, ClickType.CLICK);
        }

        return null;
    }

    @Override
    public void performAction(long ticksPast) {
        //These here are needed, because for some reason, when instantiating this class in the config these fields are null:/
        if (this.client == null) {
            this.client = BeaverUtilsClient.getInstance().client;
        }

        if (this.notifier == null) {
            this.notifier = BeaverUtilsClient.getInstance().getNotifier();
        }
        ///

        if (!actionEnabled) {
            return;
        }

        if (!(ticksPast % delay == 0)) {
            return;
        }

        switch (type) {
            case CLICK -> performClickAction();
            case HOLD -> performHoldAction();
        }
    }

    protected abstract void performClickAction();

    protected abstract void performHoldAction();

    @Override
    public AxisGridWidget createMouseActionClickControlSubMenu(int centerX, int y, TextRenderer textRenderer) {
        int verticalLayers = 4;

        AxisGridWidget verticalGridWidget = new AxisGridWidget(
                200,
                ConfigurationMenu.STANDARD_HEIGHT * verticalLayers + ConfigurationMenu.SPACING,
                AxisGridWidget.DisplayAxis.VERTICAL);

        //Layer 1 - Text widget
        Text actionTitleText = Text.of(String.format("§f %s Action Controls", mouseButton.getDisplayText().getLiteralString()));
        TextWidget actionTitleTextWidget = new TextWidget(
                -(textRenderer.getWidth(actionTitleText) / 2),
                ConfigurationMenu.getYPosition(1),
                //0,
                textRenderer.getWidth(actionTitleText), 20,
                actionTitleText, textRenderer);

        //Layer 2 - Enable/Disable
        AxisGridWidget actionEnabledHorizontalWidget = new AxisGridWidget(
                200,
                ConfigurationMenu.STANDARD_HEIGHT,
                AxisGridWidget.DisplayAxis.HORIZONTAL);

        Text actionEnableText = Text.of(String.format("§f %s", mouseButton.getDisplayText().getLiteralString()));
        TextWidget actionEnableTextWidget = new TextWidget(
                -(textRenderer.getWidth(actionEnableText) / 2),
                ConfigurationMenu.getYPosition(2),
                //0,
                textRenderer.getWidth(actionEnableText), 20,
                actionEnableText, textRenderer);


        ButtonWidget enableDisableButton = ButtonWidget.builder(
                Text.translatable(isActionEnabled() ? "global.text.enabled" : "global.text.disabled"),
                button -> {
                    setActionEnabled(!isActionEnabled());
                    button.setMessage(Text.translatable(isActionEnabled() ? "global.text.enabled" : "global.text.disabled"));
                }).width(textRenderer.getWidth(Text.translatable("global.text.disabled")) + 20).build();

        actionEnabledHorizontalWidget.add(actionEnableTextWidget);
        actionEnabledHorizontalWidget.add(enableDisableButton);
        actionEnabledHorizontalWidget.refreshPositions();
        verticalGridWidget.add(actionTitleTextWidget);
        verticalGridWidget.add(actionEnabledHorizontalWidget);

        //Layer 3 - Click Type
        AxisGridWidget clickTypeHorizontalWidget = new AxisGridWidget(
                200,
                ConfigurationMenu.STANDARD_HEIGHT,
                AxisGridWidget.DisplayAxis.HORIZONTAL);

        Text clickTypeText = Text.translatable("feature.auto_clicker.click_type.text");
        TextWidget clickTypeTextWidget = new TextWidget(
                -(textRenderer.getWidth(clickTypeText) / 2),
                ConfigurationMenu.getYPosition(3),
                //0,
                textRenderer.getWidth(clickTypeText), 20,
                clickTypeText, textRenderer);

        ButtonWidget changeTypeButtonWidget = ButtonWidget.builder(
                Text.translatable(getType() == ClickType.CLICK ? "feature.auto_clicker.click_type.click.text" : "feature.auto_clicker.click_type.hold.text"),
                button -> {
                    changeType();
                    button.setMessage(Text.translatable(getType() == ClickType.CLICK ? "feature.auto_clicker.click_type.click.text" : "feature.auto_clicker.click_type.hold.text"));
                }).width(80).build();

        clickTypeHorizontalWidget.add(clickTypeTextWidget);
        clickTypeHorizontalWidget.add(changeTypeButtonWidget);
        clickTypeHorizontalWidget.refreshPositions();
        verticalGridWidget.add(clickTypeHorizontalWidget);

        //Layer 4 - Delay
        AxisGridWidget delayHorizontalWidget = new AxisGridWidget(
                200,
                ConfigurationMenu.STANDARD_HEIGHT,
                AxisGridWidget.DisplayAxis.HORIZONTAL);

        Text delayText = Text.translatable("feature.auto_clicker.delay.text");
        TextWidget delayTextWidget = new TextWidget(
                -(textRenderer.getWidth(delayText) / 2),
                ConfigurationMenu.getYPosition(4),
                //0,
                textRenderer.getWidth(delayText), 20,
                delayText, textRenderer);

        AxisGridWidget delayControlsWidget = new AxisGridWidget(
                65+2+20+2+20,
                ConfigurationMenu.STANDARD_HEIGHT,
                AxisGridWidget.DisplayAxis.HORIZONTAL);

        ButtonWidget displayWidget = ButtonWidget.builder(
                Text.of(String.format("%d ticks", getDelay())),
                button -> {
                    setDelay(30);
                    button.setMessage(Text.of(String.format("%d ticks", getDelay())));
                }
        ).width(65).build();

        ButtonWidget plusButton = ButtonWidget.builder(
                Text.of("§l+"),
                button -> {
                    setDelay(getDelay() + 1);
                    displayWidget.setMessage(Text.of(String.format("%d ticks", getDelay())));
                }
        ).size(20, 20).build();

        ButtonWidget minusButton = ButtonWidget.builder(
                Text.of("§l-"),
                button -> {
                    setDelay(getDelay() - 1);
                    displayWidget.setMessage(Text.of(String.format("%d ticks", getDelay())));
                }
        ).size(20, 20).build();

        delayControlsWidget.add(displayWidget);
        delayControlsWidget.add(plusButton);
        delayControlsWidget.add(minusButton);
        delayControlsWidget.refreshPositions();

        delayHorizontalWidget.add(delayTextWidget);
        delayHorizontalWidget.add(delayControlsWidget);
        delayHorizontalWidget.refreshPositions();
        verticalGridWidget.add(delayHorizontalWidget);

        //Finish setting up positioning
        verticalGridWidget.refreshPositions();
        verticalGridWidget.setPosition(centerX - verticalGridWidget.getWidth() / 2, y);

        return verticalGridWidget;
    }

    public int getDelay() {
        return delay;
    }

    public void setDelay(int delay) {
        this.delay = delay;
    }

    public void changeType() {
        setType(getType() == ClickType.CLICK ? ClickType.HOLD : ClickType.CLICK);
    }

    public ClickType getType() {
        return type;
    }

    public void setType(ClickType type) {
        this.type = type;
    }

    public MouseButton getMouseButton() {
        return mouseButton;
    }

    public boolean isActionEnabled() {
        return actionEnabled;
    }

    public void setActionEnabled(boolean actionEnabled) {
        this.actionEnabled = actionEnabled;
    }
}
