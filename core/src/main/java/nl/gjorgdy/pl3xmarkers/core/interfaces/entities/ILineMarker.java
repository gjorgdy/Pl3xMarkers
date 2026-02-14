package nl.gjorgdy.pl3xmarkers.core.interfaces.entities;

import java.util.Collection;

public interface ILineMarker<T extends IPoint> {

	String getWorld();

	Collection<T> getPoints();

	IPoint getFirstPoint();

	IPoint getLastPoint();

	boolean isOnLine(T point);

	default String getKey () {
		return getFirstPoint().getKey() + "-" + getLastPoint().getKey();
	}

}
