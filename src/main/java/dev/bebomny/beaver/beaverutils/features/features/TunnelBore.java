package dev.bebomny.beaver.beaverutils.features.features;

import dev.bebomny.beaver.beaverutils.configuration.config.TunnelBoreConfig;
import dev.bebomny.beaver.beaverutils.configuration.gui.menus.TunnelBoreMenu;
import dev.bebomny.beaver.beaverutils.features.KeyOnOffFeature;
import org.lwjgl.glfw.GLFW;

public class TunnelBore extends KeyOnOffFeature {

    private TunnelBoreConfig tunnelBoreConfig = config.tunnelBoreConfig;

    public TunnelBore() {
        super("TunnelBore");

        addActivationKeybinding(GLFW.GLFW_KEY_UNKNOWN);
        setEnableConfig(tunnelBoreConfig);
        setOptionsMenu(new TunnelBoreMenu());
    }
}
