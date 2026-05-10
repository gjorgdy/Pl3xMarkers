package nl.gjorgdy.pl3xmarkers.core.json;

import nl.gjorgdy.pl3xmarkers.core.Pl3xMarkersCore;
import nl.gjorgdy.pl3xmarkers.core.interfaces.IStorage;
import nl.gjorgdy.pl3xmarkers.core.interfaces.IWorldRepository;
import nl.gjorgdy.pl3xmarkers.core.json.repositories.WorldRepository;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.HashMap;

public class JsonStorage implements IStorage {

	private final HashMap<String, WorldRepository> worldRepositories = new HashMap<>();
	private String configPath;
	private boolean loaded = false;

	private void load() {
		// read files
		configPath = Pl3xMarkersCore.isBukkit() ? "plugins/Pl3xMarkers" : "config/pl3xmarkers";
		// migrate old data if needed
		if (Pl3xMarkersCore.isBukkit()) {
			migrate(Path.of("config/pl3xmarkers"), Path.of("plugins/Pl3xMarkers"));
		}
		// mark as loaded
		loaded = true;
	}

	public String getConfigPath() {
		if (!loaded) {
			load();
		}
		return configPath;
	}

	@Override
	public IWorldRepository getWorldRepository(String worldIdentifier) {
		if (worldRepositories.containsKey(worldIdentifier)) {
			return worldRepositories.get(worldIdentifier);
		}
		var repo = new WorldRepository(this, worldIdentifier);
		worldRepositories.put(worldIdentifier, repo);
		return repo;
	}

	@Override
	public void close() {
		writeInternal();
	}

	public void write() {
		writeInternal();
	}

	private void writeInternal() {
		if (!loaded) {
			load();
		}
		// save files
		worldRepositories.forEach((k, repo) -> repo.write());
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
