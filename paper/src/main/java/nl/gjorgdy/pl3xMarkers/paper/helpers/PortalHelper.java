package nl.gjorgdy.pl3xMarkers.paper.helpers;

import org.bukkit.Axis;
import org.bukkit.Location;
import org.bukkit.block.BlockType;
import org.bukkit.block.data.Orientable;
import org.jetbrains.annotations.Nullable;

public class PortalHelper {

    @Nullable
    public static Location getNetherPortalCenter(Location location) {
        if (location.getBlock().getBlockData() instanceof Orientable orientable) {
            boolean isX = orientable.getAxis() == Axis.X;
            var lowCorner = location.clone();
            // Move along x or z-axis
            while (lowCorner.getBlock().getType().asBlockType() == BlockType.NETHER_PORTAL) {
                lowCorner = lowCorner.add(isX ? -1 : 0, 0, isX ? 0 : -1);
            }
            // Move along y-axis
            while (lowCorner.getBlock().getType().asBlockType() == BlockType.NETHER_PORTAL) {
                lowCorner = lowCorner.add(0, -1, 0);
            }
            var highCorner = location.clone();
            // Move along x or z-axis
            while (highCorner.getBlock().getType().asBlockType() == BlockType.NETHER_PORTAL) {
                highCorner = highCorner.add(isX ? 1 : 0, 0, isX ? 0 : 1);
            }
            // Move along y-axis
            while (highCorner.getBlock().getType().asBlockType() == BlockType.NETHER_PORTAL) {
                highCorner = highCorner.add(0, 1, 0);
            }
            int deltaX = Math.abs(highCorner.getBlockX() - lowCorner.getBlockX());
            int deltaY = Math.abs(highCorner.getBlockY() - lowCorner.getBlockY());
            int deltaZ = Math.abs(highCorner.getBlockZ() - lowCorner.getBlockZ());
            int centerX = lowCorner.getBlockX() + deltaX / 2;
            int centerY = lowCorner.getBlockY() + deltaY / 2;
            int centerZ = lowCorner.getBlockZ() + deltaZ / 2;
            return location.clone().set(centerX, centerY, centerZ);
        }
        return null;
    }

    public static Location getEndPortalCenter(Location location) {
        // if there is no portal block north, move south
        if (location.clone().add(-1, 0, 0).getBlock().getType().asBlockType() != BlockType.END_PORTAL) {
            location.add(1, 0, 0);
        }
        // if there is no portal block south, move north
        if (location.clone().add(1, 0, 0).getBlock().getType().asBlockType() != BlockType.END_PORTAL) {
            location.add(-1, 0, 0);
        }
        // if there is no portal block east, move west
        if (location.clone().add(0, 0, -1).getBlock().getType().asBlockType() != BlockType.END_PORTAL) {
            location.add(0, 0, 1);
        }
        // if there is no portal block west, move east

        if (location.clone().add(0, 0, 1).getBlock().getType().asBlockType() != BlockType.END_PORTAL) {
            location.add(0, 0, -1);
        }
        return location;
    }

}
