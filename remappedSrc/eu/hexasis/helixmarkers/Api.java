package eu.hexasis.helixmarkers;

import eu.hexasis.helixmarkers.layers.LayerAccessor;
import eu.hexasis.helixmarkers.layers.MarkerLayer;
import eu.hexasis.helixmarkers.layers.SimpleIconMarkerLayer;
import eu.hexasis.helixmarkers.layers.SimpleMarkerLayer;
import eu.hexasis.helixmarkers.markers.MarkerBuilder;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.BlockPos;
import net.pl3x.map.core.Pl3xMap;
import net.pl3x.map.core.markers.layer.Layer;
import net.pl3x.map.core.markers.layer.WorldLayer;
import net.pl3x.map.core.world.World;

import java.util.function.BiConsumer;
import java.util.function.Function;

public class Api {

    private static World getWorld(Identifier worldIdentifier) {
        World world = Pl3xMap.api().getWorldRegistry().get(worldIdentifier.toString());
        if (world == null) throw new RuntimeException("World not found " + worldIdentifier);
        return world;
    }

    public void registerMarkerLayer(String layerKey, String layerLabel, BiConsumer<LayerAccessor, Identifier> loadConsumer) {
        // parse through to internal handler
        HelixMarkers.apiHandler().registerMarkerLayer(
                world -> new SimpleMarkerLayer(layerKey, layerLabel, world, loadConsumer)
        );
    }

    public void registerMarkerLayer(Function<World, MarkerLayer> function) {
        // parse through to internal handler
        HelixMarkers.apiHandler().registerMarkerLayer(function);
    }

    public void registerIcon(String path, String filename, String filetype) {
        // parse through to internal handler
        HelixMarkers.apiHandler().registerIcon(path, filename, filetype);
    }

    public void addMarker(Identifier worldIdentifier, String layerKey, MarkerBuilder markerBuilder) {
        Layer layer = getWorld(worldIdentifier).getLayerRegistry().get(layerKey);
        if (layer instanceof WorldLayer wl) {
            wl.addMarker(markerBuilder.build());
        }
    }

    public void addSimpleMarker(Identifier worldIdentifier, String layerKey, BlockPos pos) {
        Layer layer = getWorld(worldIdentifier).getLayerRegistry().get(layerKey);
        if (layer instanceof SimpleIconMarkerLayer simpleIconMarkerLayer) {
            simpleIconMarkerLayer.addSimpleMarker(pos);
        }
    }

    public void removeSimpleMarker(Identifier worldIdentifier, String layerKey, BlockPos pos) {
        Layer layer = getWorld(worldIdentifier).getLayerRegistry().get(layerKey);
        if (layer instanceof SimpleIconMarkerLayer markerLayer) {
            markerLayer.removeMarker(pos);
        }
    }

    public void removeMarker(Identifier worldIdentifier, String layerKey, String markerKey) {
        Layer layer = getWorld(worldIdentifier).getLayerRegistry().get(layerKey);
        if (layer instanceof SimpleIconMarkerLayer markerLayer) {
            markerLayer.removeMarker(markerKey);
        }
    }

}
