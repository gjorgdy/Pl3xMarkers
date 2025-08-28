package eu.hexasis.helixmarkers.helpers;

import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;

public class PortalHelper {

    public static BlockPos getNetherPortalCenter(BlockPos lowerCorner, Direction.Axis axis, int width) {
        if (lowerCorner == null) return null;
        if (axis == Direction.Axis.X) {
            return lowerCorner.add((int) Math.ceil(width / -2.0), 0, 0);
        } else {
            return lowerCorner.add(0, 0, (int) Math.floor(width / 2.0));
        }
    }

}
