package nl.gjorgdy.pl3xMarkers.paper.listeners;

import com.destroystokyo.paper.event.player.PlayerTeleportEndGatewayEvent;
import nl.gjorgdy.pl3xMarkers.paper.helpers.FeedbackHelper;
import nl.gjorgdy.pl3xmarkers.core.Pl3xMarkersCore;
import nl.gjorgdy.pl3xmarkers.core.layers.EndGatewayMarkerLayer;
import nl.gjorgdy.pl3xmarkers.core.registries.Layers;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

public class EndGatewayListener implements Listener {

    @EventHandler
    public void onEnterGateway(PlayerTeleportEndGatewayEvent event) {
        var loc = event.getGateway().getLocation();
	    var markerLayer = Pl3xMarkersCore.api()
			    .getWorld(loc.getWorld().getName())
			    .getLayer(EndGatewayMarkerLayer.class, Layers.Keys.END_GATEWAYS);
	    if (markerLayer == null) {
		    return;
	    }
	    var result = markerLayer
			    .add(loc.getBlockX(), loc.getBlockY(), loc.getBlockZ());
		FeedbackHelper.sendFeedback(result, loc);
        var exit = event.getGateway().getExitLocation();
	    if (exit == null) {
		    return;
	    }
	    markerLayer = Pl3xMarkersCore.api()
			    .getWorld(exit.getWorld().getName())
			    .getLayer(EndGatewayMarkerLayer.class, Layers.Keys.END_GATEWAYS);
	    if (markerLayer == null) {
		    return;
	    }
	    result = markerLayer.add(exit.getBlockX(), exit.getBlockY(), exit.getBlockZ());
		FeedbackHelper.sendFeedback(result, loc);
    }

}