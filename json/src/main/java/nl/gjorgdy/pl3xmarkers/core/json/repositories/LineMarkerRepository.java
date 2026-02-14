package nl.gjorgdy.pl3xmarkers.core.json.repositories;

import com.google.gson.reflect.TypeToken;
import nl.gjorgdy.pl3xmarkers.core.interfaces.ILineMarkerRepository;
import nl.gjorgdy.pl3xmarkers.core.interfaces.entities.IPoint;
import nl.gjorgdy.pl3xmarkers.core.json.entities.LineMarker;
import nl.gjorgdy.pl3xmarkers.core.json.entities.Point;
import nl.gjorgdy.pl3xmarkers.core.json.interfaces.IJsonRepositoryData;

import java.util.*;

public class LineMarkerRepository extends JsonRepository<LineMarkerRepository.Data> implements ILineMarkerRepository<LineMarker, Point> {

	public LineMarkerRepository(String folderPath, String fileName) {
		super(folderPath, fileName, TypeToken.get(Data.class), new Data());
		// TODO: remove test data
		createLineMarker(
				"world",
				List.of(
						new Point(20, 64),
						new Point(20, 64),
						new Point(20, 44),
						new Point(40, 24)
				)
		);
		// TODO: remove test data
		markDirty();
	}

	@Override
	public Collection<LineMarker> getLineMarkers(String worldIdentifier) {
		return data.get(worldIdentifier);
	}

	@Override
	public LineMarker getLineMarker(String worldIdentifier, IPoint firstPoint, IPoint lastPoint) {
		return data.get(worldIdentifier).stream().filter(
				l -> l.getFirstPoint().equals(firstPoint)
							 && l.getLastPoint().equals(lastPoint)
		).findFirst().orElse(null);
	}

	@Override
	public LineMarker getLineMarker(String worldIdentifier, IPoint point) {
		return data.get(worldIdentifier).stream()
					   .filter(l -> l.isOnLine((Point) point))
					   .findFirst()
					   .orElse(null);
	}

	@Override
	public LineMarker createLineMarker(String worldIdentifier, List<Point> points) {
		var lineMarker = new LineMarker(points);
		data.get(worldIdentifier).add(lineMarker);
		return lineMarker;
	}

	@Override
	public boolean removeLineMarker(String worldIdentifier, IPoint firstPoint, IPoint lastPoint) {
		return data.get(worldIdentifier).removeIf(
				l -> l.getFirstPoint().equals(firstPoint)
							 && l.getLastPoint().equals(lastPoint)
		);
	}

	@Override
	public boolean removeLineMarker(String worldIdentifier, IPoint point) {
		return data.get(worldIdentifier).removeIf(
				l -> l.getFirstPoint().equals(point)
							 || l.getLastPoint().equals(point)
		);
	}

	public static class Data extends HashMap<String, WorldData> implements IJsonRepositoryData {

		@Override
		public WorldData get(Object key) {
			var world = super.get(key);
			if (world == null) {
				world = new WorldData();
				put((String) key, world);
			}
			return world;
		}

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
			forEach((worldKey, worldData) -> worldData.setContext(worldKey));
		}
	}

	public static class WorldData extends HashSet<LineMarker> {

		public LineMarker get(IPoint lowerPoint, IPoint higherPoint) {
			return stream().filter(l ->
										   l.getFirstPoint().equals(lowerPoint)
												   && l.getLastPoint().equals(higherPoint)
					)
					.findFirst()
					.orElse(null);
		}

		public void setContext(String worldIdentifier) {
			forEach(marker -> marker.setContext(worldIdentifier));
		}

	}

}
