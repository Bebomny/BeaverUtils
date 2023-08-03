package dev.bebomny.beaver.beaverutils.features.features;

import dev.bebomny.beaver.beaverutils.client.BeaverUtilsClient;
import dev.bebomny.beaver.beaverutils.configuration.config.FullBrightConfig;
import dev.bebomny.beaver.beaverutils.features.SimpleOnOffFeature;
import dev.bebomny.beaver.beaverutils.mixinterface.ISimpleOption;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.minecraft.client.MinecraftClient;

public class FullBright extends SimpleOnOffFeature {

    FullBrightConfig fullBrightConfig = config.fullBrightConfig;
    private boolean requiresUpdate;

    public FullBright() {
        super("FullBright");

        setEnableConfig(fullBrightConfig);

        if(config.generalConfig.autoEnable)
            setEnabled(fullBrightConfig.enabled);

        ClientTickEvents.START_CLIENT_TICK.register(this::tryUpdateGamma);
    }

    private void setGamma(double newValue) {
        @SuppressWarnings("unchecked")
        ISimpleOption<Double> gammaOption = (ISimpleOption<Double>) (Object) BeaverUtilsClient.getInstance().client.options.getGamma();
        gammaOption.forceSetValue(newValue);
    }

    @Override
    protected void onEnable() {
        requiresUpdate = true;
        super.onEnable();
    }

    private void tryUpdateGamma(MinecraftClient client) {
        if(!requiresUpdate)
            return;

        if(isEnabled())
            setGamma(16.0f);

        if(!isEnabled())
            setGamma(1.0f);

        requiresUpdate = false;
    }

    @Override
    protected void onDisable() {
        requiresUpdate = true;
        super.onDisable();
    }
}
