package dev.bebomny.beaver.beaverutils.configuration.gui.buttons;

import dev.bebomny.beaver.beaverutils.client.BeaverUtilsClient;
import dev.bebomny.beaver.beaverutils.features.features.AutoClicker;
import dev.bebomny.beaver.beaverutils.helpers.TextUtils;
import net.minecraft.client.gui.tooltip.Tooltip;
import net.minecraft.client.gui.widget.AxisGridWidget;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.text.Text;

public class AutoClickerButton extends AxisGridWidget {

    private AutoClicker autoClicker = BeaverUtilsClient.getInstance().features.autoClicker;

    public AutoClickerButton() {
        super(128, 20, DisplayAxis.HORIZONTAL);
        this.getMainPositioner().alignLeft();

        ButtonWidget autoClickerEnable = ButtonWidget.builder(
                TextUtils.getEnabledDisabledText(autoClicker.getName(), autoClicker.isEnabled()),
                button -> {
                    autoClicker.setEnabled(!autoClicker.isEnabled());
                    button.setMessage(TextUtils.getEnabledDisabledText(autoClicker.getName(), autoClicker.isEnabled()));
                }
        ).dimensions(0, 0, 128, 20).build();

        ButtonWidget autoClickerDelayDisplay = ButtonWidget.builder(
                Text.of(autoClicker.getDelay() + " ticks"),
                button -> {
                    //doesnt do shit, its just a display
                }
        ).dimensions(128 + 2, 0, 65, 20).build();

        ButtonWidget autoClickerPlusButton = ButtonWidget.builder(
                Text.of("§l+"),
                button -> {
                    autoClicker.setDelay(autoClicker.getDelay() + 1);
                    autoClickerDelayDisplay.setMessage(Text.of(autoClicker.getDelay() + " ticks"));
                }
        ).size(20, 20).position(128 + 2 + 65 + 2, 0).build();

        ButtonWidget autoClickerMinusButton = ButtonWidget.builder(
                Text.of("§l-"),
                button -> {
                    autoClicker.setDelay(autoClicker.getDelay() - 1);
                    autoClickerDelayDisplay.setMessage(Text.of(autoClicker.getDelay() + " ticks"));
                }
        ).size(20, 20).position(128 + 2 + 65 + 2 + 20 + 2, 0).build();

        ButtonWidget autoClickerModeCycleButton = ButtonWidget.builder(
                Text.of("Mode: " + autoClicker.getMode()),
                button -> {
                    autoClicker.changeMode();
                    button.setMessage(Text.of("Mode: " + autoClicker.getMode()));
                }
        ).size(80, 20).position(128 + 2 + 65 + 2 + 20 + 2 + 20 + 2, 0).build();

        ButtonWidget autoClickerTypeCycleButton = ButtonWidget.builder(
                Text.of("Type: " + autoClicker.getType()),
                button -> {
                    autoClicker.changeType();
                    button.setMessage(Text.of("Type: " + autoClicker.getType()));
                }
        ).size(65, 20).position(128 + 2 + 65 + 2 + 20 + 2 + 20 + 2 + 80 + 2, 0).build();

        this.add(autoClickerEnable);
        this.add(autoClickerDelayDisplay);
        this.add(autoClickerPlusButton);
        this.add(autoClickerMinusButton);
        this.add(autoClickerModeCycleButton);
        this.add(autoClickerTypeCycleButton);

        this.getMainPositioner().alignLeft();
    }
}
