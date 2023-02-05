package dev.bebomny.beaver.beaverutils.mixins;

import dev.bebomny.beaver.beaverutils.client.BeaverUtilsClient;
import net.minecraft.client.network.ClientPlayerInteractionManager;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(ClientPlayerInteractionManager.class)
public class ClientPlayerInteractionManagerMixin {


    @Inject(method = "getReachDistance()F", at = @At("HEAD"), cancellable = true)
    public void onGetReachDistance(CallbackInfoReturnable<Float> cir) {

        BeaverUtilsClient modBeaverUtils = BeaverUtilsClient.getInstance();
        if(!modBeaverUtils.reach.isEnabled())
            return;

        cir.setReturnValue(modBeaverUtils.reach.getReachDistance());
    }

    @Inject(method = "hasExtendedReach()Z", at = @At("HEAD"), cancellable = true)
    public void hasExtendedReach(CallbackInfoReturnable<Boolean> cir) {

        BeaverUtilsClient modBeaverUtils = BeaverUtilsClient.getInstance();
        if(!modBeaverUtils.reach.isEnabled() || !modBeaverUtils.reach.isActive())
            return;

        cir.setReturnValue(true);
    }

}
