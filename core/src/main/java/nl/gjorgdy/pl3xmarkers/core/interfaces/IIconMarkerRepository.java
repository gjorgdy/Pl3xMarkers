package nl.gjorgdy.pl3xmarkers.core.interfaces;

import nl.gjorgdy.pl3xmarkers.core.interfaces.entities.IIconMarker;

import java.util.Collection;

public interface IIconMarkerRepository<T extends IIconMarker> {

	Collection<T> getIconMarkers(String worldIdentifier, String layerKey);

	T createIconMarker(String worldIdentifier, String layerKey, int x, int z);

	boolean removeIconMarker(String worldIdentifier, String layerKey, int x, int z);

}
