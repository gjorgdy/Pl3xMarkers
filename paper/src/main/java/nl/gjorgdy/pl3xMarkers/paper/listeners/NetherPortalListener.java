package nl.gjorgdy.pl3xMarkers.paper.listeners;

import nl.gjorgdy.pl3xMarkers.paper.helpers.FeedbackHelper;
import nl.gjorgdy.pl3xMarkers.paper.helpers.PortalHelper;
import nl.gjorgdy.pl3xmarkers.core.Pl3xMarkersCore;
import nl.gjorgdy.pl3xmarkers.core.layers.NetherPortalMarkerLayer;
import nl.gjorgdy.pl3xmarkers.core.registries.Layers;
import org.bukkit.Location;
import org.bukkit.block.BlockType;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.player.PlayerPortalEvent;
import org.bukkit.event.player.PlayerTeleportEvent;

public class NetherPortalListener implements Listener {

    @EventHandler
    public void onPortalTeleport(PlayerPortalEvent event) {
        System.out.println("PlayerPortalEvent: " + event.getCause());
        if (event.getCause() == PlayerTeleportEvent.TeleportCause.NETHER_PORTAL) {
            onNetherPortalTeleport(event.getFrom());
        }
    }

    @EventHandler
    public void onBlockBreak(BlockBreakEvent event) {
        var blockType = event.getBlock().getType().asBlockType();
        var location = event.getBlock().getLocation();
        if (blockType == BlockType.OBSIDIAN) {
            var nearby = location.clone();
            for (int x = -1; x <= 1; x++) {
                for (int y = -1; y <= 1; y++) {
                    for (int z = -1; z <= 1; z++) {
                        nearby.set(location.getBlockX() + x, location.getBlockY() + y, location.getBlockZ() + z);
                        if (nearby.getBlock().getType().asBlockType() == BlockType.NETHER_PORTAL) {
                            var center = PortalHelper.getNetherPortalCenter(nearby);
                            onNetherPortalBreak(center);
                            return;
                        }
                    }
                }
            }
        }
        if (blockType == BlockType.NETHER_PORTAL) {
            var center = PortalHelper.getNetherPortalCenter(location);
            onNetherPortalBreak(center);
        }
    }

    private void onNetherPortalBreak(Location location) {
        var center = PortalHelper.getNetherPortalCenter(location);
        var result = Pl3xMarkersCore.api()
                .getWorld(location.getWorld().getName())
                .getLayer(NetherPortalMarkerLayer.class, Layers.Keys.NETHER_PORTALS)
                .remove(center.getBlockX(), center.getBlockY(), center.getBlockZ());
		FeedbackHelper.sendFeedback(result, center);
    }

    private void onNetherPortalTeleport(Location location) {
        var center = PortalHelper.getNetherPortalCenter(location);
        var result = Pl3xMarkersCore.api()
                .getWorld(location.getWorld().getName())
                .getLayer(NetherPortalMarkerLayer.class, Layers.Keys.NETHER_PORTALS)
                .add(center.getBlockX(), center.getBlockY(), center.getBlockZ());
        FeedbackHelper.sendFeedback(result, location);
    }

}
