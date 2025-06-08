package eu.hexasis.helixmarkers.mixin;

import eu.hexasis.helixmarkers.HelixMarkers;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(World.class)
public class ServerWorldMixin {

    @Unique
    private final World world = (World) (Object) this;

    @Inject(method = "setBlockState(Lnet/minecraft/util/math/BlockPos;Lnet/minecraft/block/BlockState;II)Z", at = @At("HEAD"))
    public void onBlockChanged(BlockPos pos, BlockState state, int flags, int maxUpdateDepth, CallbackInfoReturnable<Boolean> cir) {
        var currentBlockState = world.getBlockState(pos);
        // check if nether portal block was broken
        if (currentBlockState.isOf(Blocks.NETHER_PORTAL) && !state.isOf(Blocks.NETHER_PORTAL)) {
            // try to remove marker
            HelixMarkers.api().removeSimpleMarker(world.getRegistryKey().getValue(), "nether_portals", pos);
        }
    }

}
