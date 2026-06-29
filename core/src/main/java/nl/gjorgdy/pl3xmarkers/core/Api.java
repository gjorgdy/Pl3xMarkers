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
		Pl3xMap.api().getWorldRegistry().forEach(world -> worldApis.put(world.getKey(), new WorldApi(world)));
	}

	@Override
	public IWorldApi getWorld(String worldIdentifier) {
		return worldApis.computeIfAbsent(worldIdentifier, WorldApi::new);
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

	private class WorldApi implements IWorldApi {

		private final String worldIdentifier;
		private final World pl3xWorld;

		private WorldApi(World pl3xWorld) {
			this.pl3xWorld = pl3xWorld;
			worldIdentifier = pl3xWorld.getKey();
		}

		private WorldApi(String worldIdentifier) {
			pl3xWorld = Pl3xMap.api().getWorldRegistry().get(worldIdentifier);
			this.worldIdentifier = worldIdentifier;
		}

		@Override
		@Nullable
		public <T extends MarkerLayer> T getLayer(Class<T> layerClass, String layerKey) {
			if (pl3xWorld == null) {
				worldApis.remove(worldIdentifier);
				return null;
			}
			Layer layer = pl3xWorld.getLayerRegistry().get(layerKey);
			if (layerClass.isInstance(layer)) {
				return layerClass.cast(layer);
			}
			return null;
		}

	}

}
