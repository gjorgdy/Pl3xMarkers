package eu.hexasis.helixmarkers.mixin;

import eu.hexasis.helixmarkers.MarkerUtils;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.world.BlockLocating;
import net.minecraft.world.PortalForcer;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.Optional;

@Mixin(PortalForcer.class)
public class PortalForcerMixin {

    @Final
    @Shadow
    private ServerWorld world;

    @Inject(method = "createPortal", at = @At("RETURN"))
    public void createPortal(BlockPos pos, Direction.Axis axis, CallbackInfoReturnable<Optional<BlockLocating.Rectangle>> cir) {
        Identifier identifier = world.getRegistryKey().getValue();
        if (cir.getReturnValue().isEmpty()) return;
        BlockPos corner = cir.getReturnValue().get().lowerLeft;
        BlockPos center =
            (axis == Direction.Axis.X)
                ? corner.add(1, 0, 0)
                : corner.add(0, 0, 1);
        MarkerUtils.addSimpleMarker(identifier, "nether_portals", center);
    }

}
