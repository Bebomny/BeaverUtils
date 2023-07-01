package dev.bebomny.beaver.beaverutils.configuration.gui.buttons;

import dev.bebomny.beaver.beaverutils.client.BeaverUtilsClient;
import dev.bebomny.beaver.beaverutils.configuration.config.AutoPlantConfig;
import dev.bebomny.beaver.beaverutils.features.features.AutoPlant;
import dev.bebomny.beaver.beaverutils.helpers.TextUtils;
import net.minecraft.client.gui.tooltip.Tooltip;
import net.minecraft.client.gui.widget.AxisGridWidget;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.text.Text;

public class AutoPlantButton extends AxisGridWidget {

    private AutoPlantConfig autoPlantConfig = BeaverUtilsClient.getInstance().getConfig().autoPlantConfig;
    private AutoPlant autoPlant = BeaverUtilsClient.getInstance().features.autoPlant;

    public AutoPlantButton(int x, int y) {
        super(128, 20 , DisplayAxis.HORIZONTAL);

        ButtonWidget mainButton = ButtonWidget.builder(
                TextUtils.getEnabledDisabledText(autoPlant.getName(), autoPlant.isEnabled()),
                button -> {
                    autoPlant.setEnabled(!autoPlant.isEnabled());
                    button.setMessage(TextUtils.getEnabledDisabledText(autoPlant.getName(), autoPlant.isEnabled()));
                }
        ).dimensions(0, 0, 128, 20).build();

        Tooltip plantRadiusTooltip = Tooltip.of(Text.of("§cFor now its only possible to change this directly in the config!"));

        ButtonWidget plantRadiusButton = ButtonWidget.builder(
                Text.of("Plant Radius: " + autoPlant.getPlantRadius()),
                button -> {
                    //can change in the config for now
                    button.setMessage(Text.of("Plant Radius: " + autoPlant.getPlantRadius()));
                }
        ).dimensions( -2 - 90, 0, 90, 20).tooltip(plantRadiusTooltip).build();

        //TODO: To fix
        Tooltip notImplementedTooltip = Tooltip.of(Text.of("§cNot Implemented Yet!!!"));

        ButtonWidget modeButton = ButtonWidget.builder(
                Text.of("Mode: " + autoPlant.getMode()),
                button -> {
                    autoPlant.changeMode();
                    button.setMessage(Text.of("Mode: " + autoPlant.getMode()));
                }
        ).dimensions(-164 - 2 - 2 - 90, 0, 164, 20).tooltip(notImplementedTooltip).build();

        this.add(mainButton);
        this.add(plantRadiusButton);
        this.add(modeButton);

        this.getMainPositioner().alignRight();
        this.setPosition(x, y);
    }
}
