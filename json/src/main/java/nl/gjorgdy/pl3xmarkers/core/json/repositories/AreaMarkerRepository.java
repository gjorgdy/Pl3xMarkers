package nl.gjorgdy.pl3xmarkers.core.json.repositories;

import nl.gjorgdy.pl3xmarkers.core.interfaces.IAreaMarkerRepository;
import nl.gjorgdy.pl3xmarkers.core.json.entities.AreaMarker;

public class AreaMarkerRepository extends MarkerRepository<AreaMarker> implements IAreaMarkerRepository<AreaMarker> {

	public AreaMarkerRepository(WorldRepository worldRepository, String layerKey) {
		super(worldRepository, layerKey, AreaMarker.class);
		// STORAGE MIGRATION
		worldRepository.getStorage().oldJsonStorage()
				.getAreaMarkerRepository()
				.getAreas(worldRepository.worldIdentifier)
				.forEach(oldArea -> {
					var area = getOrCreate(oldArea.getName(), oldArea.getColor());
					oldArea.getPoints().forEach(point ->
							                            area.addPoint(point.getX(), Integer.MIN_VALUE, point.getZ())
					);
				});
		write();
		// STORAGE MIGRATION
	}

	@Override
	public AreaMarker get(String name, int color) {
		return data.stream()
				.filter(area -> area.getName().equals(name) && area.getColor() == color)
				.findFirst()
				.orElse(null);
	}

	@Override
	public AreaMarker getOrCreate(String name, int color) {
		var area = get(name, color);
		if (area == null) {
			area = new AreaMarker(this, name, color);
			data.add(area);
			markDirty();
		}
		return area;
	}

	@Override
	public boolean remove(String name, int color) {
		boolean removed = data.removeIf(area -> area.getName().equals(name) && area.getColor() == color);
		if (removed) {
			markDirty();
		}
		return removed;
	}
}
