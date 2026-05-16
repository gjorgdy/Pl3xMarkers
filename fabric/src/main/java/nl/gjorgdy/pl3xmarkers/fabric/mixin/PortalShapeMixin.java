package nl.gjorgdy.pl3xmarkers.fabric.mixin;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.portal.PortalShape;
import nl.gjorgdy.pl3xmarkers.core.Pl3xMarkersCore;
import nl.gjorgdy.pl3xmarkers.core.layers.NetherPortalMarkerLayer;
import nl.gjorgdy.pl3xmarkers.core.registries.Layers;
import nl.gjorgdy.pl3xmarkers.fabric.helpers.FeedbackHelper;
import nl.gjorgdy.pl3xmarkers.fabric.helpers.PortalHelper;
import nl.gjorgdy.pl3xmarkers.fabric.interfaces.NetherPortalInterface;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.Optional;

@Mixin(PortalShape.class)
public class PortalShapeMixin implements NetherPortalInterface {

    @Final
    @Shadow
    private Direction.Axis axis;

    @Final
    @Shadow
    private BlockPos bottomLeft;

    @Shadow
    @Final
    private int width;

    @Inject(method = "findEmptyPortalShape", at = @At("RETURN"))
    private static void onNewPortal(LevelAccessor level, BlockPos pos, Direction.Axis preferredAxis, CallbackInfoReturnable<Optional<PortalShape>> cir) {
        cir.getReturnValue().ifPresent(netherPortal -> {
            if (netherPortal instanceof NetherPortalInterface np) {
                np.pl3xMarkers$createMarker((Level) level);
            }
        });
    }

    @Override
    public void pl3xMarkers$createMarker(Level world) {
        var center = PortalHelper.getNetherPortalCenter(bottomLeft, axis, width);
        var markerLayer = Pl3xMarkersCore.api()
                .getWorld(world.dimension().identifier().toString())
                .getLayer(NetherPortalMarkerLayer.class, Layers.Keys.NETHER_PORTALS);
        if (markerLayer == null) {
            return;
        }
        var result = markerLayer.add(center.getX(), center.getY(), center.getZ());
		FeedbackHelper.sendFeedback(result, world, center);
	}

}
