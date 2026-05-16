package nl.gjorgdy.pl3xmarkers.core;

import net.pl3x.map.core.Pl3xMap;
import net.pl3x.map.core.markers.layer.Layer;
import net.pl3x.map.core.world.World;
import nl.gjorgdy.pl3xmarkers.core.interfaces.api.IApi;
import nl.gjorgdy.pl3xmarkers.core.interfaces.api.IWorldApi;
import nl.gjorgdy.pl3xmarkers.core.layers.primitive.MarkerLayer;
import nl.gjorgdy.pl3xmarkers.core.objects.LayerFactory;
import org.jspecify.annotations.Nullable;

import java.util.HashMap;

public class Api implements IApi {

	private final HashMap<String, IWorldApi> worldApis = new HashMap<>();

	Api() {
		Pl3xMap.api().getWorldRegistry().forEach(world -> worldApis.put(world.getKey(), new WorldApi(world.getKey())));
	}

	@SuppressWarnings("unused")
	private static World getPl3xWorld(String worldIdentifier) {
        World world = Pl3xMap.api().getWorldRegistry().get(worldIdentifier);
		if (world == null) {
			throw new RuntimeException("World not found " + worldIdentifier);
		}
        return world;
    }

	@Override
	public IWorldApi getWorld(String worldIdentifier) {
		return worldApis.get(worldIdentifier);
	}

	@Override
    @SuppressWarnings("unused")
    public void registerMarkerLayer(LayerFactory factory) {
        // parse through to internal handler
        Pl3xMarkersCore.pl3xHandler().registerMarkerLayer(factory);
    }

	@Override
    @SuppressWarnings("unused")
    public void registerIconImage(String path, String filename, String filetype) {
        // parse through to internal handler
        Pl3xMarkersCore.pl3xHandler().registerIconImage(path, filename, filetype);
    }

	private record WorldApi(String worldIdentifier) implements IWorldApi {

		@Override
		@Nullable
		public <T extends MarkerLayer> T getLayer(Class<T> layerClass, String layerKey) {
			Layer layer = Api.getPl3xWorld(worldIdentifier).getLayerRegistry().get(layerKey);
			if (layerClass.isInstance(layer)) {
				return layerClass.cast(layer);
			}
			return null;
		}

	}

}
