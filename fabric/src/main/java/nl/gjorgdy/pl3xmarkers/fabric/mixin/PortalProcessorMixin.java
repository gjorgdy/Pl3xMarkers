package nl.gjorgdy.pl3xmarkers.fabric.mixin;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.PortalProcessor;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.portal.PortalShape;
import net.minecraft.world.level.portal.TeleportTransition;
import nl.gjorgdy.pl3xmarkers.core.Pl3xMarkersCore;
import nl.gjorgdy.pl3xmarkers.core.layers.EndGatewayMarkerLayer;
import nl.gjorgdy.pl3xmarkers.core.layers.EndPortalMarkerLayer;
import nl.gjorgdy.pl3xmarkers.core.objects.InteractionResult;
import nl.gjorgdy.pl3xmarkers.core.registries.Layers;
import nl.gjorgdy.pl3xmarkers.fabric.helpers.FeedbackHelper;
import nl.gjorgdy.pl3xmarkers.fabric.helpers.PortalHelper;
import nl.gjorgdy.pl3xmarkers.fabric.interfaces.NetherPortalInterface;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(PortalProcessor.class)
public class PortalProcessorMixin {

	@Shadow
	private BlockPos entryPosition;

	// On a player traveling through a nether portal, try to create a marker
	@Inject(method = "getPortalDestination", at = @At("RETURN"))
	public void onTick(ServerLevel serverLevel, Entity entity, CallbackInfoReturnable<TeleportTransition> cir) {
		// origin
		tryCreateMarker(serverLevel, entryPosition, entity);
		// destination
		tryCreateMarker(cir.getReturnValue().newLevel(), BlockPos.containing(cir.getReturnValue().position()), entity);
	}

	@Unique
	private void tryCreateMarker(Level world, BlockPos pos, Entity entity) {
		BlockState portalBlock = world.getBlockState(pos);
		InteractionResult result = InteractionResult.skip();
		if (portalBlock.is(Blocks.NETHER_PORTAL)) {
			Direction.Axis axis = portalBlock.getValue(BlockStateProperties.HORIZONTAL_AXIS);
			var portal = (NetherPortalInterface) PortalShape.findAnyShape(world, pos, axis);
			portal.pl3xMarkers$createMarker(world);
		}
		if (portalBlock.is(Blocks.END_GATEWAY)) {
			Pl3xMarkersCore.api()
					.getWorld(world.dimension().identifier().toString())
					.getLayer(EndGatewayMarkerLayer.class, Layers.Keys.END_GATEWAYS)
					.add(pos.getX(), pos.getY(), pos.getZ());
		}
		if (portalBlock.is(Blocks.END_PORTAL)) {
			pos = PortalHelper.getEndPortalCenter(world, pos);
			Pl3xMarkersCore.api()
					.getWorld(world.dimension().identifier().toString())
					.getLayer(EndPortalMarkerLayer.class, Layers.Keys.END_PORTALS)
					.add(pos.getX(), pos.getY(), pos.getZ());
		}
		if (entity instanceof ServerPlayer serverPlayer) {
			FeedbackHelper.sendFeedback(result, serverPlayer);
		}
	}

}
