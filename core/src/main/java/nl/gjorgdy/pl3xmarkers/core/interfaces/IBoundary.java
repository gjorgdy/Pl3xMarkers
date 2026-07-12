package nl.gjorgdy.pl3xmarkers.core.interfaces;

import nl.gjorgdy.pl3xmarkers.core.interfaces.entities.IAreaMarker;

public interface IBoundary {

	boolean contains(int x, int z);

	IAreaMarker areaMarker();

	double size();

}
