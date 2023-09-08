package dev.bebomny.beaver.beaverutils.configuration.gui.buttons;

import dev.bebomny.beaver.beaverutils.client.BeaverUtilsClient;
import dev.bebomny.beaver.beaverutils.helpers.TextUtils;
import net.minecraft.client.gui.widget.ButtonWidget;

public class InGameStatsButton extends ButtonWidget {

    public InGameStatsButton(int x, int y) {
        super(
                x, y,
                128, 20,
                TextUtils.getEnabledDisabledText(BeaverUtilsClient.getInstance().features.inGameStats.getName(), BeaverUtilsClient.getInstance().features.inGameStats.isEnabled()),
                button -> {
                    BeaverUtilsClient.getInstance().features.inGameStats.setEnabled(!BeaverUtilsClient.getInstance().features.inGameStats.isEnabled());
                    button.setMessage(TextUtils.getEnabledDisabledText(BeaverUtilsClient.getInstance().features.inGameStats.getName(), BeaverUtilsClient.getInstance().features.inGameStats.isEnabled()));
                }
        );
    }
}
