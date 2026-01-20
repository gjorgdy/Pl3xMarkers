package nl.gjorgdy.pl3xmarkers.core.json.repositories;

import com.google.gson.reflect.TypeToken;
import nl.gjorgdy.pl3xmarkers.core.json.entities.IconMarker;
import nl.gjorgdy.pl3xmarkers.core.json.interfaces.IJsonRepositoryData;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class BaseIconMarkerRepository<T extends IconMarker> extends JsonRepository<BaseIconMarkerRepository.Data<T>> {

	public BaseIconMarkerRepository(String folderPath, String fileName, TypeToken<?> typeToken) {
		super(folderPath, fileName, typeToken, new Data<>());
	}

	public static class Data<T extends IconMarker> extends HashMap<String, WorldData<T>> implements IJsonRepositoryData {

		public boolean add(T iconMarker) {
			return this
						   .getOrCreate(iconMarker.getWorld())
						   .getOrCreate(iconMarker.getLayer())
						   .add(iconMarker);
		}

		public boolean remove(String worldIdentifier, String layerKey, int x, int z) {
			var worldData = this.get(worldIdentifier);
			if (worldData == null) return false;
			var layerData = worldData.get(layerKey);
			if (layerData == null) return false;
			return layerData.removeIf(m -> m.getX() == x && m.getZ() == z);
		}

		@Override
		public boolean isEmpty() {
			return super.isEmpty() || super.values().stream().allMatch(Map::isEmpty);
		}

		@Override
		public void setContext(JsonRepository<?> jsonRepository) {
			this.forEach((worldIdentifier, world) -> world.setContext(worldIdentifier));
		}

		public WorldData<T> getOrCreate(String key) {
			return super.computeIfAbsent(key, k -> new WorldData<>());
		}

		@Override
		public void strip() {
			entrySet().removeIf(e -> e.getValue().isEmpty());
			values().forEach(WorldData::strip);
		}

	}

	public static class WorldData<T extends IconMarker> extends HashMap<String, LayerData<T>> {

		public void strip() {
			entrySet().removeIf(e -> e.getValue().isEmpty());
		}

		@Override
		public boolean isEmpty() {
			return super.isEmpty() || super.values().stream().allMatch(Set::isEmpty);
		}

		public LayerData<T> getOrCreate(String key) {
			return super.computeIfAbsent(key, k -> new LayerData<>());
		}

		public void setContext(String worldIdentifier) {
			this.forEach((layerKey, layer) -> layer.setContext(worldIdentifier, layerKey));
		}

	}

	public static class LayerData<T extends IconMarker> extends HashSet<T> {

		public void setContext(String worldIdentifier, String layerKey) {
			this.forEach(m -> m.setContext(worldIdentifier, layerKey));
		}

	}

}
