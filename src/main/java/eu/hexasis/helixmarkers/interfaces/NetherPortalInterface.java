package eu.hexasis.helixmarkers.interfaces;

import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public interface NetherPortalInterface {

    BlockPos helixMarkers$getCenter();

    void helixMarkers$createMarker(World world);

}
