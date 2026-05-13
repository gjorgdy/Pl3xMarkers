package nl.gjorgdy.pl3xmarkers.core.json.repositories;

import nl.gjorgdy.pl3xmarkers.core.deprecated.interfaces.IStorage;
import nl.gjorgdy.pl3xmarkers.core.interfaces.ISimpleMarkerRepository;
import nl.gjorgdy.pl3xmarkers.core.json.entities.Point;
import nl.gjorgdy.pl3xmarkers.core.json.entities.SimpleMarker;

public class SimpleMarkerRepository extends MarkerRepository<SimpleMarker> implements ISimpleMarkerRepository<SimpleMarker> {

	public SimpleMarkerRepository(WorldRepository worldRepository, String layerKey) {
		super(worldRepository, layerKey, SimpleMarker.class);
	}

	public void migrate(IStorage oldJsonStorage) {
		oldJsonStorage.getIconMarkerRepository()
				.getIconMarkers(worldIdentifier, layerKey)
				.forEach(oldMarker -> create(oldMarker.getLocation().getX(), Integer.MIN_VALUE,
				                             oldMarker.getLocation().getZ()
				));
	}

	@Override
	public SimpleMarker create(int x, int y, int z) {
		var marker = new SimpleMarker(this, new Point(x, y, z));
		data.add(marker);
		markDirty();
		return marker;
	}

	@Override
	public SimpleMarker get(int x, int y, int z) {
		return data.stream().filter(marker -> marker.getPosition().equals(x, y, z)).findFirst().orElse(null);
	}

	@Override
	public SimpleMarker getOrCreate(int x, int y, int z) {
		var marker = get(x, y, z);
		if (marker == null) {
			marker = create(x, y, z);
		} else if (marker.getPosition().y() == Integer.MIN_VALUE) {
			marker.setPosition(new Point(x, y, z));
			markDirty();
		}
		return marker;
	}

	@Override
	public boolean remove(int x, int y, int z) {
		boolean removed = data.removeIf(marker -> marker.getPosition().equals(new Point(x, y, z)));
		if (removed) {
			markDirty();
		}
		return removed;
	}
}
