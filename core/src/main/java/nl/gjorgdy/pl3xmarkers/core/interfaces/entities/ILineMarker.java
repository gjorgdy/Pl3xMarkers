package nl.gjorgdy.pl3xmarkers.core.interfaces.entities;

import java.util.Collection;

public interface ILineMarker {

	String getWorld();

	String getName();

	void setName(String name);

	int getColor();

	void setColor(int color);

	Collection<IPoint> getPoints();

	IPoint getLowerPoint();

	IPoint getUpperPoint();

	default String getKey () {
		return getLowerPoint().getKey() + "-" + getUpperPoint().getKey();
	}

}
