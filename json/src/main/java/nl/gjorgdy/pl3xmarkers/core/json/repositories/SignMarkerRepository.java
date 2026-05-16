package nl.gjorgdy.pl3xmarkers.core.json.repositories;

import nl.gjorgdy.pl3xmarkers.core.deprecated.interfaces.IStorage;
import nl.gjorgdy.pl3xmarkers.core.interfaces.ISignMarkerRepository;
import nl.gjorgdy.pl3xmarkers.core.json.entities.Point;
import nl.gjorgdy.pl3xmarkers.core.json.entities.SignMarker;

public class SignMarkerRepository extends MarkerRepository<SignMarker> implements ISignMarkerRepository<SignMarker> {

	public SignMarkerRepository(WorldRepository worldRepository, String layerKey) {
		super(worldRepository, layerKey, SignMarker.class);
	}

	public void migrate(IStorage oldJsonStorage) {
		oldJsonStorage.getSignMarkerRepository()
				.getMarkers(worldIdentifier, layerKey)
				.forEach(oldMarker -> create(oldMarker.getLocation().getX(), Integer.MIN_VALUE,
				                             oldMarker.getLocation().getZ(),
				                             oldMarker.getText()
				));
		write();
	}

	@Override
	public SignMarker create(int x, int y, int z, String[] text) {
		var marker = new SignMarker(this, new Point(x, y, z), text);
		data.add(marker);
		markDirty();
		return marker;
	}

	@Override
	public SignMarker edit(int x, int y, int z, String[] text) {
		var marker = data.stream().filter(m -> m.getPosition().equals(x, y, z)).findFirst().orElse(null);
		if (marker != null) {
			marker.setText(text);
			markDirty();
		}
		return marker;
	}

	@Override
	public SignMarker editOrCreate(int x, int y, int z, String[] text) {
		var marker = edit(x, y, z, text);
		if (marker == null) {
			marker = create(x, y, z, text);
			markDirty();
		} else if (marker.getPosition().y() == Integer.MIN_VALUE) {
			marker.setPosition(new Point(x, y, z));
			markDirty();
		}
		return marker;
	}

	@Override
	public boolean remove(int x, int y, int z) {
		var removed = data.removeIf(marker -> marker.getPosition().equals(x, y, z));
		if (removed) {
			markDirty();
		}
		return removed;
	}
}
