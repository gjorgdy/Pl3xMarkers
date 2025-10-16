package nl.gjorgdy.pl3xMarkers;

import nl.gjorgdy.pl3xmarkers.Pl3xMarkersCore;
import org.bukkit.Axis;
import org.bukkit.Location;
import org.bukkit.block.BlockType;
import org.bukkit.block.data.Orientable;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.player.PlayerPortalEvent;

public class NetherPortalListener implements Listener {

    @EventHandler
    public void onPortalTeleport(PlayerPortalEvent event) {
        var fromBlockType = event.getFrom().getBlock().getType().asBlockType();
        if (fromBlockType == BlockType.NETHER_PORTAL) {
            onNetherPortalTeleport(event.getFrom());
        } else if (fromBlockType == BlockType.END_PORTAL) {
            onEndPortalTeleport(event.getFrom());
        }
    }

    @EventHandler
    public void onBlockBreak(BlockBreakEvent event) {
        var blockType = event.getBlock().getType().asBlockType();
        var location = event.getBlock().getLocation();
        if (blockType == BlockType.OBSIDIAN) {
            // check if there is a nether portal nearby
            var nearby = location.clone();
            for (int x = -1; x <= 1; x++) {
                for (int y = -1; y <= 1; y++) {
                    for (int z = -1; z <= 1; z++) {
                        nearby.set(location.getBlockX() + x, location.getBlockY() + y, location.getBlockZ() + z);
                        if (nearby.getBlock().getType().asBlockType() == BlockType.NETHER_PORTAL) {
                            var center = getNetherPortalCenter(nearby);
                            onNetherPortalBreak(center);
                            return;
                        }
                    }
                }
            }
        }
        if (blockType == BlockType.NETHER_PORTAL) {
            var center = getNetherPortalCenter(location);
            onNetherPortalBreak(center);
        }
    }

    private void onNetherPortalBreak(Location location) {
        var center = getNetherPortalCenter(location);
        Pl3xMarkersCore.api().removeNetherPortalIconMarker(location.getWorld().getName(), center.getBlockX(), center.getBlockZ());
    }

    private void onNetherPortalTeleport(Location location) {
        var center = getNetherPortalCenter(location);
        Pl3xMarkersCore.api().addNetherPortalIconMarker(location.getWorld().getName(), center.getBlockX(), center.getBlockZ());
    }

    private Location getNetherPortalCenter(Location location) {
        if (location.getBlock().getBlockData() instanceof Orientable orientable) {
            boolean isX = orientable.getAxis() == Axis.X;
            var lowCorner = location.clone();
            while (lowCorner.getBlock().getType().asBlockType() == BlockType.NETHER_PORTAL) {
                lowCorner = lowCorner.add(isX ? -1 : 0, 0, isX ? 0 : -1);
            }
            var highCorner = location.clone();
            while (highCorner.getBlock().getType().asBlockType() == BlockType.NETHER_PORTAL) {
                highCorner = highCorner.add(isX ? 1 : 0, 0, isX ? 0 : 1);
            }
            int deltaX = Math.abs(highCorner.getBlockX() - lowCorner.getBlockX());
            int deltaZ = Math.abs(highCorner.getBlockZ() - lowCorner.getBlockZ());
            int centerX = lowCorner.getBlockX() + deltaX / 2;
            int centerZ = lowCorner.getBlockZ() + deltaZ / 2;
            return location.clone().set(centerX, 0, centerZ);
        }
        return location;
    }

    private void onEndPortalTeleport(Location location) {

    }

}
