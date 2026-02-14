package nl.gjorgdy.pl3xmarkers.core.helpers;

import nl.gjorgdy.pl3xmarkers.core.interfaces.entities.IPoint;

import java.util.List;

public abstract class PolygonArea {

	/**
	 * Calculate the area of a polygon given its corners
	 * @param points the corners of the polygon
	 * @return the area of the polygon in square blocks
	 */
	public static double calculate(List<IPoint> points) {
		int n = points.size();
		if (n < 3) {
			return 0.0;
		}

		long sum = 0;

		for (int i = 0; i < n; i++) {
			IPoint current = points.get(i);
			IPoint next = points.get((i + 1) % n);

			sum += (long) current.getX() * next.getZ();
			sum -= (long) current.getZ() * next.getX();
		}

		return Math.abs(sum) / 2.0;
	}

}
