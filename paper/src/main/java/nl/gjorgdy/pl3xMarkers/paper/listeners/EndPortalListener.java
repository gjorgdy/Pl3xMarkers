package nl.gjorgdy.pl3xMarkers.paper.listeners;

import nl.gjorgdy.pl3xMarkers.paper.helpers.FeedbackHelper;
import nl.gjorgdy.pl3xMarkers.paper.helpers.PortalHelper;
import nl.gjorgdy.pl3xmarkers.core.Pl3xMarkersCore;
import nl.gjorgdy.pl3xmarkers.core.layers.EndPortalMarkerLayer;
import nl.gjorgdy.pl3xmarkers.core.registries.Layers;
import org.bukkit.block.BlockType;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerPortalEvent;

public class EndPortalListener implements Listener {

    @EventHandler
    public void onPortalTeleport(PlayerPortalEvent event) {
        if (event.getFrom().getBlock().getType().asBlockType() == BlockType.END_PORTAL) {
            var center = PortalHelper.getEndPortalCenter(event.getFrom());
            var result = Pl3xMarkersCore.api()
                    .getWorld(center.getWorld().getName())
                    .getLayer(EndPortalMarkerLayer.class, Layers.Keys.END_PORTALS)
                    .add(center.getBlockX(), center.getBlockY(), center.getBlockZ());
			FeedbackHelper.sendFeedback(result, event.getPlayer());
        }
    }

}
