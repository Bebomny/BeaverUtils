package dev.bebomny.beaver.beaverutils.configuration.gui.buttons;

import dev.bebomny.beaver.beaverutils.client.BeaverUtilsClient;
import dev.bebomny.beaver.beaverutils.features.TriggerFeature;
import dev.bebomny.beaver.beaverutils.features.features.QuickTeleport;
import net.minecraft.client.gui.tooltip.Tooltip;
import net.minecraft.client.gui.widget.AxisGridWidget;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.text.Text;

public class QuickTeleportButton extends AxisGridWidget {

    private QuickTeleport quickTeleport = BeaverUtilsClient.getInstance().features.quickTeleport;

    public QuickTeleportButton() {
        super(128, 20 ,DisplayAxis.HORIZONTAL);
        this.getMainPositioner().alignRight(); //maybe chnage this? //.marginX(2)

        Tooltip tooltip = Tooltip.of(Text.of("§l§c(NOT YET IMPLEMENTED) §r Teleports you in the direction you are facing!"));

        ButtonWidget teleportDisplayButton = ButtonWidget.builder(
                Text.of("Quick TP: §r" + (quickTeleport.getDistance())), //do something chnage numebr ec.
                button -> {
                    quickTeleport.fire(TriggerFeature.FiredBy.MENU);
                }
        ).width(84).tooltip(tooltip).build();

        ButtonWidget teleportPlusButton = ButtonWidget.builder(
                Text.of("§l+"),
                button -> {
                    //increase the tp distance
                    quickTeleport.setDistance(quickTeleport.getDistance() + 1);
                    teleportDisplayButton.setMessage(Text.of("Quick TP: §r" + (quickTeleport.getDistance())));
                    //if above certain level display a warning thath it is too far
                }
        ).size(20, 20).build();

        ButtonWidget teleportMinusButton = ButtonWidget.builder(
                Text.of("§l-"),
                button -> {
                    //decrease the tp distance
                    quickTeleport.setDistance(quickTeleport.getDistance() - 1);
                    teleportDisplayButton.setMessage(Text.of("Quick TP: §r" + (quickTeleport.getDistance())));
                    //if below certain level display a warning that it will tp backwards
                }
        ).size(20, 20).build();

        this.add(teleportDisplayButton);
        this.add(teleportPlusButton);
        this.add(teleportMinusButton);
        this.recalculateDimensions();
    }
}
