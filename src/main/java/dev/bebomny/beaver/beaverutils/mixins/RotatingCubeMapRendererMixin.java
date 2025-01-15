package dev.bebomny.beaver.beaverutils.mixins;

import net.minecraft.client.gui.RotatingCubeMapRenderer;
import net.minecraft.util.Identifier;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Mutable;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Constant;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyConstant;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(RotatingCubeMapRenderer.class)
public class RotatingCubeMapRendererMixin {

    // Shadow the original field to replace its value
    @Mutable
    @Shadow
    private static Identifier OVERLAY_TEXTURE;

    // Inject at the class initialization to change the texture
    @Inject(method = "<clinit>", at = @At("TAIL"))
    private static void onClassInit(CallbackInfo ci) {
        OVERLAY_TEXTURE = Identifier.of("beaverutils", "panorama.png");
    }

}
