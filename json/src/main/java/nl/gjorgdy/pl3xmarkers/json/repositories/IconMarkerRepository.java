package nl.gjorgdy.pl3xmarkers.json.repositories;

import nl.gjorgdy.pl3xmarkers.core.interfaces.IIconMarkerRepository;
import nl.gjorgdy.pl3xmarkers.json.entities.IconMarker;

import java.util.*;
import java.util.stream.Collectors;

public class IconMarkerRepository extends JsonRepository<IconMarker> implements IIconMarkerRepository<IconMarker> {

	private final Set<IconMarker> iconMarkers;

	public IconMarkerRepository(String filePath) {
		super(filePath, IconMarker[].class);
		this.iconMarkers = new HashSet<>();
		iconMarkers.add(new IconMarker("minecraft:overworld", "beacons", 10, 10));
		iconMarkers.add(new IconMarker("minecraft:overworld", "nether_portals", 49, -59));
	}

	@Override
	public Collection<IconMarker> getIconMarkers(String worldIdentifier, String layerKey) {
		return iconMarkers.stream()
				   .filter(m -> m.getWorldIdentifier().equals(worldIdentifier) && m.getLayerKey().equals(layerKey))
				   .collect(Collectors.toSet());
	}

	@Override
	public IconMarker createIconMarker(String worldIdentifier, String layerKey, int x, int z) {
		var marker = new IconMarker(worldIdentifier, layerKey, x, z);
		var added = iconMarkers.add(marker);
		if (added) {
			write();
		}
		return added ? marker : null;
	}

	@Override
	public boolean removeIconMarker(String worldIdentifier, String layerKey, int x, int z) {
		var removed = iconMarkers.removeIf(
				m ->
					m.getWorldIdentifier().equals(worldIdentifier)
					&& m.getLayerKey().equals(layerKey)
					&& m.getLocation().getX() == x
					&& m.getLocation().getZ() == z
		);
		if (removed) {
			write();
		}
		return removed;
	}
}
