package nl.gjorgdy.pl3xmarkers.core.layers;

import net.pl3x.map.core.Pl3xMap;
import net.pl3x.map.core.configuration.PlayersLayerConfig;
import net.pl3x.map.core.markers.Point;
import net.pl3x.map.core.markers.marker.Icon;
import net.pl3x.map.core.markers.marker.Marker;
import net.pl3x.map.core.markers.option.Options;
import net.pl3x.map.core.markers.option.Tooltip;
import net.pl3x.map.core.player.Player;
import net.pl3x.map.core.world.World;
import nl.gjorgdy.pl3xmarkers.core.helpers.WorldHelpers;
import nl.gjorgdy.pl3xmarkers.core.layers.primitive.MarkerLayer;
import nl.gjorgdy.pl3xmarkers.core.markers.MarkerBuilder;
import nl.gjorgdy.pl3xmarkers.core.registries.Layers;
import org.jspecify.annotations.NonNull;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

public class CrossDimensionPlayerMarkerLayer extends MarkerLayer<Player> {

	public CrossDimensionPlayerMarkerLayer(World world) {
		super(
				WorldHelpers.isOverworld(world) ? Layers.Keys.NETHER_PLAYERS : Layers.Keys.OVERWORLD_PLAYERS,
				WorldHelpers.isOverworld(world) ? Layers.Labels.NETHER_PLAYERS : Layers.Labels.OVERWORLD_PLAYERS,
				world, 21
		);
		setUpdateInterval(PlayersLayerConfig.UPDATE_INTERVAL);
		setLiveUpdate(PlayersLayerConfig.LIVE_UPDATE);
		setShowControls(PlayersLayerConfig.SHOW_CONTROLS);
		setDefaultHidden(PlayersLayerConfig.DEFAULT_HIDDEN);
		setPriority(PlayersLayerConfig.PRIORITY);
		setZIndex(PlayersLayerConfig.Z_INDEX);
		setPane(PlayersLayerConfig.PANE);
		setCss(PlayersLayerConfig.CSS);

	}

	@Override
	public @NonNull Collection<Marker<?>> getMarkers() {
		Set<Marker<?>> icons = new HashSet<>();

		var otherKey = otherDimensionWorldKey(getWorld().getKey());
		var otherDimensionWorld = Pl3xMap.api().getWorldRegistry().get(otherKey);
		if (otherDimensionWorld == null) {
			return icons;
		}
		otherDimensionWorld.getPlayers().forEach((player) -> {
			if (!player.isHidden()) {
				if (!player.isNPC()) {
					if (!PlayersLayerConfig.HIDE_INVISIBLE || !player.isInvisible()) {
						if (!PlayersLayerConfig.HIDE_SPECTATORS || !player.isSpectator()) {
							icons.add(createIcon(player));
						}
					}
				}
			}
		});
		return icons;
	}

	private Point translateToOtherDimension(Point point) {
		return WorldHelpers.isOverworld(getWorld())
				? new Point(point.x() * 8, point.z() * 8)
				: new Point(point.x() / 8, point.z() / 8);
	}

	private String otherDimensionWorldKey(String worldKey) {
		return switch (worldKey) {
			case "minecraft:the_nether" -> "minecraft:overworld"; // fabric
			case "minecraft:overworld" -> "minecraft:the_nether"; // fabric
			case "world" -> "world_nether"; // paper
			case "world_nether" -> "world"; // paper
			default -> dynamicDestinationKey(worldKey);
		};
	}

	private String dynamicDestinationKey(String worldKey) {
		String destination;
		if (WorldHelpers.isOverworld(worldKey)) {
			destination = worldKey + "_nether";
		} else {
			destination = worldKey.replace("_nether", "");
		}
		return destination.replace(":", "-");
	}

	private Icon createIcon(Player player) {
		Icon icon = Marker.icon(player.getUUID().toString(), translateToOtherDimension(player.getPosition()),
		                        PlayersLayerConfig.ICON, 16.0F
				)
				.setOptions(Options.builder().build())
				.setRotationAngle((double) player.getYaw())
				.setRotationOrigin("center").setPane("players");
		String tooltip = PlayersLayerConfig.TOOLTIP;
		tooltip = "<div style=\"opacity: 0.25;\">" + tooltip + "</div>";
		return !tooltip.isBlank() ? icon.setOptions(
				Options.builder().tooltipContent(
						tooltip.replace("<uuid>", player.getUUID().toString()).replace("<name>",
						                                                               player.getName()
						).replace("<decoratedName>", player.getDecoratedName()).replace("<health>", Integer.toString(
								player.getHealth())
						).replace("<armor>", Integer.toString(player.getArmorPoints()))).tooltipPane(
						PlayersLayerConfig.PANE).tooltipDirection(
						Tooltip.Direction.RIGHT).tooltipPermanent(true).tooltipOffset(Point.of(5, 0)).tooltipOpacity(
						(double) 1.0F).build()
		) : icon;
	}

	@Override
	public void load() {
		// ignore
	}

	@Override
	public MarkerBuilder<?> createBuilder(Player object) {
		// ignore
		return null;
	}
}
