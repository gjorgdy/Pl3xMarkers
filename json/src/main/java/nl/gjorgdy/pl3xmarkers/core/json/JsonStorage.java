package nl.gjorgdy.pl3xmarkers.core.json;

import nl.gjorgdy.pl3xmarkers.core.Pl3xMarkersCore;
import nl.gjorgdy.pl3xmarkers.core.interfaces.IStorage;
import nl.gjorgdy.pl3xmarkers.core.interfaces.IWorldRepository;
import nl.gjorgdy.pl3xmarkers.core.json.repositories.WorldRepository;
import nl.gjorgdy.pl3xmarkers.core.json_old.OldJsonStorage;

import java.util.HashMap;

public class JsonStorage implements IStorage {

	private final HashMap<String, WorldRepository> worldRepositories = new HashMap<>();
	private String configPath;
	private boolean loaded = false;

	private OldJsonStorage oldJsonStorage;

	private void load() {
		// read files
		configPath = Pl3xMarkersCore.isBukkit() ? "plugins/Pl3xMarkers" : "config/pl3xmarkers";
		// mark as loaded
		loaded = true;
	}

	public OldJsonStorage oldJsonStorage() {
		if (oldJsonStorage == null) {
			oldJsonStorage = new OldJsonStorage();
		}
		return oldJsonStorage;
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
		write();
	}

	public void write() {
		if (!loaded) {
			load();
		}
		// save files
		worldRepositories.forEach((k, repo) -> repo.write());
	}

	public void migrate(nl.gjorgdy.pl3xmarkers.core.deprecated.interfaces.IStorage oldJsonStorage) {
		worldRepositories.values().forEach(repo -> repo.migrate(oldJsonStorage));
		write();
	}
}
