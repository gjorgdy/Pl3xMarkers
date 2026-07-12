package nl.gjorgdy.pl3xmarkers.core.objects;

import nl.gjorgdy.pl3xmarkers.core.interfaces.IBoundary;
import nl.gjorgdy.pl3xmarkers.core.interfaces.entities.IAreaMarker;
import nl.gjorgdy.pl3xmarkers.core.interfaces.entities.IPoint;

import java.util.List;

public record PolygonBoundary(IPoint min, IPoint max, List<? extends IPoint> orderedPoints,
                              IAreaMarker areaMarker) implements IBoundary {

	@Override
	public boolean contains(int x, int z) {
		boolean xInside = x >= min().x() && x <= max().x();
		boolean zInside = z >= min().z() && z <= max().z();
		if (!xInside || !zInside) {
			return false;
		}

		int n = orderedPoints.size();
		boolean inside = false;
		for (int i = 0, j = n - 1; i < n; j = i++) {
			IPoint pi = orderedPoints.get(i);
			IPoint pj = orderedPoints.get(j);
			if (((pi.z() > z) != (pj.z() > z)) &&
					(x < (pj.x() - pi.x()) * (z - pi.z()) / (pj.z() - pi.z()) + pi.x())) {
				inside = !inside;
			}
		}

		return inside;
	}

	@Override
	public double size() {
		int n = orderedPoints.size();
		if (n < 3) {
			return 0.0;
		}

		long sum = 0;

		for (int i = 0; i < n; i++) {
			IPoint current = orderedPoints.get(i);
			IPoint next = orderedPoints.get((i + 1) % n);

			sum += (long) current.x() * next.z();
			sum -= (long) current.z() * next.x();
		}

		return Math.abs(sum) / 2.0;
	}

}
