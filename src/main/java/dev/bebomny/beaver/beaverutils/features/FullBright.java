package dev.bebomny.beaver.beaverutils.features;

import dev.bebomny.beaver.beaverutils.client.BeaverUtilsClient;
import dev.bebomny.beaver.beaverutils.mixinterface.ISimpleOption;
import net.minecraft.client.MinecraftClient;
import net.minecraft.text.Text;

public class FullBright extends Feature{

    private final MinecraftClient client;
    private final BeaverUtilsClient modBeaverUtils;

    public FullBright(MinecraftClient client, BeaverUtilsClient modBeaverUtils) {
        super("FullBright");
        this.client = client;
        this.modBeaverUtils = modBeaverUtils;
    }

    private void setGamma(double value) {
        @SuppressWarnings("unchecked")
        ISimpleOption<Double> gammaOption =
                (ISimpleOption<Double>)(Object) BeaverUtilsClient.getInstance().client.options.getGamma();
        gammaOption.forceSetValue(value);
    }

    @Override
    public void onEnable() {
        setGamma(16.0f);
        modBeaverUtils.notifier.newNotification(Text.literal("FullBright Enabled"));
    }

    @Override
    public void onDisable() {
        setGamma(1.0f);
        modBeaverUtils.notifier.newNotification(Text.literal("FullBright Disabled"));
    }
}
