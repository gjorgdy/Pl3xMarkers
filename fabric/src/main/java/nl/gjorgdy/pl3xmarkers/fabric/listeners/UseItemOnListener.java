package nl.gjorgdy.pl3xmarkers.fabric.listeners;

import net.fabricmc.fabric.api.event.player.BlockEvents;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.portal.PortalShape;
import net.minecraft.world.phys.BlockHitResult;
import nl.gjorgdy.pl3xmarkers.core.Pl3xMarkersCore;
import nl.gjorgdy.pl3xmarkers.core.layers.EndGatewayMarkerLayer;
import nl.gjorgdy.pl3xmarkers.core.layers.EndPortalMarkerLayer;
import nl.gjorgdy.pl3xmarkers.core.layers.NetherPortalMarkerLayer;
import nl.gjorgdy.pl3xmarkers.core.layers.primitive.SimpleMarkerLayer;
import nl.gjorgdy.pl3xmarkers.core.registries.Layers;
import nl.gjorgdy.pl3xmarkers.fabric.helpers.FeedbackHelper;
import nl.gjorgdy.pl3xmarkers.fabric.helpers.PortalHelper;
import nl.gjorgdy.pl3xmarkers.fabric.interfaces.NetherPortalInterface;
import org.jspecify.annotations.NonNull;
import org.jspecify.annotations.Nullable;

import static nl.gjorgdy.pl3xmarkers.core.objects.InteractionResult.State;

public class UseItemOnListener implements BlockEvents.UseItemOnCallback {

	@Override
	public @Nullable InteractionResult useItemOn(@NonNull ItemStack itemStack, @NonNull BlockState blockState, @NonNull Level level, @NonNull BlockPos blockPos, @NonNull Player player, @NonNull InteractionHand interactionHand, @NonNull BlockHitResult blockHitResult) {
		var netherPortalCenter = getNetherPortalCenter(level, blockPos, blockState);
		if (netherPortalCenter != null) {
			if (interactionHand == InteractionHand.OFF_HAND) {
				return null;
			}
			if (itemStack.is(Items.NAME_TAG)) {
				return useNameTagOnNetherPortal(level, (ServerPlayer) player, interactionHand, itemStack,
				                                netherPortalCenter
				);
			} else {
				return useNetherPortal(level, (ServerPlayer) player, interactionHand, netherPortalCenter);
			}
		}
		var endPortalCenter = getEndPortalCenter(level, blockPos, blockState);
		if (endPortalCenter != null) {
			if (interactionHand == InteractionHand.OFF_HAND) {
				return null;
			}
			if (itemStack.is(Items.NAME_TAG)) {
				return useNameTagOnEndPortal(level, (ServerPlayer) player, interactionHand, itemStack,
				                             endPortalCenter
				);
			} else {
				return useEndPortal(level, (ServerPlayer) player, interactionHand, endPortalCenter);
			}
		}
		if (level.getBlockState(blockPos).is(Blocks.END_GATEWAY)) {
			if (interactionHand == InteractionHand.OFF_HAND) {
				return null;
			}
			if (itemStack.is(Items.NAME_TAG)) {
				return useNameTagOnEndGateway(level, (ServerPlayer) player, interactionHand, itemStack,
				                              blockPos
				);
			} else {
				return useEndGateway(level, (ServerPlayer) player, interactionHand, blockPos);
			}
		}
		return null;
	}

	private InteractionResult useNameTagOnNetherPortal(Level level, ServerPlayer player, InteractionHand interactionHand, ItemStack nameTagItem, BlockPos portalCenter) {
		var layer = Pl3xMarkersCore.api()
				.getWorld(level.dimension().identifier().toString())
				.getLayer(NetherPortalMarkerLayer.class, Layers.Keys.NETHER_PORTALS);
		return useNameTagOnPortal(layer, player, interactionHand, nameTagItem, portalCenter);
	}

	private InteractionResult useNameTagOnEndPortal(Level level, ServerPlayer player, InteractionHand interactionHand, ItemStack nameTagItem, BlockPos portalCenter) {
		var layer = Pl3xMarkersCore.api()
				.getWorld(level.dimension().identifier().toString())
				.getLayer(EndPortalMarkerLayer.class, Layers.Keys.END_PORTALS);
		return useNameTagOnPortal(layer, player, interactionHand, nameTagItem, portalCenter);
	}

	private InteractionResult useNameTagOnEndGateway(Level level, ServerPlayer player, InteractionHand interactionHand, ItemStack nameTagItem, BlockPos portalCenter) {
		var layer = Pl3xMarkersCore.api()
				.getWorld(level.dimension().identifier().toString())
				.getLayer(EndGatewayMarkerLayer.class, Layers.Keys.END_GATEWAYS);
		return useNameTagOnPortal(layer, player, interactionHand, nameTagItem, portalCenter);
	}

	private InteractionResult useNameTagOnPortal(SimpleMarkerLayer layer, ServerPlayer player, InteractionHand interactionHand, ItemStack nameTagItem, BlockPos portalCenter) {
		var customName = nameTagItem.getCustomName();
		if (customName == null) {
			return InteractionResult.PASS;
		}
		String name = customName.getString();
		if (layer == null) {
			return InteractionResult.PASS;
		}
		var result = layer.setName(
				portalCenter.getX(), portalCenter.getY(), portalCenter.getZ(), name
		);
		FeedbackHelper.sendFeedback(result, player);
		if (result.state().equals(State.ADDED)) {
			nameTagItem.consume(1, player);
			player.swing(interactionHand, true);
		}
		if (result.state().equals(State.FEEDBACK)) {
			player.swing(interactionHand, true);
		}
		return InteractionResult.SUCCESS;
	}

	private InteractionResult useNetherPortal(Level level, ServerPlayer player, InteractionHand interactionHand, BlockPos portalCenter) {
		var layer = Pl3xMarkersCore.api()
				.getWorld(level.dimension().identifier().toString())
				.getLayer(NetherPortalMarkerLayer.class, Layers.Keys.NETHER_PORTALS);
		return usePortal(layer, player, interactionHand, portalCenter);
	}

	private InteractionResult useEndPortal(Level level, ServerPlayer player, InteractionHand interactionHand, BlockPos portalCenter) {
		var layer = Pl3xMarkersCore.api()
				.getWorld(level.dimension().identifier().toString())
				.getLayer(EndPortalMarkerLayer.class, Layers.Keys.END_PORTALS);
		return usePortal(layer, player, interactionHand, portalCenter);
	}

	private InteractionResult useEndGateway(Level level, ServerPlayer player, InteractionHand interactionHand, BlockPos portalCenter) {
		var layer = Pl3xMarkersCore.api()
				.getWorld(level.dimension().identifier().toString())
				.getLayer(EndGatewayMarkerLayer.class, Layers.Keys.END_GATEWAYS);
		return usePortal(layer, player, interactionHand, portalCenter);
	}

	private InteractionResult usePortal(SimpleMarkerLayer layer, ServerPlayer player, InteractionHand interactionHand, BlockPos portalCenter) {
		if (layer == null) {
			return InteractionResult.PASS;
		}
		var result = layer.interact(portalCenter.getX(), portalCenter.getY(), portalCenter.getZ());
		FeedbackHelper.sendFeedback(result, player);
		if (result.state().equals(State.FEEDBACK)) {
			player.swing(interactionHand, true);
		}
		return InteractionResult.SUCCESS;
	}

	@Nullable
	private BlockPos getNetherPortalCenter(LevelAccessor level, BlockPos pos, BlockState blockState) {
		if (!blockState.is(Blocks.NETHER_PORTAL)) {
			return null;
		}
		Direction.Axis axis = blockState.getValue(BlockStateProperties.HORIZONTAL_AXIS);
		var portal = (NetherPortalInterface) PortalShape.findAnyShape(level, pos, axis);
		return portal.pl3xMarkers$getPortalCenter();
	}

	private BlockPos getEndPortalCenter(LevelAccessor level, BlockPos pos, BlockState blockState) {
		if (!blockState.is(Blocks.END_PORTAL)) {
			return null;
		}
		return PortalHelper.getEndPortalCenter((Level) level, pos);
	}

}
