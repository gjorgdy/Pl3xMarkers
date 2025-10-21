package nl.gjorgdy.pl3xmarkers.json;

import nl.gjorgdy.pl3xmarkers.core.Pl3xMarkersCore;
import nl.gjorgdy.pl3xmarkers.core.interfaces.IAreaMarkerRepository;
import nl.gjorgdy.pl3xmarkers.core.interfaces.IIconMarkerRepository;
import nl.gjorgdy.pl3xmarkers.core.interfaces.IStorage;
import nl.gjorgdy.pl3xmarkers.core.interfaces.entities.IIconMarker;
import nl.gjorgdy.pl3xmarkers.json.repositories.AreaMarkerRepository;
import nl.gjorgdy.pl3xmarkers.json.repositories.IconMarkerRepository;

public class FileStorage implements IStorage {

	private final IIconMarkerRepository<? extends IIconMarker> iconMarkerRepository;
	private final IAreaMarkerRepository areaMarkerRepository;

	public FileStorage() {
		// read files
		String configPath = Pl3xMarkersCore.isBukkit() ? "plugins/Pl3xMarkers" : "config/pl3xmarkers";
		// load repositories
		iconMarkerRepository = new IconMarkerRepository(configPath + "/markers", "icons");
		areaMarkerRepository = new AreaMarkerRepository(configPath + "/markers", "areas");
	}

	@Override
	public IAreaMarkerRepository getAreaMarkerRepository() {
		return areaMarkerRepository;
	}

	@Override
	public IIconMarkerRepository<? extends IIconMarker> getIconMarkerRepository() {
		return iconMarkerRepository;
	}

	@Override
	public void close() {

	}
}
