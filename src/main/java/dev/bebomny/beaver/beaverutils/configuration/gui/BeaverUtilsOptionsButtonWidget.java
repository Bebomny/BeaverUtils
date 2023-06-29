package dev.bebomny.beaver.beaverutils.configuration.gui;

import dev.bebomny.beaver.beaverutils.client.BeaverUtilsClient;
import dev.bebomny.beaver.beaverutils.configuration.ConfigurationMenu;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.text.Text;

public class BeaverUtilsOptionsButtonWidget extends ButtonWidget {

    public BeaverUtilsOptionsButtonWidget(Screen screen) {
        super(
                10, screen.height - 40,
                128, 20,
                Text.of("BeaverUtils Options"),
                button -> {
                    BeaverUtilsClient.getInstance().client.setScreen(new ConfigurationMenu(screen));
                    BeaverUtilsClient.getInstance().LOGGER.atInfo().log("Opening BeaverUtils Options Screen");
                },
                ButtonWidget.DEFAULT_NARRATION_SUPPLIER);
    }
}
