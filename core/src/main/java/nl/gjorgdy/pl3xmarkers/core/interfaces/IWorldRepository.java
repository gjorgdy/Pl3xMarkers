package nl.gjorgdy.pl3xmarkers.core.interfaces;

import nl.gjorgdy.pl3xmarkers.core.interfaces.entities.IAreaMarker;
import nl.gjorgdy.pl3xmarkers.core.interfaces.entities.ISignMarker;
import nl.gjorgdy.pl3xmarkers.core.interfaces.entities.ISimpleMarker;

public interface IWorldRepository {

	IAreaMarkerRepository<? extends IAreaMarker> getAreaMarkerRepository(String layerKey);

	ISimpleMarkerRepository<? extends ISimpleMarker> getSimpleMarkerRepository(String layerKey);

	ISignMarkerRepository<? extends ISignMarker> getSignMarkerRepository(String layerKey);

}