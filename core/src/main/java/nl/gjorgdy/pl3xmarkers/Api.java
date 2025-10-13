package nl.gjorgdy.pl3xmarkers;

import nl.gjorgdy.pl3xmarkers.layers.primitive.AreaMarkerLayer;
import nl.gjorgdy.pl3xmarkers.layers.primitive.IconMarkerLayer;
import nl.gjorgdy.pl3xmarkers.layers.primitive.MarkerLayer;
import net.pl3x.map.core.Pl3xMap;
import net.pl3x.map.core.markers.layer.Layer;
import net.pl3x.map.core.world.World;
import org.intellij.lang.annotations.Language;

import java.util.concurrent.*;
import java.util.function.Function;

public class Api {

    public ExecutorService executor = new ThreadPoolExecutor(2, 8, 5L, TimeUnit.SECONDS,new LinkedBlockingQueue<>(1000));

    @SuppressWarnings("unused")
    private static World getWorld(String worldIdentifier) {
        World world = Pl3xMap.api().getWorldRegistry().get(worldIdentifier);
        if (world == null) throw new RuntimeException("World not found " + worldIdentifier);
        return world;
    }
    @SuppressWarnings("unused")
    public void registerMarkerLayer(Function<World, MarkerLayer> function) {
        // parse through to internal handler
        Pl3xMarkersCore.pl3xHandler().registerMarkerLayer(function);
    }

    @SuppressWarnings("unused")
    public void registerIconImage(String path, String filename, String filetype) {
        // parse through to internal handler
        Pl3xMarkersCore.pl3xHandler().registerIconImage(path, filename, filetype);
    }

    @SuppressWarnings("unused")
    public void addAreaPoint(String worldIdentifier, @Language("HTML") String label, int color, int x, int z) {
        executor.execute(() -> {
            Layer layer = getWorld(worldIdentifier).getLayerRegistry().get(Layers.Keys.AREAS);
            if (layer instanceof AreaMarkerLayer aml) {
                aml.addPoint(label, color, x, z);
            }
        });
    }

    @SuppressWarnings("unused")
    public void removeAreaPoint(String worldIdentifier, @Language("HTML") String label, int color, int x, int z) {
        executor.execute(() -> {
            Layer layer = getWorld(worldIdentifier).getLayerRegistry().get(Layers.Keys.AREAS);
            if (layer instanceof AreaMarkerLayer aml) {
                aml.removePoint(label, color, x, z);
            }
        });
    }

    public void addNetherPortalIconMarker(String worldIdentifier, int x, int z) {
        addIconMarker(worldIdentifier, Layers.Keys.NETHER_PORTALS, x, z);
    }

    public void addEndGatewayIconMarker(String worldIdentifier, int x, int z) {
        addIconMarker(worldIdentifier, Layers.Keys.END_GATEWAYS, x, z);
    }

    public void addEndPortalIconMarker(String worldIdentifier, int x, int z) {
        addIconMarker(worldIdentifier, Layers.Keys.END_PORTALS, x, z);
    }

    public void addBeaconIconMarker(String worldIdentifier, int x, int z) {
        addIconMarker(worldIdentifier, Layers.Keys.BEACONS, x, z);
    }

    public void addIconMarker(String worldIdentifier, String layerKey, int x, int z) {
        executor.execute(() -> {
            Layer layer = getWorld(worldIdentifier).getLayerRegistry().get(layerKey);
            if (layer instanceof IconMarkerLayer simpleIconMarkerLayer) {
                simpleIconMarkerLayer.addSimpleMarker(x, z);
            }
        });
    }

    public void removeNetherPortalIconMarker(String worldIdentifier, int x, int z) {
        removeIconMarker(worldIdentifier, Layers.Keys.NETHER_PORTALS, x, z);
    }

    @SuppressWarnings("unused")
    public void removeEndGatewayIconMarker(String worldIdentifier, int x, int z) {
        removeIconMarker(worldIdentifier, Layers.Keys.END_GATEWAYS, x, z);
    }

    @SuppressWarnings("unused")
    public void removeEndPortalIconMarker(String worldIdentifier, int x, int z) {
        removeIconMarker(worldIdentifier, Layers.Keys.END_PORTALS, x, z);
    }

    public void removeBeaconIconMarker(String worldIdentifier, int x, int z) {
        removeIconMarker(worldIdentifier, Layers.Keys.BEACONS, x, z);
    }

    public void removeIconMarker(String worldIdentifier, String layerKey, int x, int z) {
        executor.execute(() -> {
            Layer layer = getWorld(worldIdentifier).getLayerRegistry().get(layerKey);
            if (layer instanceof IconMarkerLayer markerLayer) {
                markerLayer.removeMarker(x, z);
            }
        });
    }

}
