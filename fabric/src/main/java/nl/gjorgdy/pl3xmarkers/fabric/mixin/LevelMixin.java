package nl.gjorgdy.pl3xmarkers.fabric.mixin;

import net.minecraft.core.BlockPos;
import net.minecraft.tags.BlockTags;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import nl.gjorgdy.pl3xmarkers.core.Pl3xMarkersCore;
import nl.gjorgdy.pl3xmarkers.core.layers.NetherPortalMarkerLayer;
import nl.gjorgdy.pl3xmarkers.core.layers.SignsMarkerLayer;
import nl.gjorgdy.pl3xmarkers.core.registries.Layers;
import nl.gjorgdy.pl3xmarkers.fabric.helpers.FeedbackHelper;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(Level.class)
public class LevelMixin {

    @Unique
    private final Level level = (Level) (Object) this;

    @Inject(method = "setBlock(Lnet/minecraft/core/BlockPos;Lnet/minecraft/world/level/block/state/BlockState;II)Z", at = @At("HEAD"))
    public void onBlockChanged(BlockPos pos, BlockState blockState, int updateFlags, int updateLimit, CallbackInfoReturnable<Boolean> cir) {
        var currentBlockState = level.getBlockState(pos);

        if (currentBlockState.is(Blocks.NETHER_PORTAL) && !blockState.is(Blocks.NETHER_PORTAL)) {
            var result = Pl3xMarkersCore.api()
                    .getWorld(level.dimension().identifier().toString())
                    .getLayer(NetherPortalMarkerLayer.class, Layers.Keys.NETHER_PORTALS)
                    .remove(pos.getX(), pos.getY(), pos.getZ());
            FeedbackHelper.sendFeedback(result, level, pos);
        }

        if (currentBlockState.is(BlockTags.STANDING_SIGNS) && !blockState.is(BlockTags.STANDING_SIGNS)) {
            var result = Pl3xMarkersCore.api()
                    .getWorld(level.dimension().identifier().toString())
                    .getLayer(SignsMarkerLayer.class, Layers.Keys.SIGNS)
                    .remove(pos.getX(), pos.getY(), pos.getZ());
            FeedbackHelper.sendFeedback(result, level, pos);
        }
    }

}
