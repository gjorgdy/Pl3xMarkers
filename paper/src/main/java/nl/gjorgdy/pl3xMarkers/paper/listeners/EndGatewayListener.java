package nl.gjorgdy.pl3xMarkers.paper.listeners;

import com.destroystokyo.paper.event.player.PlayerTeleportEndGatewayEvent;
import nl.gjorgdy.pl3xMarkers.paper.helpers.FeedbackHelper;
import nl.gjorgdy.pl3xmarkers.core.Pl3xMarkersCore;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

public class EndGatewayListener implements Listener {

    @EventHandler
    public void onEnterGateway(PlayerTeleportEndGatewayEvent event) {
        var loc = event.getGateway().getLocation();
        var result = Pl3xMarkersCore.api().addEndGatewayIconMarker(loc.getWorld().getName(), loc.getBlockX(), loc.getBlockZ());
		FeedbackHelper.sendFeedback(result, loc);
        var exit = event.getGateway().getExitLocation();
        if (exit == null) return;
        result = Pl3xMarkersCore.api().addEndGatewayIconMarker(exit.getWorld().getName(), exit.getBlockX(), exit.getBlockZ());
		FeedbackHelper.sendFeedback(result, loc);
    }

}