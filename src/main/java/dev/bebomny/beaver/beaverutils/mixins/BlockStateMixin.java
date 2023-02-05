package dev.bebomny.beaver.beaverutils.mixins;

import dev.bebomny.beaver.beaverutils.client.BeaverUtilsClient;
import net.minecraft.block.AbstractBlock.AbstractBlockState;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.SideShapeType;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.shape.VoxelShape;
import net.minecraft.util.shape.VoxelShapes;
import net.minecraft.world.BlockView;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(AbstractBlockState.class)
public abstract class BlockStateMixin {
    @Shadow public abstract Block getBlock();

    @Inject(at = @At("HEAD"), method = "isSideInvisible", cancellable = true)
    public void isSideInvisible(BlockState state, Direction direction, CallbackInfoReturnable<Boolean> cir) {
        if(BeaverUtilsClient.getInstance().xRay.isEnabled() && BeaverUtilsClient.getInstance().xRay.isActive()) {
            if(BeaverUtilsClient.getInstance().xRay.isInterestingBlock(this.getBlock())) {
                cir.setReturnValue(false);
                return;
            }
            cir.setReturnValue(true);
            return;
        }
    }

    @Inject(at = @At("HEAD"), method = "isSideSolid", cancellable = true)
    public void isSideSolid(BlockView world, BlockPos pos, Direction direction, SideShapeType shapeType, CallbackInfoReturnable<Boolean> cir) {
        if(BeaverUtilsClient.getInstance().xRay.isEnabled() && BeaverUtilsClient.getInstance().xRay.isActive()) {
            if(BeaverUtilsClient.getInstance().xRay.isInterestingBlock(this.getBlock())) {
                cir.setReturnValue(true);
                return;
            }
            cir.setReturnValue(false);
            return;
        }
    }

    @Inject(at = @At("HEAD"), method = "getLuminance", cancellable = true)
    public void getLuminance(CallbackInfoReturnable<Integer> cir) {
        if(BeaverUtilsClient.getInstance().xRay.isEnabled() && BeaverUtilsClient.getInstance().xRay.isActive()) {
            if(BeaverUtilsClient.getInstance().xRay.isInterestingBlock(this.getBlock())) {
                cir.setReturnValue(12);
                return;
            }

        }
    }

    @Inject(at = @At("HEAD"), method = "getAmbientOcclusionLightLevel", cancellable = true)
    public void getAmbientOcclusionLightLevel(BlockView world, BlockPos pos, CallbackInfoReturnable<Float> cir) {
        if(BeaverUtilsClient.getInstance().xRay.isEnabled() && BeaverUtilsClient.getInstance().xRay.isActive()) {
            if(BeaverUtilsClient.getInstance().xRay.isInterestingBlock(this.getBlock())) {
                cir.setReturnValue(1.0f);
                return;
            }
            cir.setReturnValue(1.0f);
            return;
        }
    }

    @Inject(at = @At("HEAD"), method = "getCullingFace", cancellable = true)
    public void getCullingFace(BlockView world, BlockPos pos, Direction direction, CallbackInfoReturnable<VoxelShape> cir) {
        if(BeaverUtilsClient.getInstance().xRay.isEnabled() && BeaverUtilsClient.getInstance().xRay.isActive()) {
            if(BeaverUtilsClient.getInstance().xRay.isInterestingBlock(this.getBlock())) {
                cir.setReturnValue(VoxelShapes.fullCube());
                return;
            }
            cir.setReturnValue(VoxelShapes.empty());
            return;
        }
    }
}
