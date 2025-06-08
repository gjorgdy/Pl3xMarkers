package eu.hexasis.helixmarkers.helpers;

import eu.hexasis.helixmarkers.objects.Position;

import java.util.ArrayList;
import java.util.List;

public class ConvexHull {

    private ConvexHull() {}

    private static final int maxDistance = 128;

    /**
     * Perform a convex hull calculation to only use the outer hull of points for the area
     * @param points input list of points to use
     * @return a line of external points
     */
    public static List<Position> calculate(List<Position> points) {
        if (points.size() <= 1) return new ArrayList<>(points); // No hull needed

        // Step 1: Sort the points by x (and y if tie)
        points.sort(null);

        List<Position> lower = new ArrayList<>();
        // Step 2: Build the lower half of the hull
        for (Position p : points) {
            // Remove last point from lower if we turn clockwise or straight (<= 0 cross product)
            while (lower.size() >= 2 &&
                    cross(lower.get(lower.size() - 2), lower.getLast(), p) <= 0) {
                lower.removeLast();
            }
            // only add p if it's not too far from the last point
            if (lower.isEmpty() || lower.getLast().distance(p) <= maxDistance) {
                lower.add(p);
            }
        }

        List<Position> upper = new ArrayList<>();
        // Step 3: Build the upper half of the hull (in reverse)
        for (int i = points.size() - 1; i >= 0; i--) {
            Position p = points.get(i);
            // Same logic: remove point if it makes a non-left turn
            while (upper.size() >= 2 &&
                    cross(upper.get(upper.size() - 2), upper.getLast(), p) <= 0) {
                upper.removeLast();
            }
            // only add p if it's not too far from the last point
            if (upper.isEmpty() || upper.getLast().distance(p) <= maxDistance) {
                upper.add(p);
            }
        }

        // Step 4: Combine the two halves
        // Last point of each list is duplicated, so remove them
        lower.removeLast();
        upper.removeLast();

        // Concatenate lower + upper hulls
        lower.addAll(upper);

        return lower; // This is the convex hull in counter-clockwise order
    }

    // Cross product of AB and AC vectors: positive = left turn, negative = right turn
    private static long cross(Position a, Position b, Position c) {
        return (long)(b.X() - a.X()) * (c.Z() - a.Z()) - (long)(b.Z() - a.Z()) * (c.X() - a.X());
    }

}
