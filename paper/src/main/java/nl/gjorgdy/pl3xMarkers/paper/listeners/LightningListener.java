package nl.gjorgdy.pl3xMarkers.paper.listeners;

import nl.gjorgdy.pl3xmarkers.core.Pl3xMarkersCore;
import nl.gjorgdy.pl3xmarkers.core.layers.LightningMarkerLayer;
import nl.gjorgdy.pl3xmarkers.core.registries.Layers;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.weather.LightningStrikeEvent;

public class LightningListener implements Listener {

	@EventHandler
	public void onLightningStrike(LightningStrikeEvent event) {
		Pl3xMarkersCore.api()
				.getWorld(event.getLightning().getWorld().getName())
				.getLayer(LightningMarkerLayer.class, Layers.Keys.LIGHTNING)
				.show(
						(int) event.getLightning().getX(),
						(int) event.getLightning().getY(),
						(int) event.getLightning().getZ()
				);
	}

}
