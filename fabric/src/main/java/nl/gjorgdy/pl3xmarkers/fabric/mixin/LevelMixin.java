package nl.gjorgdy.pl3xmarkers.fabric.mixin;

import net.minecraft.core.BlockPos;
import net.minecraft.tags.BlockTags;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import nl.gjorgdy.pl3xmarkers.core.Pl3xMarkersCore;
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
    public void onBlockChanged(BlockPos pos, BlockState state, int flags, int maxUpdateDepth, CallbackInfoReturnable<Boolean> cir) {
        var currentBlockState = level.getBlockState(pos);
        // check if a nether portal block was broken
        if (currentBlockState.is(Blocks.NETHER_PORTAL) && !state.is(Blocks.NETHER_PORTAL)) {
            // try to remove marker
            var result = Pl3xMarkersCore.api().removeNetherPortalIconMarker(level.dimension().identifier().toString(),
                                                                            pos.getX(), pos.getZ()
            );
            FeedbackHelper.sendFeedback(result, level, pos);
        }
        if (currentBlockState.is(BlockTags.STANDING_SIGNS) && !state.is(BlockTags.STANDING_SIGNS)) {
            var result = Pl3xMarkersCore.api().removeSignMarker(level.dimension().identifier().toString(), pos.getX(),
                                                                pos.getZ()
            );
            FeedbackHelper.sendFeedback(result, level, pos);
        }
    }

}
