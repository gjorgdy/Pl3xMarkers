package nl.gjorgdy.pl3xmarkers.json.repositories;

import nl.gjorgdy.pl3xmarkers.core.interfaces.IIconMarkerRepository;
import nl.gjorgdy.pl3xmarkers.json.entities.IconMarker;

import java.util.*;
import java.util.stream.Collectors;

public class IconMarkerRepository extends JsonRepository<IconMarker> implements IIconMarkerRepository<IconMarker> {

	public IconMarkerRepository(String folderPath, String fileName) {
		super(folderPath, fileName, IconMarker[].class);
	}

	@Override
	public Collection<IconMarker> getIconMarkers(String worldIdentifier, String layerKey) {
		return markers.stream()
				   .filter(m -> m.getWorld().equals(worldIdentifier) && m.getLayer().equals(layerKey))
				   .collect(Collectors.toSet());
	}

	@Override
	public IconMarker createIconMarker(String worldIdentifier, String layerKey, int x, int z) {
		var marker = new IconMarker(worldIdentifier, layerKey, x, z);
		var added = markers.add(marker);
		if (added) {
			markDirty();
			write();
		}
		return added ? marker : null;
	}

	@Override
	public boolean removeIconMarker(String worldIdentifier, String layerKey, int x, int z) {
		var removed = markers.removeIf(
				m ->
					m.getWorld().equals(worldIdentifier)
					&& m.getLayer().equals(layerKey)
					&& m.getLocation().getX() == x
					&& m.getLocation().getZ() == z
		);
		if (removed) {
			markDirty();
			write();
		}
		return removed;
	}
}
