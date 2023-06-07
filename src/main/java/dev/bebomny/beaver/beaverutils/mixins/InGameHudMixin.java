package dev.bebomny.beaver.beaverutils.mixins;

import dev.bebomny.beaver.beaverutils.client.BeaverUtilsClient;
import net.minecraft.client.font.TextRenderer;
import net.minecraft.client.gui.hud.InGameHud;
import net.minecraft.client.util.math.MatrixStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(InGameHud.class)
public abstract class InGameHudMixin {

    @Shadow public abstract TextRenderer getTextRenderer();
    @Shadow private int scaledWidth;
    @Shadow private int scaledHeight;

    @Inject(method = "render", at = @At("HEAD"))
    public void onRenderInit(MatrixStack matrices, float tickDelta, CallbackInfo ci) {
        //Call the render function in NotificationHandler -- DONE (I think I want to call this directly not through getNotifier)
        BeaverUtilsClient.getInstance().notifier.onRenderInit(matrices, tickDelta);
    }
}
