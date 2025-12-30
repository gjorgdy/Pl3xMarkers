package nl.gjorgdy.pl3xmarkers.core.json.repositories;

import nl.gjorgdy.pl3xmarkers.core.interfaces.ILineMarkerRepository;
import nl.gjorgdy.pl3xmarkers.core.interfaces.entities.IPoint;
import nl.gjorgdy.pl3xmarkers.core.json.entities.LineMarker;
import nl.gjorgdy.pl3xmarkers.core.json.interfaces.IJsonRepositoryData;

import java.util.*;

public class LineMarkerRepository extends JsonRepository<LineMarkerRepository.Data> implements ILineMarkerRepository<LineMarker> {

	public LineMarkerRepository(String folderPath, String fileName) {
		super(folderPath, fileName, Data.class, new Data());
	}

	@Override
	public Collection<LineMarker> getLineMarkers(String worldIdentifier) {
		return List.of();
	}

	@Override
	public LineMarker getLineMarker(String worldIdentifier, IPoint lowerPoint, IPoint higherPoint) {
		return null;
	}

	@Override
	public LineMarker getLineMarker(String worldIdentifier, IPoint point) {
		return null;
	}

	@Override
	public LineMarker createLineMarker(String worldIdentifier, IPoint pointA, IPoint pointB) {
		return null;
	}

	@Override
	public boolean removeLineMarker(String worldIdentifier, IPoint lowerPoint, IPoint higherPoint) {
		return false;
	}

	@Override
	public boolean removeLineMarker(String worldIdentifier, IPoint point) {
		return data.get(worldIdentifier);
	}

	public static class Data extends HashMap<String, WorldData> implements IJsonRepositoryData {

		@Override
		public void strip() {
			entrySet().removeIf(entry -> entry.getValue().isEmpty());
		}

		@Override
		public boolean isEmpty() {
			return entrySet().isEmpty() || values().stream().allMatch(Set::isEmpty);
		}

		@Override
		public void setContext(JsonRepository<?> jsonRepository) {
			forEach((worldKey, worldData) -> worldData.setContext(worldKey, (LineMarkerRepository) jsonRepository));
		}
	}

	public static class WorldData extends HashSet<LineMarker> {

		public LineMarker get(IPoint lowerPoint, IPoint higherPoint) {
			return stream().filter(l ->
						l.getLowerPoint().equals(lowerPoint)
							&& l.getUpperPoint().equals(higherPoint)
					)
					.findFirst()
					.orElse(null);
		}

		public void setContext(String worldIdentifier, LineMarkerRepository repository) {
			forEach(marker -> marker.setContext(repository, worldIdentifier));
		}

	}

}
