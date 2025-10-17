package nl.gjorgdy.pl3xMarkers.paper.listeners;

import com.destroystokyo.paper.event.player.PlayerTeleportEndGatewayEvent;
import nl.gjorgdy.pl3xmarkers.core.Pl3xMarkersCore;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

public class EndGatewayListener implements Listener {

    @EventHandler
    public void onEnterGateway(PlayerTeleportEndGatewayEvent event) {
        var loc = event.getGateway().getLocation();
        Pl3xMarkersCore.api().addEndGatewayIconMarker(loc.getWorld().getName(), loc.getBlockX(), loc.getBlockZ());
        var exit = event.getGateway().getExitLocation();
        if (exit == null) return;
        Pl3xMarkersCore.api().addEndGatewayIconMarker(exit.getWorld().getName(), exit.getBlockX(), exit.getBlockZ());
    }

}
