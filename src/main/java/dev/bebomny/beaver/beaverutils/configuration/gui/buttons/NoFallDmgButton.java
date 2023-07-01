package dev.bebomny.beaver.beaverutils.configuration.gui.buttons;

import dev.bebomny.beaver.beaverutils.client.BeaverUtilsClient;
import dev.bebomny.beaver.beaverutils.features.features.NoFallDmg;
import dev.bebomny.beaver.beaverutils.helpers.TextUtils;
import net.minecraft.client.gui.widget.ButtonWidget;

public class NoFallDmgButton extends ButtonWidget {

    public NoFallDmgButton(int x, int y) {
        super(
                0, 0,
                128, 20,
                TextUtils.getEnabledDisabledText(
                        BeaverUtilsClient.getInstance().features.noFallDmg.getName(),
                        BeaverUtilsClient.getInstance().features.noFallDmg.isEnabled()
                ),
                button -> {
                    NoFallDmg noFallDmg = BeaverUtilsClient.getInstance().features.noFallDmg;
                    noFallDmg.setEnabled(!noFallDmg.isEnabled());
                    button.setMessage(TextUtils.getEnabledDisabledText(
                            noFallDmg.getName(),
                            noFallDmg.isEnabled()
                    ));
                },
                DEFAULT_NARRATION_SUPPLIER
        );
        this.setPosition(x, y);
    }
}
