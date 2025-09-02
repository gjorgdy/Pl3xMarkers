package nl.gjorgdy.pl3xmarkers.interfaces;

import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public interface NetherPortalInterface {

    BlockPos pl3xMarkers$getPortalCenter();

    void pl3xMarkers$createMarker(World world);

}
