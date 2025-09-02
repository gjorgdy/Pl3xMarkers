package nl.gjorgdy.pl3xmarkers.helpers;

import net.minecraft.block.Blocks;
import net.minecraft.state.property.Properties;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.world.World;
import net.minecraft.world.dimension.NetherPortal;
import nl.gjorgdy.pl3xmarkers.interfaces.NetherPortalInterface;
import org.jetbrains.annotations.Nullable;

public class PortalHelper {

    @Nullable
    public static BlockPos getNetherPortalCenter(World world, BlockPos pos) {
        var portal = NetherPortal.getOnAxis(world, pos, world.getBlockState(pos).get(Properties.HORIZONTAL_AXIS));
        if (portal instanceof NetherPortalInterface npi) {
            return npi.pl3xMarkers$getPortalCenter();
        }
        return null;
    }

    public static BlockPos getNetherPortalCenter(BlockPos lowerCorner, Direction.Axis axis, int width) {
        if (lowerCorner == null) return null;
        if (axis == Direction.Axis.X) {
            return lowerCorner.add((int) Math.ceil(width / -2.0), 0, 0);
        } else {
            return lowerCorner.add(0, 0, (int) Math.floor(width / 2.0));
        }
    }

    public static BlockPos getEndPortalCenter(World world, BlockPos pos) {
        // if there is no portal block north, move south
        if (!world.getBlockState(pos.north()).isOf(Blocks.END_PORTAL)) {
            pos = pos.south();
        }
        // if there is no portal block south, move north
        if (!world.getBlockState(pos.south()).isOf(Blocks.END_PORTAL)) {
            pos = pos.north();
        }
        // if there is no portal block east, move west
        if (!world.getBlockState(pos.east()).isOf(Blocks.END_PORTAL)) {
            pos = pos.west();
        }
        // if there is no portal block west, move east
        if (!world.getBlockState(pos.west()).isOf(Blocks.END_PORTAL)) {
            pos = pos.east();
        }
        return pos;
    }

}
