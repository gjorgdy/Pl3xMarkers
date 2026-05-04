package nl.gjorgdy.pl3xmarkers.core.json.repositories;

import nl.gjorgdy.pl3xmarkers.core.interfaces.ISignMarkerRepository;
import nl.gjorgdy.pl3xmarkers.core.json.entities.Point;
import nl.gjorgdy.pl3xmarkers.core.json.entities.SignMarker;

public class SignMarkerRepository extends MarkerRepository<SignMarker> implements ISignMarkerRepository<SignMarker> {

	public SignMarkerRepository(WorldRepository worldRepository, String layerKey) {
		super(worldRepository, layerKey, SignMarker.class);
	}

	@Override
	public SignMarker create(int x, int y, int z, String[] text) {
		var marker = new SignMarker(this, new Point(x, y, z), text);
		data.add(marker);
		markDirty();
		return marker;
	}

	@Override
	public boolean edit(int x, int y, int z, String[] text) {
		var marker = data.stream().filter(m -> m.getPosition().equals(new Point(x, y, z))).findFirst().orElse(null);
		if (marker != null) {
			marker.setText(text);
			markDirty();
			return true;
		}
		return false;
	}

	@Override
	public boolean editOrCreate(int x, int y, int z, String[] text) {
		boolean edited = edit(x, y, z, text);
		if (!edited) {
			create(x, y, z, text);
			markDirty();
			return false;
		}
		return true;
	}

	@Override
	public boolean remove(int x, int y, int z) {
		return false;
	}
}
