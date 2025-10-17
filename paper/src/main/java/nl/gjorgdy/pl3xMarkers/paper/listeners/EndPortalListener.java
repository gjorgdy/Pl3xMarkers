package nl.gjorgdy.pl3xMarkers.paper.listeners;

import nl.gjorgdy.pl3xMarkers.paper.helpers.PortalHelper;
import nl.gjorgdy.pl3xmarkers.core.Pl3xMarkersCore;
import org.bukkit.block.BlockType;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerPortalEvent;

public class EndPortalListener implements Listener {

    @EventHandler
    public void onPortalTeleport(PlayerPortalEvent event) {
        if (event.getFrom().getBlock().getType().asBlockType() == BlockType.END_PORTAL) {
            var center = PortalHelper.getEndPortalCenter(event.getFrom());
            Pl3xMarkersCore.api().addEndPortalIconMarker(center.getWorld().getName(), center.getBlockX(), center.getBlockZ());
        }
    }

}
