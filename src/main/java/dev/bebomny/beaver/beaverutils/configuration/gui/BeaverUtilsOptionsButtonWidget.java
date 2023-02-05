package dev.bebomny.beaver.beaverutils.configuration.gui;

import dev.bebomny.beaver.beaverutils.configuration.ConfigurationMenu;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.text.Text;

public class BeaverUtilsOptionsButtonWidget extends ButtonWidget {

    public BeaverUtilsOptionsButtonWidget(Screen screen) {
        super(
                10, screen.height - 40,
                128, 20,
                Text.literal("BeaverUtils Options"),
                button -> MinecraftClient.getInstance().setScreen(new ConfigurationMenu(screen, MinecraftClient.getInstance().options)),
                ButtonWidget.DEFAULT_NARRATION_SUPPLIER);
    }

    public BeaverUtilsOptionsButtonWidget(int x, int y, int width, int height, Text text, Screen screen) {
        super(
                x, y,
                width, height,
                text,
                button -> MinecraftClient.getInstance().setScreen(new ConfigurationMenu(screen, MinecraftClient.getInstance().options)),
                ButtonWidget.DEFAULT_NARRATION_SUPPLIER);
    }
}
