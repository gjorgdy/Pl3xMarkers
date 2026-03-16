package nl.gjorgdy.pl3xMarkers.paper.listeners;

import nl.gjorgdy.pl3xmarkers.core.Pl3xMarkersCore;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.weather.LightningStrikeEvent;

public class LightningListener implements Listener {

	@EventHandler
	public void onLightningStrike(LightningStrikeEvent event) {
		Pl3xMarkersCore.api().showLightningIconMarker(
				event.getLightning().getWorld().getName(),
				(int) event.getLightning().getX(),
				(int) event.getLightning().getZ()
		);
	}

}
