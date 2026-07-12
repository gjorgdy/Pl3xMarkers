package nl.gjorgdy.pl3xmarkers.core.objects;

import nl.gjorgdy.pl3xmarkers.core.interfaces.IBoundary;
import nl.gjorgdy.pl3xmarkers.core.interfaces.entities.IAreaMarker;
import nl.gjorgdy.pl3xmarkers.core.interfaces.entities.IPoint;

public record CircleBoundary(IPoint center, int radius, IAreaMarker areaMarker) implements IBoundary {

	@Override
	public boolean contains(int x, int z) {
		return center.distance(x, z) <= radius;
	}

	@Override
	public double size() {
		return Math.PI * radius * radius;
	}

}
