package nl.gjorgdy.pl3xmarkers.core.interfaces;

import nl.gjorgdy.pl3xmarkers.core.interfaces.entities.IIconMarker;

public interface IStorage {

	IAreaMarkerRepository getAreaMarkerRepository();
	IIconMarkerRepository<? extends IIconMarker> getIconMarkerRepository();
	void close();

}
