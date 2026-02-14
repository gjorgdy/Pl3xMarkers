package nl.gjorgdy.pl3xmarkers.core.json.repositories;

import com.google.gson.reflect.TypeToken;
import nl.gjorgdy.pl3xmarkers.core.interfaces.IIconMarkerRepository;
import nl.gjorgdy.pl3xmarkers.core.json.entities.IconMarker;

import java.util.*;

public class IconMarkerRepository extends BaseIconMarkerRepository<IconMarker> implements IIconMarkerRepository<IconMarker> {

	public IconMarkerRepository(String folderPath, String fileName) {
		super(folderPath, fileName, TypeToken.getParameterized(Data.class, IconMarker.class));
	}

	@Override
	public Collection<IconMarker> getIconMarkers(String worldIdentifier, String layerKey) {
		var world = data.get(worldIdentifier);
		if (world == null) return Set.of();
		var layer = world.get(layerKey);
		return layer != null ? layer : Set.of();
	}

	@Override
	public IconMarker createIconMarker(String worldIdentifier, String layerKey, int x, int z) {
		var marker = new IconMarker(worldIdentifier, layerKey, x, z);
		var added = data.add(marker);
		if (added) markDirty();
		return added ? marker : null;
	}

	@Override
	public boolean removeIconMarker(String worldIdentifier, String layerKey, int x, int z) {
		return data.remove(worldIdentifier, layerKey, x, z);
	}

}
