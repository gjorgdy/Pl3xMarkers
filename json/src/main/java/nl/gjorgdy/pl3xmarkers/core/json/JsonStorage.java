package nl.gjorgdy.pl3xmarkers.core.json;

import nl.gjorgdy.pl3xmarkers.core.Pl3xMarkersCore;
import nl.gjorgdy.pl3xmarkers.core.interfaces.IStorage;
import nl.gjorgdy.pl3xmarkers.core.json.repositories.AreaMarkerRepository;
import nl.gjorgdy.pl3xmarkers.core.json.repositories.IconMarkerRepository;

public class JsonStorage implements IStorage {

	private final IconMarkerRepository iconMarkerRepository;
	private final AreaMarkerRepository areaMarkerRepository;

	public JsonStorage() {
		// read files
		String configPath = Pl3xMarkersCore.isBukkit() ? "plugins/Pl3xMarkers" : "config/pl3xmarkers";
		// load repositories
		iconMarkerRepository = new IconMarkerRepository(configPath + "/markers", "icons");
		areaMarkerRepository = new AreaMarkerRepository(configPath + "/markers", "areas");
	}

	@Override
	public AreaMarkerRepository getAreaMarkerRepository() {
		return areaMarkerRepository;
	}

	@Override
	public IconMarkerRepository getIconMarkerRepository() {
		return iconMarkerRepository;
	}

	@Override
	public void close() {
		writeInternal();
	}

	public void write() {
		writeInternal();
	}

	private void writeInternal() {
		// save files
		iconMarkerRepository.write();
		areaMarkerRepository.write();
	}
}
