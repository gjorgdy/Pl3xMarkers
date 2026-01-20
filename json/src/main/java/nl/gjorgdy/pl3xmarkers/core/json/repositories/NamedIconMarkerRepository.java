package nl.gjorgdy.pl3xmarkers.core.json.repositories;

import com.google.gson.reflect.TypeToken;
import nl.gjorgdy.pl3xmarkers.core.interfaces.INamedIconMarkerRepository;
import nl.gjorgdy.pl3xmarkers.core.json.entities.NamedIconMarker;

import java.util.*;

public class NamedIconMarkerRepository extends BaseIconMarkerRepository<NamedIconMarker> implements INamedIconMarkerRepository<NamedIconMarker> {

	public NamedIconMarkerRepository(String folderPath, String fileName) {
		super(folderPath, fileName, TypeToken.getParameterized(Data.class, NamedIconMarker.class));
	}

	@Override
	public Collection<NamedIconMarker> getNamedIconMarkers(String worldIdentifier, String layerKey) {
		var world = data.get(worldIdentifier);
		if (world == null) return Set.of();
		var layer = world.get(layerKey);
		return layer != null ? layer : Set.of();
	}

	@Override
	public boolean renameIconMarker(String worldIdentifier, String layerKey, int x, int z, String newName) {
		var marker = getNamedIconMarker(worldIdentifier, layerKey, x, z);
		if (marker == null) {
			createNamedIconMarker(worldIdentifier, layerKey, x, z, newName);
		}
		else {
			marker.setName(newName);
			markDirty();
		}
		return true;
	}

	@Override
	public NamedIconMarker createNamedIconMarker(String worldIdentifier, String layerKey, int x, int z, String name) {
		var marker = new NamedIconMarker(worldIdentifier, layerKey, x, z, name);
		var added = data.add(marker);
		if (added) markDirty();
		return added ? marker : null;
	}

	@Override
	public boolean removeNamedIconMarker(String worldIdentifier, String layerKey, int x, int z) {
		return data.remove(worldIdentifier, layerKey, x, z);
	}

	private NamedIconMarker getNamedIconMarker(String worldIdentifier, String layerKey, int x, int z) {
		var world = data.get(worldIdentifier);
		if (world == null) return null;
		var layer = world.get(layerKey);
		if (layer == null) return null;
		for (var marker : layer) {
			if (marker.getX() == x && marker.getZ() == z) {
				return marker;
			}
		}
		return null;
	}

}
