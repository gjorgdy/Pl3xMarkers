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
	private HashMap<String, SimpleMarkerRepository> simpleMarkerRepositories;
	private HashMap<String, SignMarkerRepository> signMarkerRepositories;
	private HashMap<String, AreaMarkerRepository> areaMarkerRepositories;

	public WorldRepository(JsonStorage storage, String worldIdentifier) {
		this.worldIdentifier = worldIdentifier;
		this.storage = storage;
	}

	public JsonStorage getStorage() {
		return storage;
	}

	@Override
	public IAreaMarkerRepository<? extends IAreaMarker> getAreaMarkerRepository(String layerKey) {
		return areaMarkerRepositories.computeIfAbsent(
				layerKey,
				key ->
				{
					var repo = new AreaMarkerRepository(this, layerKey);
					areaMarkerRepositories.put(layerKey, repo);
					return repo;
				}
		);
	}

	@Override
	public ISimpleMarkerRepository<? extends ISimpleMarker> getSimpleMarkerRepository(String layerKey) {
		return simpleMarkerRepositories.computeIfAbsent(
				layerKey,
				key ->
				{
					var repo = new SimpleMarkerRepository(this, layerKey);
					simpleMarkerRepositories.put(layerKey, repo);
					return repo;
				}
		);
	}

	@Override
	public ISignMarkerRepository<? extends ISignMarker> getSignMarkerRepository(String layerKey) {
		return signMarkerRepositories.computeIfAbsent(
				layerKey,
				key ->
				{
					var repo = new SignMarkerRepository(this, layerKey);
					signMarkerRepositories.put(layerKey, repo);
					return repo;
				}
		);
	}

	public void write() {
		simpleMarkerRepositories.values().forEach(SimpleMarkerRepository::write);
		signMarkerRepositories.values().forEach(SignMarkerRepository::write);
		areaMarkerRepositories.values().forEach(AreaMarkerRepository::write);
	}
}
