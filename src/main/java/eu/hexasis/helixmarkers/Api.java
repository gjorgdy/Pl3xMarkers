package eu.hexasis.helixmarkers;

import eu.hexasis.helixmarkers.layers.*;
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
    public String registerIconImage(String path, String filename, String filetype) {
        // parse through to internal handler
        HelixMarkers.pl3xHandler().registerIconImage(path, filename, filetype);
        return filename;
    }

    @SuppressWarnings("unused")
    public void addAreaPoint(Identifier worldIdentifier, @Language("HTML") String label, int color, BlockPos pos) {
        executor.execute(() -> {
            Layer layer = getWorld(worldIdentifier).getLayerRegistry().get("areas");
            if (layer instanceof AreaMarkerLayer aml) {
                aml.addPoint(label, color, pos.getX(), pos.getZ());
            }
        });
    }

    @SuppressWarnings("unused")
    public void removeAreaPoint(Identifier worldIdentifier, @Language("HTML") String label, int color, BlockPos pos) {
        executor.execute(() -> {
            Layer layer = getWorld(worldIdentifier).getLayerRegistry().get("areas");
            if (layer instanceof AreaMarkerLayer aml) {
                aml.removePoint(label, color, pos.getX(), pos.getZ());
            }
        });
    }

    @SuppressWarnings("unused")
    public void addIconMarker(Identifier worldIdentifier, String layerKey, BlockPos pos) {
        executor.execute(() -> {
            Layer layer = getWorld(worldIdentifier).getLayerRegistry().get(layerKey);
            if (layer instanceof IconMarkerLayer simpleIconMarkerLayer) {
                simpleIconMarkerLayer.addSimpleMarker(pos);
            }
        });
    }

    @SuppressWarnings("unused")
    public void removeIconMarker(Identifier worldIdentifier, String layerKey, BlockPos pos) {
        executor.execute(() -> {
            Layer layer = getWorld(worldIdentifier).getLayerRegistry().get(layerKey);
            if (layer instanceof IconMarkerLayer markerLayer) {
                markerLayer.removeMarker(pos);
            }
        });
    }

}
