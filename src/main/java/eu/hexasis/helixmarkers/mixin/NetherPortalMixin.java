package eu.hexasis.helixmarkers.mixin;

import eu.hexasis.helixmarkers.HelixMarkers;
import eu.hexasis.helixmarkers.helpers.PortalHelper;
import eu.hexasis.helixmarkers.interfaces.NetherPortalInterface;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.world.World;
import net.minecraft.world.WorldAccess;
import net.minecraft.world.dimension.NetherPortal;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.Optional;

@Mixin(NetherPortal.class)
public class NetherPortalMixin implements NetherPortalInterface {

    @Final
    @Shadow
    private Direction.Axis axis;

    @Final
    @Shadow
    private BlockPos lowerCorner;

    @Shadow
    @Final
    private int width;

    @Override
    public void helixMarkers$createMarker(World world) {
        var center = PortalHelper.getNetherPortalCenter(lowerCorner, axis, width);
        HelixMarkers.api().addNetherPortalIconMarker(world.getRegistryKey().getValue(), center);
    }

    @Inject(method = "getNewPortal", at = @At("RETURN"))
    private static void onNewPortal(WorldAccess world, BlockPos pos, Direction.Axis firstCheckedAxis, CallbackInfoReturnable<Optional<NetherPortal>> cir) {
        cir.getReturnValue().ifPresent(netherPortal -> {
            if (netherPortal instanceof NetherPortalInterface np) {
                np.helixMarkers$createMarker((World) world);
            }
        });
    }

}
