package nl.gjorgdy.pl3xmarkers.core.json.repositories;

import nl.gjorgdy.pl3xmarkers.core.interfaces.IAreaMarkerRepository;
import nl.gjorgdy.pl3xmarkers.core.interfaces.ISignMarkerRepository;
import nl.gjorgdy.pl3xmarkers.core.interfaces.ISimpleMarkerRepository;
import nl.gjorgdy.pl3xmarkers.core.interfaces.IWorldRepository;
import nl.gjorgdy.pl3xmarkers.core.interfaces.entities.IAreaMarker;
import nl.gjorgdy.pl3xmarkers.core.interfaces.entities.ISignMarker;
import nl.gjorgdy.pl3xmarkers.core.interfaces.entities.ISimpleMarker;
import nl.gjorgdy.pl3xmarkers.core.json.JsonStorage;

import java.util.HashMap;

public class WorldRepository implements IWorldRepository {

	public final String worldIdentifier;
	private final JsonStorage storage;
	private final HashMap<String, SimpleMarkerRepository> simpleMarkerRepositories;
	private final HashMap<String, SignMarkerRepository> signMarkerRepositories;
	private final HashMap<String, AreaMarkerRepository> areaMarkerRepositories;

	public WorldRepository(JsonStorage storage, String worldIdentifier) {
		this.worldIdentifier = worldIdentifier;
		this.storage = storage;

		simpleMarkerRepositories = new HashMap<>();
		signMarkerRepositories = new HashMap<>();
		areaMarkerRepositories = new HashMap<>();
	}

	public JsonStorage getStorage() {
		return storage;
	}

	@Override
	public IAreaMarkerRepository<? extends IAreaMarker> getAreaMarkerRepository(String layerKey) {
		if (areaMarkerRepositories.containsKey(layerKey)) {
			return areaMarkerRepositories.get(layerKey);
		}
		var repo = new AreaMarkerRepository(this, layerKey);
		areaMarkerRepositories.put(layerKey, repo);
		return repo;
	}

	@Override
	public ISimpleMarkerRepository<? extends ISimpleMarker> getSimpleMarkerRepository(String layerKey) {
		if (simpleMarkerRepositories.containsKey(layerKey)) {
			return simpleMarkerRepositories.get(layerKey);
		}
		var repo = new SimpleMarkerRepository(this, layerKey);
		simpleMarkerRepositories.put(layerKey, repo);
		return repo;
	}

	@Override
	public ISignMarkerRepository<? extends ISignMarker> getSignMarkerRepository(String layerKey) {
		if (signMarkerRepositories.containsKey(layerKey)) {
			return signMarkerRepositories.get(layerKey);
		}
		var repo = new SignMarkerRepository(this, layerKey);
		signMarkerRepositories.put(layerKey, repo);
		return repo;
	}

	public void write() {
		simpleMarkerRepositories.values().forEach(MarkerRepository::write);
		signMarkerRepositories.values().forEach(MarkerRepository::write);
		areaMarkerRepositories.values().forEach(MarkerRepository::write);
	}
}
