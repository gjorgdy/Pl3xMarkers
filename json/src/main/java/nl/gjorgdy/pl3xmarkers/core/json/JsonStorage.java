package nl.gjorgdy.pl3xmarkers.core.json;

import nl.gjorgdy.pl3xmarkers.core.Pl3xMarkersCore;
import nl.gjorgdy.pl3xmarkers.core.interfaces.IStorage;
import nl.gjorgdy.pl3xmarkers.core.json.repositories.AreaMarkerRepository;
import nl.gjorgdy.pl3xmarkers.core.json.repositories.IconMarkerRepository;
import nl.gjorgdy.pl3xmarkers.core.json.repositories.NamedIconMarkerRepository;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

public class JsonStorage implements IStorage {

	private IconMarkerRepository iconMarkerRepository;
	private NamedIconMarkerRepository namedIconMarkerRepository;
	private AreaMarkerRepository areaMarkerRepository;
	private boolean loaded = false;

	private void load() {
		// read files
		String configPath = Pl3xMarkersCore.isBukkit() ? "plugins/Pl3xMarkers" : "config/pl3xmarkers";
		// migrate old data if needed
		if (Pl3xMarkersCore.isBukkit()) migrate(Path.of("config/pl3xmarkers"), Path.of("plugins/Pl3xMarkers"));
		// load repositories
		iconMarkerRepository = new IconMarkerRepository(configPath + "/markers", "icons");
		namedIconMarkerRepository = new NamedIconMarkerRepository(configPath + "/markers", "named_icons");
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
	public NamedIconMarkerRepository getNamedIconMarkerRepository() {
		if (!loaded) load();
		return namedIconMarkerRepository;
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

	private void migrate(Path oldPath, Path newPath) {
		try (var files = Files.list(oldPath)) {
			if (!Files.exists(newPath)) {
				Files.createDirectories(newPath);
			}
			files.forEach(oldFile -> {
				try {
					Files.move(oldFile, newPath.resolve(oldFile.getFileName()));
				} catch (Exception ignored) {
				}
			});
			Files.delete(oldPath);
		} catch (IOException ignored) {
		}
	}
}
