package nl.gjorgdy.pl3xmarkers.core.interfaces.entities;

import org.jspecify.annotations.Nullable;

import java.util.Collection;
import java.util.Locale;

public interface IAreaMarker {

	String getWorld();

	String getName();

	int getColor();

	Collection<? extends IPoint> getPoints();

	@Nullable
	IPoint getMinCorner();

	@Nullable
	IPoint getMaxCorner();

	boolean addPoint(int x, int z);

	boolean removePoint(int x, int z);

	default String getKey () {
		return getName().toLowerCase(Locale.ROOT).trim().replaceAll(" ", "_");
	}

}
