package eu.hexasis.helixmarkers.mixin;

import eu.hexasis.helixmarkers.MarkerUtils;
import net.minecraft.block.entity.BeaconBlockEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(BeaconBlockEntity.class)
public class BeaconBlockEntityMixin {

    @Inject(method = "updateLevel", at = @At("RETURN"))
    private static void tick(World world, int x, int y, int z, CallbackInfoReturnable<Integer> cir) {
        MarkerUtils.addMarker(world.getRegistryKey().getValue(), "beacons", new BlockPos(x, y, z));
//        System.out.println(cir.getReturnValue());
    }

}
