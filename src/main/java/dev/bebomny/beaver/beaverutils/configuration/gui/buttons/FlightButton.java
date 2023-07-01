package dev.bebomny.beaver.beaverutils.configuration.gui.buttons;

import dev.bebomny.beaver.beaverutils.client.BeaverUtilsClient;
import dev.bebomny.beaver.beaverutils.configuration.config.FlightConfig;
import dev.bebomny.beaver.beaverutils.features.features.Flight;
import dev.bebomny.beaver.beaverutils.helpers.TextUtils;
import net.minecraft.client.gui.tooltip.Tooltip;
import net.minecraft.client.gui.widget.AxisGridWidget;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.text.Text;

public class FlightButton extends AxisGridWidget {

    private Flight flight = BeaverUtilsClient.getInstance().features.flight;
    private FlightConfig flightConfig = BeaverUtilsClient.getInstance().getConfig().flightConfig;

    public FlightButton(int x, int y) {
        super(128, 20, DisplayAxis.HORIZONTAL);

        ButtonWidget flightButton = ButtonWidget.builder(
                TextUtils.getEnabledDisabledText(flight.getName(), flight.isEnabled()),
                button -> {
                    flight.setEnabled(!flight.isEnabled());
                    button.setMessage(TextUtils.getEnabledDisabledText(flight.getName(), flight.isEnabled()));
                }
        ).dimensions(0, 0, 128, 20).build();

        //TODO: To fix
        Tooltip notImplementedTooltip = Tooltip.of(Text.of("§cNot Implemented Yet!!!"));

        ButtonWidget flightMode = ButtonWidget.builder(
                Text.of("Mode: " + flight.getMode()),
                button -> {
                    flight.changeMode();
                    button.setMessage(Text.of("Mode: " + flight.getMode()));
                }
        ).dimensions(-2 - 80,0, 80, 20).tooltip(notImplementedTooltip).build();

        this.add(flightButton);
        this.add(flightMode);

        this.getMainPositioner().alignRight();
        this.setPosition(x, y);
    }
}
