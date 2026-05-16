package nl.gjorgdy.pl3xmarkers.fabric.listeners;

import net.fabricmc.fabric.api.event.player.BlockEvents;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.component.DataComponents;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.tags.ItemTags;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.DyeColor;
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
import nl.gjorgdy.pl3xmarkers.core.layers.NetherPortalMarkerLayer;
import nl.gjorgdy.pl3xmarkers.core.registries.Layers;
import nl.gjorgdy.pl3xmarkers.fabric.helpers.FeedbackHelper;
import nl.gjorgdy.pl3xmarkers.fabric.interfaces.NetherPortalInterface;
import org.jspecify.annotations.NonNull;
import org.jspecify.annotations.Nullable;

import static nl.gjorgdy.pl3xmarkers.core.objects.InteractionResult.State;

public class UseItemOnListener implements BlockEvents.UseItemOnCallback {

	@Override
	public @Nullable InteractionResult useItemOn(@NonNull ItemStack itemStack, @NonNull BlockState blockState, @NonNull Level level, @NonNull BlockPos blockPos, @NonNull Player player, @NonNull InteractionHand interactionHand, @NonNull BlockHitResult blockHitResult) {
		var portalCenter = getPortalCenter(level, blockPos, blockState);
		if (portalCenter == null) {
			return null;
		}
		if (itemStack.is(ItemTags.DYES)) {
			return useDyeItemOnPortal(level, (ServerPlayer) player, interactionHand, itemStack, portalCenter);
		}
		if (itemStack.is(Items.NAME_TAG)) {
			return useNameTagOnPortal(level, (ServerPlayer) player, interactionHand, itemStack, portalCenter);
		}
		return null;
	}

	private InteractionResult useDyeItemOnPortal(Level level, ServerPlayer player, InteractionHand interactionHand, ItemStack dyeItem, BlockPos portalCenter) {
		DyeColor color = dyeItem.get(DataComponents.DYE);
		if (color == null) {
			return InteractionResult.PASS;
		}
		var result = Pl3xMarkersCore.api()
				.getWorld(level.dimension().identifier().toString())
				.getLayer(NetherPortalMarkerLayer.class, Layers.Keys.NETHER_PORTALS)
				.setColor(portalCenter.getX(), portalCenter.getY(), portalCenter.getZ(),
				          color.getTextureDiffuseColor()
				);
		FeedbackHelper.sendFeedback(result, player);
		if (result.state().equals(State.ADDED)) {
			dyeItem.consume(1, player);
			player.swing(interactionHand, true);
		}
		return InteractionResult.SUCCESS;
	}

	private InteractionResult useNameTagOnPortal(Level level, ServerPlayer player, InteractionHand interactionHand, ItemStack nameTagItem, BlockPos portalCenter) {
		var customName = nameTagItem.getCustomName();
		if (customName == null) {
			return InteractionResult.PASS;
		}
		String name = customName.getString();
		var result = Pl3xMarkersCore.api()
				.getWorld(level.dimension().identifier().toString())
				.getLayer(NetherPortalMarkerLayer.class, Layers.Keys.NETHER_PORTALS)
				.setName(portalCenter.getX(), portalCenter.getY(), portalCenter.getZ(),
				         name
				);
		FeedbackHelper.sendFeedback(result, player);
		if (result.state().equals(State.ADDED)) {
			nameTagItem.consume(1, player);
			player.swing(interactionHand, true);
		}
		return InteractionResult.SUCCESS;
	}

	@Nullable
	private BlockPos getPortalCenter(LevelAccessor level, BlockPos pos, BlockState blockState) {
		if (!blockState.is(Blocks.NETHER_PORTAL)) {
			return null;
		}
		Direction.Axis axis = blockState.getValue(BlockStateProperties.HORIZONTAL_AXIS);
		var portal = (NetherPortalInterface) PortalShape.findAnyShape(level, pos, axis);
		return portal.pl3xMarkers$getPortalCenter();
	}

}
