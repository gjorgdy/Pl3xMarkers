package eu.hexasis.helixmarkers;

import eu.hexasis.helixmarkers.layers.SimpleMarkerLayer;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.BlockPos;
import net.pl3x.map.core.Pl3xMap;
import net.pl3x.map.core.markers.layer.Layer;
import net.pl3x.map.core.markers.layer.WorldLayer;
import net.pl3x.map.core.markers.marker.Marker;
import net.pl3x.map.core.world.World;

public class MarkerUtils {

    public static void addMarker(Identifier worldIdentifier, String layerKey, Marker<?> marker) {
        Layer layer = getWorld(worldIdentifier).getLayerRegistry().get(layerKey);
        if (layer instanceof WorldLayer wl) {
            wl.addMarker(marker);
        }
    }

    public static void addSimpleMarker(Identifier worldIdentifier, String layerKey, BlockPos pos) {
        Layer layer = getWorld(worldIdentifier).getLayerRegistry().get(layerKey);
        if (layer instanceof SimpleMarkerLayer simpleMarkerLayer) {
            simpleMarkerLayer.addSimpleMarker(pos);
        }
    }

    public static void removeSimpleMarker(Identifier worldIdentifier, String layerKey, BlockPos pos) {
        Layer layer = getWorld(worldIdentifier).getLayerRegistry().get(layerKey);
        if (layer instanceof SimpleMarkerLayer markerLayer) {
            markerLayer.removeMarker(pos);
        }
    }

    public static void removeMarker(Identifier worldIdentifier, String layerKey, String markerKey) {
        Layer layer = getWorld(worldIdentifier).getLayerRegistry().get(layerKey);
        if (layer instanceof SimpleMarkerLayer markerLayer) {
            markerLayer.removeMarker(markerKey);
        }
    }

    private static World getWorld(Identifier worldIdentifier) {
        World world = Pl3xMap.api().getWorldRegistry().get(worldIdentifier.toString());
        if (world == null) throw new RuntimeException("World not found " + worldIdentifier);
        return world;
    }

}