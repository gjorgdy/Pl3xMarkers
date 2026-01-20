package nl.gjorgdy.pl3xmarkers.core.interfaces;

import nl.gjorgdy.pl3xmarkers.core.interfaces.entities.IAreaMarker;
import nl.gjorgdy.pl3xmarkers.core.interfaces.entities.IIconMarker;
import nl.gjorgdy.pl3xmarkers.core.interfaces.entities.INamedIconMarker;

public interface IStorage {

	IAreaMarkerRepository<? extends IAreaMarker> getAreaMarkerRepository();

	IIconMarkerRepository<? extends IIconMarker> getIconMarkerRepository();

	INamedIconMarkerRepository<? extends INamedIconMarker> getNamedIconMarkerRepository();

	void close();

}
