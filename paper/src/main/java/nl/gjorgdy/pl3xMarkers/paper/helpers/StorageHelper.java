package nl.gjorgdy.pl3xMarkers.paper.helpers;

import nl.gjorgdy.pl3xmarkers.core.json.entities.Point;
import org.bukkit.Location;

import java.util.List;

public abstract class StorageHelper {

	public static List<Point> toJsonStoragePoints(List<Location> locations) {
		return locations.stream().map(StorageHelper::toJsonStoragePoint).toList();
	}

	private static Point toJsonStoragePoint(Location location) {
		return new Point(
				(int) location.getX(),
				(int) location.getZ()
		);
	}

}
