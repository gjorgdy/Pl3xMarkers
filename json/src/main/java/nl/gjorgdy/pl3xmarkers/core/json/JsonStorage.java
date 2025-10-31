package nl.gjorgdy.pl3xmarkers.core.json;

import nl.gjorgdy.pl3xmarkers.core.Pl3xMarkersCore;
import nl.gjorgdy.pl3xmarkers.core.interfaces.IStorage;
import nl.gjorgdy.pl3xmarkers.core.json.repositories.AreaMarkerRepository;
import nl.gjorgdy.pl3xmarkers.core.json.repositories.IconMarkerRepository;

public class JsonStorage implements IStorage {

	private  IconMarkerRepository iconMarkerRepository;
	private AreaMarkerRepository areaMarkerRepository;
	private boolean loaded = false;

	private void load() {
		// read files
		String configPath = Pl3xMarkersCore.isBukkit() ? "plugins/Pl3xMarkers" : "config/pl3xmarkers";
		// load repositories
		iconMarkerRepository = new IconMarkerRepository(configPath + "/markers", "icons");
		areaMarkerRepository = new AreaMarkerRepository(configPath + "/markers", "areas");
		// mark as loaded
		loaded = true;
	}

	@Override
	public AreaMarkerRepository getAreaMarkerRepository() {
		if (!loaded) load();
		return areaMarkerRepository;
	}

	@Override
	public IconMarkerRepository getIconMarkerRepository() {
		if (!loaded) load();
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
		if (!loaded) load();
		// save files
		iconMarkerRepository.write();
		areaMarkerRepository.write();
	}
}
