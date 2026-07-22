package nl.gjorgdy.pl3xmarkers.fabric.listeners;

import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.tags.BlockTags;
import net.minecraft.tags.TagKey;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.entity.BannerBlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import nl.gjorgdy.pl3xmarkers.core.Pl3xMarkersCore;
import nl.gjorgdy.pl3xmarkers.core.layers.BeaconMarkerLayer;
import nl.gjorgdy.pl3xmarkers.core.layers.NetherPortalMarkerLayer;
import nl.gjorgdy.pl3xmarkers.core.layers.SignsMarkerLayer;
import nl.gjorgdy.pl3xmarkers.core.layers.primitive.AreaMarkerLayer;
import nl.gjorgdy.pl3xmarkers.core.objects.InteractionResult;
import nl.gjorgdy.pl3xmarkers.core.registries.Layers;
import org.intellij.lang.annotations.Language;
import org.jetbrains.annotations.Nullable;
import org.jspecify.annotations.NonNull;

public abstract class BlockListener {

	@Nullable
	public static InteractionResult onPlace(@NonNull Level level, @NonNull BlockPos pos) {
		// area markers
		var blockEntity = level.getBlockEntity(pos);
		if (blockEntity instanceof BannerBlockEntity banner && level.getBlockState(pos.below()).is(Blocks.LODESTONE)) {
			@Language("HTML") var name = banner.getName().tryCollapseToString();
			if (name == null) {
				return null;
			}
			var markerLayer = Pl3xMarkersCore.api()
					.getWorld(level.dimension().identifier().toString())
					.getLayer(AreaMarkerLayer.class, Layers.Keys.AREAS);
			if (markerLayer == null) {
				return null;
			}
			return markerLayer.addPoint(
					name, banner.getBaseColor().getTextureDiffuseColor(),
					pos.getX(), pos.getY(), pos.getZ()
			);
		}
		return null;
	}

	@Nullable
	public static InteractionResult onDestroy(@NonNull Level level, @NonNull BlockPos pos, @NonNull BlockState state) {
		// beacon markers
		if (state.is(Blocks.BEACON)) {
			var markerLayer = Pl3xMarkersCore.api()
					.getWorld(level.dimension().identifier().toString())
					.getLayer(BeaconMarkerLayer.class, Layers.Keys.BEACONS);
			if (markerLayer == null) {
				return null;
			}
			return markerLayer.remove(pos.getX(), pos.getY(), pos.getZ());
		}
		// area markers
		if (level instanceof ServerLevel serverWorld) {
			if (state.is(Blocks.LODESTONE)) {
				pos = pos.above();
			}
			var blockEntity = serverWorld.getBlockEntity(pos);
			if (blockEntity instanceof BannerBlockEntity banner) {
				var markerLayer = Pl3xMarkersCore.api()
						.getWorld(level.dimension().identifier().toString())
						.getLayer(AreaMarkerLayer.class, Layers.Keys.AREAS);
				if (markerLayer == null) {
					return null;
				}
				@Language("HTML") var name = banner.getName().tryCollapseToString();
				if (name == null) {
					return null;
				}
				return markerLayer.removePoint(
						name, banner.getBaseColor().getTextureDiffuseColor(),
						pos.getX(), pos.getY(), pos.getZ()
				);
			}
		}
		return null;
	}

	@Nullable
	public static InteractionResult onChange(@NonNull Level level, @NonNull BlockPos pos, @NonNull BlockState state, @NonNull BlockState newState) {
		// beacon
		if (broke(Blocks.BEACON, state, newState)) {
			var markerLayer = Pl3xMarkersCore.api()
					.getWorld(level.dimension().identifier().toString())
					.getLayer(BeaconMarkerLayer.class, Layers.Keys.BEACONS);
			if (markerLayer == null) {
				return null;
			}
			return markerLayer.remove(pos.getX(), pos.getY(), pos.getZ());
		}
		// nether portal
		if (broke(Blocks.NETHER_PORTAL, state, newState)) {
			var markerLayer = Pl3xMarkersCore.api()
					.getWorld(level.dimension().identifier().toString())
					.getLayer(NetherPortalMarkerLayer.class, Layers.Keys.NETHER_PORTALS);
			if (markerLayer == null) {
				return null;
			}
			return markerLayer.remove(pos.getX(), pos.getY(), pos.getZ());
		}
		// signs
		if (broke(BlockTags.SIGNS, state, newState)) {
			var markerLayer = Pl3xMarkersCore.api()
					.getWorld(level.dimension().identifier().toString())
					.getLayer(SignsMarkerLayer.class, Layers.Keys.SIGNS);
			if (markerLayer == null) {
				return null;
			}
			return markerLayer.remove(pos.getX(), pos.getY(), pos.getZ());
		}
		// banners
		if (broke(BlockTags.BANNERS, state, newState)) {
			var markerLayer = Pl3xMarkersCore.api()
					.getWorld(level.dimension().identifier().toString())
					.getLayer(AreaMarkerLayer.class, Layers.Keys.AREAS);
			if (markerLayer == null) {
				return null;
			}
			var blockEntity = level.getBlockEntity(pos);
			if (!(blockEntity instanceof BannerBlockEntity banner)) {
				return null;
			}
			@Language("HTML") var name = banner.getName().tryCollapseToString();
			if (name == null) {
				return null;
			}
			return markerLayer.removePoint(
					name, banner.getBaseColor().getTextureDiffuseColor(),
					pos.getX(), pos.getY(), pos.getZ()
			);
		}
		return null;
	}

	private static boolean broke(Block block, BlockState state, BlockState newState) {
		return state.is(block) && !newState.is(block);
	}

	private static boolean broke(TagKey<Block> block, BlockState state, BlockState newState) {
		return state.is(block) && !newState.is(block);
	}

}
