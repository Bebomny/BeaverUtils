package dev.bebomny.beaver.beaverutils.mixins;

import dev.bebomny.beaver.beaverutils.client.BeaverUtilsClient;
import dev.bebomny.beaver.beaverutils.mixinterface.IMinecraftClientInvoker;
import net.minecraft.client.MinecraftClient;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(MinecraftClient.class)
public abstract class MinecraftClientMixin implements IMinecraftClientInvoker {

    @Shadow protected abstract boolean doAttack();
    @Shadow protected abstract void doItemUse();

    @Override
    public boolean invokeDoAttack() {
        return this.doAttack();
    }

    @Override
    public void invokeDoItemUse() {
        this.doItemUse();
    }

    @Inject(method = "doAttack", at = @At("HEAD"))
    public void onDoAttack(CallbackInfoReturnable<Boolean> cir) {
        BeaverUtilsClient.getInstance().reach.onAttack();
    }

    @Inject(method = "doItemUse", at = @At("HEAD"))
    public void onDoItemUse(CallbackInfo ci) {

    }
}
