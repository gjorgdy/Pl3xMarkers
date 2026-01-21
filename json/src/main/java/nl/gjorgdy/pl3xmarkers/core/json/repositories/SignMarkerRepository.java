package nl.gjorgdy.pl3xmarkers.core.json.repositories;

import com.google.gson.reflect.TypeToken;
import nl.gjorgdy.pl3xmarkers.core.interfaces.ISignMarkerRepository;
import nl.gjorgdy.pl3xmarkers.core.json.entities.SignMarker;

import java.util.*;

public class SignMarkerRepository extends BaseIconMarkerRepository<SignMarker> implements ISignMarkerRepository<SignMarker> {

	public SignMarkerRepository(String folderPath, String fileName) {
		super(folderPath, fileName, TypeToken.getParameterized(Data.class, SignMarker.class));
	}

	@Override
	public Collection<SignMarker> getMarkers(String worldIdentifier, String layerKey) {
		var world = data.get(worldIdentifier);
		if (world == null) return Set.of();
		var layer = world.get(layerKey);
		return layer != null ? layer : Set.of();
	}

	@Override
	public boolean editMarker(String worldIdentifier, String layerKey, int x, int z, String[] text) {
		var marker = getMarker(worldIdentifier, layerKey, x, z);
		if (marker == null) {
			createMarker(worldIdentifier, layerKey, x, z, text);
		}
		else {
			marker.setText(text);
			markDirty();
		}
		return true;
	}

	@Override
	public SignMarker createMarker(String worldIdentifier, String layerKey, int x, int z, String[] text) {
		var marker = new SignMarker(worldIdentifier, layerKey, x, z, text);
		var added = data.add(marker);
		if (added) markDirty();
		return added ? marker : null;
	}

	@Override
	public boolean removeMarker(String worldIdentifier, String layerKey, int x, int z) {
		return data.remove(worldIdentifier, layerKey, x, z);
	}

	private SignMarker getMarker(String worldIdentifier, String layerKey, int x, int z) {
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
