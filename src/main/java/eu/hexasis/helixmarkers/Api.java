package eu.hexasis.helixmarkers;

import eu.hexasis.helixmarkers.layers.primitive.AreaMarkerLayer;
import eu.hexasis.helixmarkers.layers.primitive.IconMarkerLayer;
import eu.hexasis.helixmarkers.layers.primitive.MarkerLayer;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.BlockPos;
import net.pl3x.map.core.Pl3xMap;
import net.pl3x.map.core.markers.layer.Layer;
import net.pl3x.map.core.world.World;
import org.intellij.lang.annotations.Language;

import java.util.concurrent.*;
import java.util.function.Function;

public class Api {

    public ExecutorService executor = new ThreadPoolExecutor(2, 8, 5L, TimeUnit.SECONDS,new LinkedBlockingQueue<>(1000));

    @SuppressWarnings("unused")
    private static World getWorld(Identifier worldIdentifier) {
        World world = Pl3xMap.api().getWorldRegistry().get(worldIdentifier.toString());
        if (world == null) throw new RuntimeException("World not found " + worldIdentifier);
        return world;
    }
    @SuppressWarnings("unused")
    public void registerMarkerLayer(Function<World, MarkerLayer> function) {
        // parse through to internal handler
        HelixMarkers.pl3xHandler().registerMarkerLayer(function);
    }

    @SuppressWarnings("unused")
    public void registerIconImage(String path, String filename, String filetype) {
        // parse through to internal handler
        HelixMarkers.pl3xHandler().registerIconImage(path, filename, filetype);
    }

    @SuppressWarnings("unused")
    public void addAreaPoint(Identifier worldIdentifier, @Language("HTML") String label, int color, BlockPos pos) {
        executor.execute(() -> {
            Layer layer = getWorld(worldIdentifier).getLayerRegistry().get(Layers.Keys.AREAS);
            if (layer instanceof AreaMarkerLayer aml) {
                aml.addPoint(label, color, pos.getX(), pos.getZ());
            }
        });
    }

    @SuppressWarnings("unused")
    public void removeAreaPoint(Identifier worldIdentifier, @Language("HTML") String label, int color, BlockPos pos) {
        executor.execute(() -> {
            Layer layer = getWorld(worldIdentifier).getLayerRegistry().get(Layers.Keys.AREAS);
            if (layer instanceof AreaMarkerLayer aml) {
                aml.removePoint(label, color, pos.getX(), pos.getZ());
            }
        });
    }

    public void addNetherPortalIconMarker(Identifier worldIdentifier, BlockPos pos) {
        addIconMarker(worldIdentifier, Layers.Keys.NETHER_PORTALS, pos);
    }

    public void addEndGatewayIconMarker(Identifier worldIdentifier, BlockPos pos) {
        addIconMarker(worldIdentifier, Layers.Keys.END_GATEWAYS, pos);
    }

    public void addEndPortalIconMarker(Identifier worldIdentifier, BlockPos pos) {
        addIconMarker(worldIdentifier, Layers.Keys.END_PORTALS, pos);
    }

    public void addBeaconIconMarker(Identifier worldIdentifier, BlockPos pos) {
        addIconMarker(worldIdentifier, Layers.Keys.BEACONS, pos);
    }

    public void addIconMarker(Identifier worldIdentifier, String layerKey, BlockPos pos) {
        executor.execute(() -> {
            Layer layer = getWorld(worldIdentifier).getLayerRegistry().get(layerKey);
            if (layer instanceof IconMarkerLayer simpleIconMarkerLayer) {
                simpleIconMarkerLayer.addSimpleMarker(pos);
            }
        });
    }

    public void removeNetherPortalIconMarker(Identifier worldIdentifier, BlockPos pos) {
        removeIconMarker(worldIdentifier, Layers.Keys.NETHER_PORTALS, pos);
    }

    @SuppressWarnings("unused")
    public void removeEndGatewayIconMarker(Identifier worldIdentifier, BlockPos pos) {
        removeIconMarker(worldIdentifier, Layers.Keys.END_GATEWAYS, pos);
    }

    @SuppressWarnings("unused")
    public void removeEndPortalIconMarker(Identifier worldIdentifier, BlockPos pos) {
        removeIconMarker(worldIdentifier, Layers.Keys.END_PORTALS, pos);
    }

    public void removeBeaconIconMarker(Identifier worldIdentifier, BlockPos pos) {
        removeIconMarker(worldIdentifier, Layers.Keys.BEACONS, pos);
    }

    public void removeIconMarker(Identifier worldIdentifier, String layerKey, BlockPos pos) {
        executor.execute(() -> {
            Layer layer = getWorld(worldIdentifier).getLayerRegistry().get(layerKey);
            if (layer instanceof IconMarkerLayer markerLayer) {
                markerLayer.removeMarker(pos);
            }
        });
    }

}
