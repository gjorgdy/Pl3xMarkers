package eu.hexasis.helixmarkers;

import eu.hexasis.helixmarkers.layers.SimpleWorldLayer;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.BlockPos;
import net.pl3x.map.core.Pl3xMap;
import net.pl3x.map.core.markers.layer.Layer;
import net.pl3x.map.core.world.World;

public class MarkerUtils {

    public static void addMarker(Identifier worldIdentifier, String layerKey, BlockPos pos) {
        Layer layer = getWorld(worldIdentifier).getLayerRegistry().get(layerKey);
        if (layer instanceof SimpleWorldLayer swl) {
            swl.addMarker(pos);
        }
    }

    private static World getWorld(Identifier worldIdentifier) {
        World world = Pl3xMap.api().getWorldRegistry().get(worldIdentifier.toString());
        if (world == null) throw new RuntimeException("World not found " + worldIdentifier);
        return world;
    }

}