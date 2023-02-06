package dev.bebomny.beaver.beaverutils.features;

import dev.bebomny.beaver.beaverutils.client.BeaverUtilsClient;
import dev.bebomny.beaver.beaverutils.helpers.Notification;
import dev.bebomny.beaver.beaverutils.mixinterface.ISimpleOption;
import net.minecraft.client.MinecraftClient;
import net.minecraft.text.Text;

import java.awt.*;

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
        modBeaverUtils.notifier.newNotification(new Notification(Text.literal("FullBright Enabled"), new Color(0x00FF00)));
    }

    @Override
    public void onDisable() {
        setGamma(1.0f);
        modBeaverUtils.notifier.newNotification(new Notification(Text.literal("FullBright Disabled"), new Color(0xFF0000)));
    }
}
