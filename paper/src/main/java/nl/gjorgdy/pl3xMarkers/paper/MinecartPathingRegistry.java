package nl.gjorgdy.pl3xMarkers.paper;

import org.bukkit.Location;
import org.jspecify.annotations.Nullable;

import java.util.LinkedList;
import java.util.Map;
import java.util.UUID;

public class MinecartPathingRegistry {

	private final Map<UUID, LinkedList<Location>> minecartPaths = new java.util.HashMap<>();

	public void pathPoint(UUID minecartId, Location location) {
		var path = minecartPaths.computeIfAbsent(minecartId, k -> new LinkedList<>());
		if (path.isEmpty() || !path.getLast().equals(location)) {
			path.add(location);
		}
	}

	@Nullable
	public LinkedList<Location> endPath(UUID minecartId) {
		return minecartPaths.remove(minecartId);
	}
}
