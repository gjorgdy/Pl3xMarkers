package nl.gjorgdy.pl3xmarkers.fabric.mixin;

import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BeaconBlockEntity;
import net.minecraft.world.level.block.entity.BlockEntity;
import nl.gjorgdy.pl3xmarkers.core.Pl3xMarkersCore;
import nl.gjorgdy.pl3xmarkers.fabric.helpers.FeedbackHelper;
import nl.gjorgdy.pl3xmarkers.fabric.interfaces.BeaconBlockEntityInterface;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(BeaconBlockEntity.class)
public class BeaconBlockEntityMixin implements BeaconBlockEntityInterface {

    @Shadow
    int levels;

    @Inject(method = "updateBase", at = @At("RETURN"))
    private static void updateLevel(Level world, int x, int y, int z, CallbackInfoReturnable<Integer> cir) {
        BlockPos blockPos = new BlockPos(x, y, z);
        BlockEntity blockEntity = world.getBlockEntity(blockPos);
        if (blockEntity instanceof BeaconBlockEntityInterface beaconBlockEntityInterface
                && cir.getReturnValue() != beaconBlockEntityInterface.helixMarkers$getLevel()
        ) {
			var result = cir.getReturnValue() > 0 ?
				 Pl3xMarkersCore.api().addBeaconIconMarker(world.dimension().identifier().toString(), blockPos.getX(), blockPos.getZ()) :
				 Pl3xMarkersCore.api().removeBeaconIconMarker(world.dimension().identifier().toString(), blockPos.getX(), blockPos.getZ());
			FeedbackHelper.sendFeedback(result, world, blockPos);
        }
    }

    @Override
    public int helixMarkers$getLevel() {
        return this.levels;
    }

}
