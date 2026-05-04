package nl.gjorgdy.pl3xmarkers.fabric.helpers;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Blocks;

public class PortalHelper {

    public static BlockPos getNetherPortalCenter(BlockPos lowerCorner, Direction.Axis axis, int width) {
        if (lowerCorner == null) return null;
        if (axis == Direction.Axis.X) {
            return lowerCorner.offset((int) Math.ceil(width / -2.0), 0, 0);
        } else {
            return lowerCorner.offset(0, 0, (int) Math.floor(width / 2.0));
        }
    }

    public static BlockPos getEndPortalCenter(Level world, BlockPos pos) {
        // if there is no portal block north, move south
        if (!world.getBlockState(pos.north()).is(Blocks.END_PORTAL)) {
            pos = pos.south();
        }
        // if there is no portal block south, move north
        if (!world.getBlockState(pos.south()).is(Blocks.END_PORTAL)) {
            pos = pos.north();
        }
        // if there is no portal block east, move west
        if (!world.getBlockState(pos.east()).is(Blocks.END_PORTAL)) {
            pos = pos.west();
        }
        // if there is no portal block west, move east
        if (!world.getBlockState(pos.west()).is(Blocks.END_PORTAL)) {
            pos = pos.east();
        }
        return pos;
    }

}
