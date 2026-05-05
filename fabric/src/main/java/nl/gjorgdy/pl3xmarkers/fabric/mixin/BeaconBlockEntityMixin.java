package nl.gjorgdy.pl3xmarkers.fabric.mixin;

import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BeaconBlockEntity;
import net.minecraft.world.level.block.entity.BlockEntity;
import nl.gjorgdy.pl3xmarkers.core.Pl3xMarkersCore;
import nl.gjorgdy.pl3xmarkers.core.layers.BeaconMarkerLayer;
import nl.gjorgdy.pl3xmarkers.core.registries.Layers;
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
    private int levels;

    @Inject(method = "updateBase", at = @At("RETURN"))
    private static void updateLevel(Level level, int x, int y, int z, CallbackInfoReturnable<Integer> cir) {
        BlockPos blockPos = new BlockPos(x, y, z);
	    BlockEntity blockEntity = level.getBlockEntity(blockPos);
        if (blockEntity instanceof BeaconBlockEntityInterface beaconBlockEntityInterface
                && cir.getReturnValue() != beaconBlockEntityInterface.helixMarkers$getLevel()
        ) {
	        var markerLayer = Pl3xMarkersCore.api()
			        .getWorld(level.dimension().identifier().toString())
			        .getLayer(BeaconMarkerLayer.class, Layers.Keys.BEACONS);
	        var result = cir.getReturnValue() > 0
			        ? markerLayer.add(blockPos.getX(), blockPos.getY(), blockPos.getZ())
			        : markerLayer.remove(blockPos.getX(), blockPos.getY(), blockPos.getZ());
	        FeedbackHelper.sendFeedback(result, level, blockPos);
        }
    }

    @Override
    public int helixMarkers$getLevel() {
	    return levels;
    }

}
