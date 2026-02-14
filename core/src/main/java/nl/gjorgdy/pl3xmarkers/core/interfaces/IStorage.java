package nl.gjorgdy.pl3xmarkers.core.interfaces;

import nl.gjorgdy.pl3xmarkers.core.interfaces.entities.*;

public interface IStorage {

	ILineMarkerRepository<? extends ILineMarker<?>, ? extends IPoint> getRaillineMarkerRepository();

	IAreaMarkerRepository<? extends IAreaMarker> getAreaMarkerRepository();

	IIconMarkerRepository<? extends IIconMarker> getIconMarkerRepository();

	ISignMarkerRepository<? extends ISignMarker> getSignMarkerRepository();

	void close();

}
