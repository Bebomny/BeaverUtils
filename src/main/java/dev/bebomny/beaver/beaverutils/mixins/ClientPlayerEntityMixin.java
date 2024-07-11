package dev.bebomny.beaver.beaverutils.mixins;

import com.mojang.authlib.GameProfile;
import dev.bebomny.beaver.beaverutils.client.BeaverUtilsClient;
import net.minecraft.client.network.AbstractClientPlayerEntity;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.client.world.ClientWorld;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(ClientPlayerEntity.class)
public class ClientPlayerEntityMixin extends AbstractClientPlayerEntity {

    public ClientPlayerEntityMixin(ClientWorld world, GameProfile profile) {
        super(world, profile);
    }

    @Override
    public double getBlockInteractionRange() {
        if(!BeaverUtilsClient.getInstance().features.reach.isEnabled())
            return super.getBlockInteractionRange();

        return BeaverUtilsClient.getInstance().features.reach.getDistance();
    }

    @Override
    public double getEntityInteractionRange() {
        if(!BeaverUtilsClient.getInstance().features.reach.isEnabled())
            return super.getBlockInteractionRange();

        return BeaverUtilsClient.getInstance().features.reach.getDistance();
    }
}
