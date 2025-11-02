package nl.gjorgdy.pl3xmarkers.core.interfaces;

import nl.gjorgdy.pl3xmarkers.core.interfaces.entities.IAreaMarker;
import nl.gjorgdy.pl3xmarkers.core.interfaces.entities.IIconMarker;
import nl.gjorgdy.pl3xmarkers.core.interfaces.entities.ILineMarker;

public interface IStorage {

	ILineMarkerRepository<? extends ILineMarker> getLineMarkerRepository();

	IAreaMarkerRepository<? extends IAreaMarker> getAreaMarkerRepository();

	IIconMarkerRepository<? extends IIconMarker> getIconMarkerRepository();

	void close();

}
