package eu.hexasis.helixmarkers.mixin;

import eu.hexasis.helixmarkers.HelixMarkers;
import eu.hexasis.helixmarkers.interfaces.BeaconBlockEntityInterface;
import net.minecraft.block.entity.BeaconBlockEntity;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(BeaconBlockEntity.class)
public class BeaconBlockEntityMixin implements BeaconBlockEntityInterface {

    @Shadow
    int level;

    @Inject(method = "updateLevel", at = @At("RETURN"))
    private static void updateLevel(World world, int x, int y, int z, CallbackInfoReturnable<Integer> cir) {
        BlockPos blockPos = new BlockPos(x, y, z);
        BlockEntity blockEntity = world.getBlockEntity(blockPos);
        if (blockEntity instanceof BeaconBlockEntityInterface beaconBlockEntityInterface
                && cir.getReturnValue() != beaconBlockEntityInterface.helixMarkers$getLevel()
        ) {
            if (cir.getReturnValue() > 0) {
                HelixMarkers.api().addIconMarker(world.getRegistryKey().getValue(), "beacons", blockPos);
            } else {
                HelixMarkers.api().removeIconMarker(world.getRegistryKey().getValue(), "beacons", blockPos);
            }
        }
    }

    @Override
    public int helixMarkers$getLevel() {
        return this.level;
    }

}
