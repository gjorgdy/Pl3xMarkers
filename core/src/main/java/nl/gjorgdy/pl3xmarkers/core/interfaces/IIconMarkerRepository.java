package nl.gjorgdy.pl3xmarkers.core.interfaces;

import nl.gjorgdy.pl3xmarkers.core.interfaces.entities.IIconMarker;

import java.util.Collection;

public interface IIconMarkerRepository<T extends IIconMarker> {

	@SuppressWarnings("Unused")
	Collection<T> getIconMarkers(String worldIdentifier, String layerKey);

	@SuppressWarnings("Unused")
	T createIconMarker(String worldIdentifier, String layerKey, int x, int z);

	@SuppressWarnings("Unused")
	boolean removeIconMarker(String worldIdentifier, String layerKey, int x, int z);

}
