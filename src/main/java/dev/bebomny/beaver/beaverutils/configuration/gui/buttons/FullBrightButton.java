package dev.bebomny.beaver.beaverutils.configuration.gui.buttons;

import dev.bebomny.beaver.beaverutils.client.BeaverUtilsClient;
import dev.bebomny.beaver.beaverutils.helpers.TextUtils;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.text.Text;

public class FullBrightButton extends ButtonWidget {

    public FullBrightButton(int x, int y) {
        super(
                x, y,
                128, 20,
                TextUtils.getEnabledDisabledText(BeaverUtilsClient.getInstance().features.fullBright.getName(), BeaverUtilsClient.getInstance().features.fullBright.isEnabled()),
                button -> {
                    BeaverUtilsClient.getInstance().features.fullBright.setEnabled(!BeaverUtilsClient.getInstance().features.fullBright.isEnabled());
                    button.setMessage(TextUtils.getEnabledDisabledText(BeaverUtilsClient.getInstance().features.fullBright.getName(), BeaverUtilsClient.getInstance().features.fullBright.isEnabled()));
                }
        );
    }
}
