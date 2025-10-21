package nl.gjorgdy.pl3xmarkers.core.interfaces;

import nl.gjorgdy.pl3xmarkers.core.interfaces.entities.IAreaMarker;

import java.util.List;

public interface IAreaMarkerRepository {

	List<? extends IAreaMarker> getAreas(String worldIdentifier);

	IAreaMarker getArea(String worldIdentifier, String name, int color);

	IAreaMarker getOrCreateArea(String worldIdentifier, String name, int color);

	boolean removeArea(String worldIdentifier, String name, int color);

}
