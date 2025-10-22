package nl.gjorgdy.pl3xmarkers.json.repositories;

import nl.gjorgdy.pl3xmarkers.core.interfaces.IAreaMarkerRepository;
import nl.gjorgdy.pl3xmarkers.json.entities.AreaMarker;

import java.util.List;

public class AreaMarkerRepository extends JsonRepository<AreaMarker> implements IAreaMarkerRepository<AreaMarker> {

	public AreaMarkerRepository(String folderPath, String fileName) {
		super(folderPath, fileName, AreaMarker[].class);
	}

	@Override
	public List<AreaMarker> getAreas(String worldIdentifier) {
		return markers
				   .stream()
				   .filter(m -> m.getWorld().equals(worldIdentifier))
				   .toList();
	}

	@Override
	public AreaMarker getArea(String worldIdentifier, String name, int color) {
		return markers
			   .stream()
			   .filter(m ->
				   m.getWorld().equals(worldIdentifier)
				   && m.getName().equals(name)
				   && m.getColor() == color
			   )
			   .findFirst()
			   .orElse(null);
	}

	@Override
	public AreaMarker getOrCreateArea(String worldIdentifier, String name, int color) {
		var marker = getArea(worldIdentifier, name, color);
		if (marker == null) {
			marker = new AreaMarker(this, worldIdentifier, name, color);
			markers.add(marker);
			markDirty();
		}
		return marker;
	}

	@Override
	public boolean removeArea(String worldIdentifier, String name, int color) {
		var removed = markers.removeIf(
				m ->
					m.getWorld().equals(worldIdentifier)
					&& m.getName().equals(name)
					&& m.getColor() == color
		);
		if (removed) markDirty();
		return removed;
	}

	public boolean removeArea(AreaMarker areaMarker) {
		var removed = markers.remove(areaMarker);
		if (removed) markDirty();
		return removed;
	}

}
