package nl.gjorgdy.pl3xMarkers.paper.listeners;

import nl.gjorgdy.pl3xMarkers.paper.helpers.FeedbackHelper;
import nl.gjorgdy.pl3xmarkers.core.MarkersConfig;
import nl.gjorgdy.pl3xmarkers.core.Pl3xMarkersCore;
import nl.gjorgdy.pl3xmarkers.core.interfaces.IBoundary;
import nl.gjorgdy.pl3xmarkers.core.layers.primitive.AreaMarkerLayer;
import nl.gjorgdy.pl3xmarkers.core.registries.Layers;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerMoveEvent;

import java.util.HashMap;
import java.util.UUID;

public class MovementListener implements Listener {

	private final HashMap<UUID, IBoundary> playerBoundaries;
	private final HashMap<UUID, Integer> boundaryPresenceLengths = new HashMap<>();

	public MovementListener(HashMap<UUID, IBoundary> playerBoundaries) {
		this.playerBoundaries = playerBoundaries;
	}

	@EventHandler
	public void onPlayerMove(PlayerMoveEvent event) {
		if (!MarkersConfig.FEEDBACK_AREA_ENTER_ENABLED || !MarkersConfig.AREA_MARKERS_ENABLED) {
			return;
		}

		var player = event.getPlayer();
	   	var boundary = playerBoundaries.get(player.getUniqueId());

		if (boundary != null && boundary.contains(
				event.getTo().getBlockX(),
				event.getTo().getBlockZ()
		)) {
			var boundaryPresenceLength = boundaryPresenceLengths.getOrDefault(player.getUniqueId(), 0);
			boundaryPresenceLengths.put(player.getUniqueId(), boundaryPresenceLength + 1);
			// area presence
			if (boundaryPresenceLength > 40 && MarkersConfig.FEEDBACK_AREA_PRESENCE) {
				FeedbackHelper.sendOverlayMessage(
						player,
						boundary.areaMarker().getName(),
						boundary.areaMarker().getColor()
				);
			}
			return;
		}
		boundaryPresenceLengths.put(player.getUniqueId(), 0);

		var markerLayer = Pl3xMarkersCore.api()
				.getWorld(event.getTo().getWorld().getName())
				.getLayer(AreaMarkerLayer.class, Layers.Keys.AREAS);
		if (markerLayer == null) {
			return;
		}
		markerLayer.getContaining(
						event.getTo().getBlockX(),
						event.getTo().getBlockZ()
				)
				.ifPresentOrElse(b ->
		{
			FeedbackHelper.sendOverlayMessage(
					player,
					MarkersConfig.MESSAGE_AREA_ENTER.replace("{name}", b.areaMarker().getName()),
					b.areaMarker().getColor()
			);
			playerBoundaries.put(player.getUniqueId(), b);
		}, () -> {
			if (boundary != null) {
				FeedbackHelper.sendOverlayMessage(
						player,
						MarkersConfig.MESSAGE_AREA_LEAVE.replace("{name}", boundary.areaMarker().getName()),
						boundary.areaMarker().getColor()
				);
			}
			playerBoundaries.remove(player.getUniqueId());
		});
	}

}
