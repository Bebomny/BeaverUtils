package dev.bebomny.beaver.beaverutils.features.features.autoclicker;

import net.minecraft.text.Text;

public enum MouseButton {
    LEFT,
    RIGHT;

    public Text getDisplayText() {
        return switch (this) {
            case LEFT -> Text.of("Left Mouse Button");
            case RIGHT -> Text.of("Right Mouse Button");
        };
    }
}
