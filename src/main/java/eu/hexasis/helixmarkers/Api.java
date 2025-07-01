package eu.hexasis.helixmarkers;

import eu.hexasis.helixmarkers.layers.*;
import eu.hexasis.helixmarkers.markers.MarkerBuilder;
import eu.hexasis.helixmarkers.objects.Position;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.BlockPos;
import net.pl3x.map.core.Pl3xMap;
import net.pl3x.map.core.markers.layer.Layer;
import net.pl3x.map.core.markers.layer.WorldLayer;
import net.pl3x.map.core.world.World;
import org.intellij.lang.annotations.Language;

import java.util.concurrent.*;
import java.util.function.BiConsumer;
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
    public void registerMarkerLayer(String layerKey, String layerLabel, BiConsumer<LayerAccessor, Identifier> loadConsumer) {
        // parse through to internal handler
        HelixMarkers.apiHandler().registerMarkerLayer(
                world -> new SimpleMarkerLayer(layerKey, layerLabel, world, loadConsumer)
        );
    }

    @SuppressWarnings("unused")
    public void registerMarkerLayer(Function<World, MarkerLayer> function) {
        // parse through to internal handler
        HelixMarkers.apiHandler().registerMarkerLayer(function);
    }

    @SuppressWarnings("unused")
    public void registerIcon(String path, String filename, String filetype) {
        // parse through to internal handler
        HelixMarkers.apiHandler().registerIcon(path, filename, filetype);
    }

    @SuppressWarnings("unused")
    public void addMarker(Identifier worldIdentifier, String layerKey, MarkerBuilder<?> markerBuilder) {
        executor.execute(() -> {
            Layer layer = getWorld(worldIdentifier).getLayerRegistry().get(layerKey);
            if (layer instanceof WorldLayer wl) {
                wl.addMarker(markerBuilder.build());
            }
        });
    }

    @SuppressWarnings("unused")
    public void addAreaPoint(Identifier worldIdentifier, @Language("HTML") String label, int color, BlockPos pos) {
        executor.execute(() -> {
            Layer layer = getWorld(worldIdentifier).getLayerRegistry().get("areas");
            if (layer instanceof AreaMarkerLayer aml) {
                aml.addPoint(label, color, new Position(pos.getX(), pos.getZ()));
            }
        });
    }

    @SuppressWarnings("unused")
    public void removeAreaPoint(Identifier worldIdentifier, @Language("HTML") String label, int color, BlockPos pos) {
        executor.execute(() -> {
            Layer layer = getWorld(worldIdentifier).getLayerRegistry().get("areas");
            if (layer instanceof AreaMarkerLayer aml) {
                aml.removePoint(label, color, new Position(pos.getX(), pos.getZ()));
            }
        });
    }

    @SuppressWarnings("unused")
    public void addSimpleMarker(Identifier worldIdentifier, String layerKey, BlockPos pos) {
        executor.execute(() -> {
            Layer layer = getWorld(worldIdentifier).getLayerRegistry().get(layerKey);
            if (layer instanceof SimpleIconMarkerLayer simpleIconMarkerLayer) {
                simpleIconMarkerLayer.addSimpleMarker(pos);
            }
        });
    }

    @SuppressWarnings("unused")
    public void removeSimpleMarker(Identifier worldIdentifier, String layerKey, BlockPos pos) {
        executor.execute(() -> {
            Layer layer = getWorld(worldIdentifier).getLayerRegistry().get(layerKey);
            if (layer instanceof SimpleIconMarkerLayer markerLayer) {
                markerLayer.removeMarker(pos);
            }
        });
    }

    @SuppressWarnings("unused")
    public void removeMarker(Identifier worldIdentifier, String layerKey, String markerKey) {
        executor.execute(() -> {
            Layer layer = getWorld(worldIdentifier).getLayerRegistry().get(layerKey);
            if (layer instanceof SimpleIconMarkerLayer markerLayer) {
                markerLayer.removeMarker(markerKey);
            }
        });
    }

}
