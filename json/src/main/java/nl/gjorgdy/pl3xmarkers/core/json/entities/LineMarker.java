package nl.gjorgdy.pl3xmarkers.core.json.entities;

import nl.gjorgdy.pl3xmarkers.core.interfaces.entities.ILineMarker;
import nl.gjorgdy.pl3xmarkers.core.interfaces.entities.IPoint;

import java.util.Collection;
import java.util.List;

public class LineMarker implements ILineMarker<Point> {

	private final List<Point> points;
	private String world;

	public LineMarker(List<Point> points) {
		this.points = points;
	}

	public void setContext(String world) {
		this.world = world;
	}

	@Override
	public String getWorld() {
		return world;
	}

	@Override
	public Collection<Point> getPoints() {
		return points;
	}

	@Override
	public IPoint getFirstPoint() {
		return points.getFirst();
	}

	@Override
	public IPoint getLastPoint() {
		return points.getLast();
	}

	@Override
	public boolean isOnLine(Point point) {
		if (point == null || points == null || points.isEmpty()) {
			return false;
		}
		if (points.contains(point)) {
			return true;
		}
		for (int i = 0; i < points.size() - 1; i++) {
			Point p1 = points.get(i);
			Point p2 = points.get(i + 1);

			if (isPointOnSegment(point, p1, p2)) {
				return true;
			}
		}
		return false;
	}

	/**
	 * Checks if a point lies on the line segment between two other points.
	 * Uses the cross product method to verify collinearity and bounding box check.
	 *
	 * @param point the point to check
	 * @param p1    the first endpoint of the segment
	 * @param p2    the second endpoint of the segment
	 * @return true if the point lies on the segment, false otherwise
	 */
	private boolean isPointOnSegment(Point point, Point p1, Point p2) {
		long crossProduct = (long) (point.getZ() - p1.getZ()) * (p2.getX() - p1.getX())
									- (long) (p2.getZ() - p1.getZ()) * (point.getX() - p1.getX());

		if (crossProduct != 0) {
			return false;
		}
		int minX = Math.min(p1.getX(), p2.getX());
		int maxX = Math.max(p1.getX(), p2.getX());
		int minZ = Math.min(p1.getZ(), p2.getZ());
		int maxZ = Math.max(p1.getZ(), p2.getZ());

		return point.getX() >= minX && point.getX() <= maxX
					   && point.getZ() >= minZ && point.getZ() <= maxZ;
	}
}
