package nl.gjorgdy.pl3xmarkers.core.interfaces;

import nl.gjorgdy.pl3xmarkers.core.interfaces.entities.IAreaMarker;

import java.util.List;

public interface IAreaMarkerRepository<T extends IAreaMarker> {

	@SuppressWarnings("Unused")
	List<T> getAreas(String worldIdentifier);

	@SuppressWarnings("Unused")
	T getArea(String worldIdentifier, String name, int color);

	@SuppressWarnings("Unused")
	T getOrCreateArea(String worldIdentifier, String name, int color);

	@SuppressWarnings("Unused")
	boolean removeArea(String worldIdentifier, String name, int color);

}
