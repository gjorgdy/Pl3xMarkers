package nl.gjorgdy.pl3xmarkers.core.json;

import nl.gjorgdy.pl3xmarkers.core.interfaces.IStorage;
import nl.gjorgdy.pl3xmarkers.core.interfaces.IWorldRepository;
import nl.gjorgdy.pl3xmarkers.core.json.repositories.WorldRepository;

import java.util.HashMap;

public class JsonStorage implements IStorage {

	private final HashMap<String, WorldRepository> worldRepositories = new HashMap<>();
	private final String configPath;

	public JsonStorage(String configPath) {
		this.configPath = configPath;
	}

	public String getConfigPath() {
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
		worldRepositories.forEach((k, repo) -> repo.write());
	}
}
