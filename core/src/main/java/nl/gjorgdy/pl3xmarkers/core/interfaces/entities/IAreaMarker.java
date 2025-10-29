package nl.gjorgdy.pl3xmarkers.core.interfaces.entities;

import java.util.Collection;
import java.util.Locale;

public interface IAreaMarker {

	String getWorld();

	String getName();

	int getColor();

	Collection<? extends IPoint> getPoints();

	boolean addPoint(int x, int z);

	boolean removePoint(int x, int z);

	default String getKey () {
		return getName().toLowerCase(Locale.ROOT).trim().replaceAll(" ", "_");
	}

}
