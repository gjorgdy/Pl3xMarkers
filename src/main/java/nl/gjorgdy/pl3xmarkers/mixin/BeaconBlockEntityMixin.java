package nl.gjorgdy.pl3xmarkers.mixin;

import nl.gjorgdy.pl3xmarkers.Pl3xMarkers;
import nl.gjorgdy.pl3xmarkers.interfaces.BeaconBlockEntityInterface;
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
                Pl3xMarkers.api().addBeaconIconMarker(world.getRegistryKey().getValue(), blockPos);
            } else {
                Pl3xMarkers.api().removeBeaconIconMarker(world.getRegistryKey().getValue(), blockPos);
            }
        }
    }

    @Override
    public int helixMarkers$getLevel() {
        return this.level;
    }

}
