package eu.hexasis.helixmarkers.mixin;

import eu.hexasis.helixmarkers.MarkerUtils;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.world.WorldAccess;
import net.minecraft.world.dimension.NetherPortal;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.Optional;

@Mixin(NetherPortal.class)
public class NetherPortalMixin {

    @Final
    @Shadow
    private Direction.Axis axis;

    @Shadow
    private BlockPos lowerCorner;

    @Final
    @Shadow
    private WorldAccess world;

    @Shadow
    private int foundPortalBlocks;

    @Shadow @Final private int width;

    @Inject(method = "<init>", at = @At("RETURN"))
    private void constructor(CallbackInfo ci) {
        if (world instanceof ServerWorld sw) {
            Identifier identifier = sw.getRegistryKey().getValue();
            if (foundPortalBlocks == 0 && getSelf().isValid()) {
                MarkerUtils.addMarker(identifier, "nether_portals", getCenter());
            } else {
                MarkerUtils.removeMarker(identifier, "nether_portals", getCenter());
            }
        }
    }

    @Unique
    private NetherPortal getSelf() {
        return (NetherPortal) (Object) this;
    }

    @Unique
    private BlockPos getCenter() {
        if (lowerCorner == null) return null;
        if (axis == Direction.Axis.X) {
            return lowerCorner.add(width / 2, 0, 0);
        } else {
            return lowerCorner.add(0, 0, width / 2);
        }
    }

}
