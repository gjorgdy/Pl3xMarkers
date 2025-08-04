package eu.hexasis.helixmarkers.mixin;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.RailBlock;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(RailBlock.class)
public class RailBlockMixin {

    @Inject(method = "updateBlockState", at = @At("HEAD"))
    protected void onUpdateBlockState(BlockState state, World world, BlockPos pos, Block neighbor, CallbackInfo ci) {
//        System.out.println("updating");
    }

}
