package nl.gjorgdy.pl3xmarkers.mixin;

import nl.gjorgdy.pl3xmarkers.Pl3xMarkers;
import nl.gjorgdy.pl3xmarkers.helpers.PortalHelper;
import nl.gjorgdy.pl3xmarkers.interfaces.NetherPortalInterface;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.entity.Entity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.state.property.Properties;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.world.TeleportTarget;
import net.minecraft.world.World;
import net.minecraft.world.dimension.NetherPortal;
import net.minecraft.world.dimension.PortalManager;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(PortalManager.class)
public class PortalManagerMixin {

    @Shadow private BlockPos pos;

    // On a player traveling through a nether portal, try to create a marker
    @Inject(method = "createTeleportTarget", at =@At("RETURN"))
    public void onTick(ServerWorld world, Entity entity, CallbackInfoReturnable<TeleportTarget> cir) {
        // origin
        tryCreateMarker(world, this.pos);
        // destination
        tryCreateMarker(cir.getReturnValue().world(), BlockPos.ofFloored(cir.getReturnValue().position()));
    }

    @Unique
    private void tryCreateMarker(World world, BlockPos pos) {
        BlockState portalBlock = world.getBlockState(pos);
        if (portalBlock.isOf(Blocks.NETHER_PORTAL)) {
            Direction.Axis axis = portalBlock.get(Properties.HORIZONTAL_AXIS);
            var portal = (NetherPortalInterface) NetherPortal.getOnAxis(world, pos, axis);
            portal.pl3xMarkers$createMarker(world);
        }
        if (portalBlock.isOf(Blocks.END_GATEWAY)) {
            Pl3xMarkers.api().addEndGatewayIconMarker(world.getRegistryKey().getValue(), pos);
        }
        if (portalBlock.isOf(Blocks.END_PORTAL)) {
            Pl3xMarkers.api().addEndPortalIconMarker(world.getRegistryKey().getValue(), PortalHelper.getEndPortalCenter(world, pos));
        }
    }

}
