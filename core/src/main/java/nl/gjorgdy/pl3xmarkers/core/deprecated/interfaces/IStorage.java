package nl.gjorgdy.pl3xmarkers.core.deprecated.interfaces;

import nl.gjorgdy.pl3xmarkers.core.deprecated.interfaces.entities.IAreaMarker;
import nl.gjorgdy.pl3xmarkers.core.deprecated.interfaces.entities.IIconMarker;
import nl.gjorgdy.pl3xmarkers.core.deprecated.interfaces.entities.ISignMarker;

public interface IStorage {

	IAreaMarkerRepository<? extends IAreaMarker> getAreaMarkerRepository();

	IIconMarkerRepository<? extends IIconMarker> getIconMarkerRepository();

	ISignMarkerRepository<? extends ISignMarker> getSignMarkerRepository();

	void close();

}
