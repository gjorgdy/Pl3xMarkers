package nl.gjorgdy.pl3xmarkers.core.helpers;

import nl.gjorgdy.pl3xmarkers.core.interfaces.entities.IPoint;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public class ConvexHull {

    private ConvexHull() {}

    private static final int maxSize = 512;
	private static final int maxRadius = maxSize / 2;

    /**
     * Perform a convex hull calculation to only use the outer hull of points for the area
     * @param points input list of points to use
     * @return a line of external points
     */
    public static List<IPoint> calculate(List<IPoint> points) {
        if (points.isEmpty()) return points;
		else if (points.size() < 3) return minimalArea(points.getFirst());

        // Sort points
        points.sort(null);
		// Cluster points if necessary
		if (points.getFirst().distance(points.getLast()) > maxSize) {
			points = cluster(points);
		}

		return points.size() < 3 ?  minimalArea(points.getFirst()) : calculateInternal(points);
	}

	private static List<IPoint> cluster(List<IPoint> points) {
		// Calculate center point
		int centerX = points.stream().map(IPoint::getX).reduce(0, Integer::sum) / points.size();
		int centerZ = points.stream().map(IPoint::getZ).reduce(0, Integer::sum) / points.size();
		var center = points.getFirst().set(centerX, centerZ);
		// Find the furthest point from center
		var furthest = points.stream().max(Comparator.comparing(p -> p.distance(center))).orElse(points.getLast());
		// Check if we are within radius
		if (furthest.distance(center) <= maxRadius) {
			return points;
		} else {
			// Remove the furthest point and re-cluster if not
			points.remove(furthest);
			return cluster(points);
		}
	}

	private static List<IPoint> calculateInternal(List<IPoint> points) {
		List<IPoint> lower = new ArrayList<>();
		// Step 2: Build the lower half of the hull
		for (var p : points) {
			// Remove last point from lower if we turn clockwise or straight (<= 0 cross product)
			while (lower.size() >= 2 &&
						   cross(lower.get(lower.size() - 2), lower.getLast(), p) <= 0) {
				lower.removeLast();
			}
			lower.add(p);
		}

		List<IPoint> upper = new ArrayList<>();
		// Step 3: Build the upper half of the hull (in reverse)
		for (int i = points.size() - 1; i >= 0; i--) {
			IPoint p = points.get(i);
			// Same logic: remove point if it makes a non-left turn
			while (upper.size() >= 2 &&
						   cross(upper.get(upper.size() - 2), upper.getLast(), p) <= 0) {
				upper.removeLast();
			}
			upper.add(p);
		}

		// Step 4: Combine the two halves
		// Last point of each list is duplicated, so remove them
		lower.removeLast();
		upper.removeLast();

		// Concatenate lower + upper hulls
		lower.addAll(upper);

		return lower; // This is the convex hull in counter-clockwise order
	}

	private static List<IPoint> minimalArea(IPoint center) {
		return List.of(
			center.add(0, -8),
			center.add(8, 8),
			center.add(-8, 8)
		);
	}

    // Cross product of AB and AC vectors: positive = left turn, negative = right turn
    private static long cross(IPoint a, IPoint b, IPoint c) {
        return (long)(b.getX() - a.getX()) * (c.getZ() - a.getZ()) - (long)(b.getZ() - a.getZ()) * (c.getX() - a.getX());
    }

}
