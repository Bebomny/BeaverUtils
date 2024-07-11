package dev.bebomny.beaver.beaverutils.configuration.gui.buttons;

import dev.bebomny.beaver.beaverutils.client.BeaverUtilsClient;
import dev.bebomny.beaver.beaverutils.configuration.ConfigurationMenu;
import dev.bebomny.beaver.beaverutils.features.features.Reach;
import dev.bebomny.beaver.beaverutils.helpers.TextUtils;
import net.minecraft.client.gui.tooltip.Tooltip;
import net.minecraft.client.gui.widget.AxisGridWidget;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.text.Text;

public class ReachButton extends AxisGridWidget {

    private Reach reach = BeaverUtilsClient.getInstance().features.reach;

    public ReachButton(int x, int y) {
        super(ConfigurationMenu.STANDARD_WIDTH, 20, DisplayAxis.HORIZONTAL);//219
        this.getMainPositioner().alignLeft();

        ButtonWidget reachEnable = ButtonWidget.builder(
                TextUtils.getEnabledDisabledText(reach.getName(), reach.isEnabled()),
                button -> {
                    reach.setEnabled(!reach.isEnabled());
                    button.setMessage(TextUtils.getEnabledDisabledText(reach.getName(), reach.isEnabled()));
                }
        ).width(130).tooltip(Tooltip.of(Text.of("§l§4THIS FEATURE IS DEPRECATED!!! Do not use it, it doesnt work!"))).build();

        ButtonWidget reachDisplay = ButtonWidget.builder(
                Text.of(reach.getDistance() + " Blocks"),
                button -> {
                    //reset to default
                    reach.setDistance(5.0f);
                    button.setMessage(Text.of(reach.getDistance() + " Blocks"));
                }
        ).size(65, 20).position(130 + 2, 0).build();

        ButtonWidget reachPlus = ButtonWidget.builder(
                Text.of("§l+"),
                button -> {
                    //increase the distance
                    reach.setDistance(reach.getDistance() + 0.5f);
                    reachDisplay.setMessage(Text.of(reach.getDistance() + " Blocks"));
                }
        ).size(20, 20).position(130 + 2 + 65 + 2, 0).build();

        ButtonWidget reachMinus = ButtonWidget.builder(
                Text.of("§l-"),
                button -> {
                    reach.setDistance(reach.getDistance() - 0.5f);
                    reachDisplay.setMessage(Text.of(reach.getDistance() + " Blocks"));
                }
        ).size(20, 20).position(130 + 2 + 65 + 2 + 20 + 2, 0).build();

        this.add(reachEnable);
        this.add(reachDisplay);
        this.add(reachPlus);
        this.add(reachMinus);

        this.getMainPositioner().alignLeft();
        this.setPosition(x, y);
    }
}
