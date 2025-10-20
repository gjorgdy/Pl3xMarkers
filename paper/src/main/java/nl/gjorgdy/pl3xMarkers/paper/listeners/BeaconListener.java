package nl.gjorgdy.pl3xMarkers.paper.listeners;

import io.papermc.paper.event.block.BeaconActivatedEvent;
import io.papermc.paper.event.block.BeaconDeactivatedEvent;
import nl.gjorgdy.pl3xmarkers.core.Pl3xMarkersCore;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

public class BeaconListener implements Listener {

    @EventHandler
    public void onBeaconActivate(BeaconActivatedEvent event) {
        var block = event.getBeacon();
        var world = block.getWorld();
        var blockPos = block.getLocation().getBlock();
        Pl3xMarkersCore.api().addBeaconIconMarker(world.getName(), blockPos.getX(), blockPos.getZ());
    }

    @EventHandler
    public void onBeaconDeactivate(BeaconDeactivatedEvent event) {
        if (!event.getBlock().getLocation().isChunkLoaded()) return;
        var block = event.getBlock();
        var world = block.getWorld();
        var blockPos = block.getLocation().getBlock();
        Pl3xMarkersCore.api().removeBeaconIconMarker(world.getName(), blockPos.getX(), blockPos.getZ());
    }

}
