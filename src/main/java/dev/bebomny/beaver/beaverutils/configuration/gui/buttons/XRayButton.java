package dev.bebomny.beaver.beaverutils.configuration.gui.buttons;

import dev.bebomny.beaver.beaverutils.client.BeaverUtilsClient;
import dev.bebomny.beaver.beaverutils.features.features.XRay;
import dev.bebomny.beaver.beaverutils.helpers.TextUtils;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.text.Text;

public class XRayButton extends ButtonWidget {

    public XRayButton() {
        super(
                0, 0,
                128, 20,
                TextUtils.getEnabledDisabledText(
                        BeaverUtilsClient.getInstance().features.xRay.getName(),
                        BeaverUtilsClient.getInstance().features.xRay.isEnabled()
                ),
                button -> {
                    XRay xRay = BeaverUtilsClient.getInstance().features.xRay;
                    xRay.setEnabled(!xRay.isEnabled());
                    button.setMessage(
                            TextUtils.getEnabledDisabledText(
                                    BeaverUtilsClient.getInstance().features.xRay.getName(),
                                    BeaverUtilsClient.getInstance().features.xRay.isEnabled())
                    );
                },
                DEFAULT_NARRATION_SUPPLIER
        );
    }
}
