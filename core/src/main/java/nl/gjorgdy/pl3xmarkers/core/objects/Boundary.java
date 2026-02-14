package nl.gjorgdy.pl3xmarkers.core.objects;

import nl.gjorgdy.pl3xmarkers.core.interfaces.entities.IAreaMarker;
import nl.gjorgdy.pl3xmarkers.core.interfaces.entities.IPoint;

import java.util.List;

public record Boundary(IPoint min, IPoint max, List<? extends IPoint> orderedPoints, IAreaMarker areaMarker) {

	public boolean contains(int x, int z) {
		boolean xInside = x >= min().getX() && x <= max().getX();
		boolean zInside = z >= min().getZ() && z <= max().getZ();
		if (!xInside || !zInside) {
			return false;
		}

		int n = orderedPoints.size();
		boolean inside = false;
		for (int i = 0, j = n - 1; i < n; j = i++) {
			IPoint pi = orderedPoints.get(i);
			IPoint pj = orderedPoints.get(j);
			if (((pi.getZ() > z) != (pj.getZ() > z)) &&
				(x < (pj.getX() - pi.getX()) * (z - pi.getZ()) / (pj.getZ() - pi.getZ()) + pi.getX())) {
				inside = !inside;
			}
		}

		return inside;
	}

}
