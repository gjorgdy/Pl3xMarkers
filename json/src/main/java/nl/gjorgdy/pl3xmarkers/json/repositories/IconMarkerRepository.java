package nl.gjorgdy.pl3xmarkers.json.repositories;

import nl.gjorgdy.pl3xmarkers.core.interfaces.IIconMarkerRepository;
import nl.gjorgdy.pl3xmarkers.json.entities.IconMarker;
import nl.gjorgdy.pl3xmarkers.json.interfaces.IJsonRepositoryData;

import java.util.*;

public class IconMarkerRepository extends JsonRepository<IconMarkerRepository.Data> implements IIconMarkerRepository<IconMarker> {

	public IconMarkerRepository(String folderPath, String fileName) {
		super(folderPath, fileName, Data.class, new Data());
	}

	@Override
	public Collection<IconMarker> getIconMarkers(String worldIdentifier, String layerKey) {
		var world = data.get(worldIdentifier);
		if (world == null) return Set.of();
		var layer = world.get(layerKey);
		return layer != null ? layer : Set.of();
	}

	@Override
	public IconMarker createIconMarker(String worldIdentifier, String layerKey, int x, int z) {
		var marker = new IconMarker(worldIdentifier, layerKey, x, z);
		var added = data.add(marker);
		if (added) markDirty();
		return added ? marker : null;
	}

	@Override
	public boolean removeIconMarker(String worldIdentifier, String layerKey, int x, int z) {
		return data.remove(worldIdentifier, layerKey, x, z);
	}

	public static class Data extends HashMap<String, WorldData> implements IJsonRepositoryData {

		public boolean add(IconMarker iconMarker) {
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

		public WorldData getOrCreate(String key) {
			return super.computeIfAbsent(key, k -> new WorldData());
		}

		@Override
		public void strip() {
			entrySet().removeIf(e -> e.getValue().isEmpty());
			values().forEach(WorldData::strip);
		}

	}

	public static class WorldData extends HashMap<String, LayerData> {

		public void strip() {
			entrySet().removeIf(e -> e.getValue().isEmpty());
		}

		@Override
		public boolean isEmpty() {
			return super.isEmpty() || super.values().stream().allMatch(Set::isEmpty);
		}

		public LayerData getOrCreate(String key) {
			return super.computeIfAbsent(key, k -> new LayerData());
		}

		public void setContext(String worldIdentifier) {
			this.forEach((layerKey, layer) -> layer.setContext(worldIdentifier, layerKey));
		}

	}

	public static class LayerData extends HashSet<IconMarker> {

		public void setContext(String worldIdentifier, String layerKey) {
			this.forEach(m -> m.setContext(worldIdentifier, layerKey));
		}

	}

}
