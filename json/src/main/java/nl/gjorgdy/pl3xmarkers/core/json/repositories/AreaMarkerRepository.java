package nl.gjorgdy.pl3xmarkers.core.json.repositories;

import nl.gjorgdy.pl3xmarkers.core.interfaces.IAreaMarkerRepository;
import nl.gjorgdy.pl3xmarkers.core.json.entities.AreaMarker;
import nl.gjorgdy.pl3xmarkers.core.json.interfaces.IJsonRepositoryData;

import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;

public class AreaMarkerRepository extends JsonRepository<AreaMarkerRepository.Data> implements IAreaMarkerRepository<AreaMarker> {

	public AreaMarkerRepository(String folderPath, String fileName) {
		super(folderPath, fileName, Data.class, new Data());
	}

	@Override
	public Collection<AreaMarker> getAreas(String worldIdentifier) {
		var area = data.get(worldIdentifier);
		return area != null ? area : Set.of();
	}

	@Override
	public AreaMarker getArea(String worldIdentifier, String name, int color) {
		var area = data.get(worldIdentifier);
		return area != null ? area.findFirst(name, color) : null;
	}

	@Override
	public AreaMarker getOrCreateArea(String worldIdentifier, String name, int color) {
		var marker = data.getOrCreate(worldIdentifier).findFirst(name, color);
		if (marker == null) {
			marker = new AreaMarker(this, worldIdentifier, name, color);
			getAreas(worldIdentifier).add(marker);
			markDirty();
		}
		return marker;
	}

	@Override
	public boolean removeArea(String worldIdentifier, String name, int color) {
		var removed = data.getOrCreate(worldIdentifier).remove(name, color);
		if (removed) markDirty();
		return removed;
	}

	public boolean removeArea(AreaMarker areaMarker) {
		var removed = getAreas(areaMarker.getWorld()).remove(areaMarker);
		if (removed) markDirty();
		return removed;
	}

	public static class Data extends HashMap<String, WorldData> implements IJsonRepositoryData {

		public WorldData getOrCreate(String key) {
			return super.computeIfAbsent(key, k -> new WorldData());
		}

		@Override
		public void strip() {
			entrySet().removeIf(e -> e.getValue().isEmpty());
		}

		@Override
		public boolean isEmpty() {
			return super.isEmpty() || super.values().stream().allMatch(Set::isEmpty);
		}

		@Override
		public void setContext(JsonRepository<?> jsonRepository) {
			this.forEach((worldIdentifier, world) -> world.setContext(jsonRepository, worldIdentifier));
		}

	}

	public static class WorldData extends HashSet<AreaMarker> {

		public AreaMarker findFirst(String name, int color) {
			return this.stream()
					   .filter(m -> m.getName().equals(name) && m.getColor() == color)
					   .findFirst()
					   .orElse(null);
		}

		public boolean remove(String name, int color) {
			return this.removeIf(m -> m.getName().equals(name) && m.getColor() == color);
		}

		public void setContext(JsonRepository<?> jsonRepository, String worldIdentifier) {
			this.forEach(m -> m.setContext((AreaMarkerRepository) jsonRepository, worldIdentifier));
		}

	}

}
