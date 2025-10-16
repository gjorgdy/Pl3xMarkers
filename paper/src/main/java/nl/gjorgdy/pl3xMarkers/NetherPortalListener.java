package nl.gjorgdy.pl3xMarkers;

import nl.gjorgdy.pl3xmarkers.Pl3xMarkersCore;
import org.bukkit.Axis;
import org.bukkit.Location;
import org.bukkit.block.BlockType;
import org.bukkit.block.data.Orientable;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerPortalEvent;

public class NetherPortalListener implements Listener {

    @EventHandler
    public void onPortalTeleport(PlayerPortalEvent event) {
        var fromBlockType = event.getFrom().getBlock().getType().asBlockType();
        var toBlockType = event.getTo().getBlock().getType().asBlockType();
        if (fromBlockType == BlockType.NETHER_PORTAL) {
            onNetherPortalTeleport(event.getFrom());
        } else if (toBlockType == BlockType.NETHER_PORTAL) {
            onNetherPortalTeleport(event.getTo());
        } else if (fromBlockType == BlockType.END_PORTAL) {
            onEndPortalTeleport(event.getFrom());
        }
    }

    private void onNetherPortalTeleport(Location location) {
        if (location.getBlock().getBlockData() instanceof Orientable orientable) {
            boolean isX = orientable.getAxis() == Axis.X;
            System.out.println("Nether portal axis: " + orientable.getAxis());
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
            Location center = location.clone().set(centerX, 0, centerZ);
            System.out.println("Nether portal at " + center + " size " + deltaX + "x" + deltaZ);
            Pl3xMarkersCore.api().addNetherPortalIconMarker(location.getWorld().getName(), center.getBlockX(), center.getBlockZ());
        }
    }

    private void onEndPortalTeleport(Location location) {

    }

}
