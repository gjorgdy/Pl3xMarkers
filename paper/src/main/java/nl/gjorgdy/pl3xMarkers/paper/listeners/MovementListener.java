package nl.gjorgdy.pl3xMarkers.paper.listeners;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.TextColor;
import nl.gjorgdy.pl3xmarkers.core.MarkersConfig;
import nl.gjorgdy.pl3xmarkers.core.Pl3xMarkersCore;
import nl.gjorgdy.pl3xmarkers.core.objects.Boundary;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerMoveEvent;

import java.util.HashMap;
import java.util.UUID;

public class MovementListener implements Listener {

	private final HashMap<UUID, Boundary> playerBoundaries;

	public MovementListener(HashMap<UUID, Boundary> playerBoundaries) {
		this.playerBoundaries = playerBoundaries;
	}

	@EventHandler
	public void onPlayerMove(PlayerMoveEvent event) {
		if (!MarkersConfig.FEEDBACK_AREA_ENTER_ENABLED) return;

		var player = event.getPlayer();
	   	var boundary = playerBoundaries.get(player.getUniqueId());

		if (boundary != null && boundary.contains(
				event.getTo().getBlockX(),
				event.getTo().getBlockZ()
		)) return;

		Pl3xMarkersCore.api().getAreaBoundary(
				event.getTo().getWorld().getName(),
				event.getTo().getBlockX(),
				event.getTo().getBlockZ()
		).ifPresentOrElse(b ->
		{
			player.sendActionBar(
				Component.text("[+] " + b.areaMarker().getName())
					.color(TextColor.color(b.areaMarker().getColor()))
			);
			playerBoundaries.put(player.getUniqueId(), b);
		}, () -> {
			if (boundary != null) {
				player.sendActionBar(
					Component.text("[-] " + boundary.areaMarker().getName())
						.color(TextColor.color(boundary.areaMarker().getColor()))
				);
			}
			playerBoundaries.remove(player.getUniqueId());
		});
	}

}
