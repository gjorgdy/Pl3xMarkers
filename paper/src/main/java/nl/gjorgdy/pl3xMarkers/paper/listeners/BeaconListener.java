package nl.gjorgdy.pl3xMarkers.paper.listeners;

import io.papermc.paper.event.block.BeaconActivatedEvent;
import io.papermc.paper.event.block.BeaconDeactivatedEvent;
import nl.gjorgdy.pl3xMarkers.paper.helpers.FeedbackHelper;
import nl.gjorgdy.pl3xmarkers.core.Pl3xMarkersCore;
import nl.gjorgdy.pl3xmarkers.core.layers.BeaconMarkerLayer;
import nl.gjorgdy.pl3xmarkers.core.registries.Layers;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

public class BeaconListener implements Listener {

    @EventHandler
    public void onBeaconActivate(BeaconActivatedEvent event) {
        var block = event.getBeacon();
        var world = block.getWorld();
        var blockPos = block.getLocation().getBlock();
        var result = Pl3xMarkersCore.api()
                .getWorld(world.getName())
                .getLayer(BeaconMarkerLayer.class, Layers.Keys.BEACONS)
                .add(blockPos.getX(), blockPos.getY(), blockPos.getZ());
		FeedbackHelper.sendFeedback(result, block.getLocation());
    }

    @EventHandler
    public void onBeaconDeactivate(BeaconDeactivatedEvent event) {
        if (!event.getBlock().getLocation().isChunkLoaded()) {
            return;
        }
        var block = event.getBlock();
        var world = block.getWorld();
        var blockPos = block.getLocation().getBlock();
        var result = Pl3xMarkersCore.api()
                .getWorld(world.getName())
                .getLayer(BeaconMarkerLayer.class, Layers.Keys.BEACONS)
                .remove(blockPos.getX(), blockPos.getY(), blockPos.getZ());
		FeedbackHelper.sendFeedback(result, block.getLocation());
    }

}
