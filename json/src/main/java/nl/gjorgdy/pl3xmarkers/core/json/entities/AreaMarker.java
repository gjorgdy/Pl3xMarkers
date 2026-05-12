package nl.gjorgdy.pl3xmarkers.core.json.entities;

import nl.gjorgdy.pl3xmarkers.core.interfaces.entities.IAreaMarker;
import nl.gjorgdy.pl3xmarkers.core.interfaces.entities.IPoint;
import nl.gjorgdy.pl3xmarkers.core.json.repositories.MarkerRepository;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

public class AreaMarker extends Marker implements IAreaMarker {

	private final String name;
	private final int color;
	private final Set<Point> points = new HashSet<>();

	private transient Point minCorner;
	private transient Point maxCorner;

	public AreaMarker(MarkerRepository<AreaMarker> repository, String name, int color) {
		super(repository);
		this.name = name;
		this.color = color;
	}

	@Override
	public String getName() {
		return name;
	}

	@Override
	public int getColor() {
		return color;
	}

	@Override
	public Collection<? extends IPoint> getPoints() {
		return points;
	}

	@Override
	public boolean isEmpty() {
		return points.isEmpty();
	}

	@Override
	public IPoint getMinCorner() {
		if (minCorner == null) {
			calculateOuterCorners();
		}
		return minCorner;
	}

	@Override
	public IPoint getMaxCorner() {
		if (maxCorner == null) {
			calculateOuterCorners();
		}
		return maxCorner;
	}

	@Override
	public boolean addPoint(int x, int y, int z) {
		boolean added = points.add(new Point(x, y, z));
		if (added) {
			calculateOuterCorners();
			markDirty();
		}
		return added;
	}

	@Override
	public boolean removePoint(int x, int y, int z) {
		boolean removed = points.removeIf(point -> point.equals(x, y, z));
		if (removed) {
			calculateOuterCorners();
			markDirty();
		}
		return removed;
	}

	private void calculateOuterCorners() {
		if (points.isEmpty()) {
			minCorner = null;
			maxCorner = null;
			return;
		}
		
		int minX = Integer.MAX_VALUE;
		int minY = Integer.MAX_VALUE;
		int minZ = Integer.MAX_VALUE;
		int maxX = Integer.MIN_VALUE;
		int maxY = Integer.MIN_VALUE;
		int maxZ = Integer.MIN_VALUE;

		for (Point point : points) {
			if (point.x() < minX) {
				minX = point.x();
			}
			if (point.y() < minY) {
				minY = point.y();
			}
			if (point.z() < minZ) {
				minZ = point.z();
			}
			if (point.x() > maxX) {
				maxX = point.x();
			}
			if (point.y() > maxY) {
				maxY = point.y();
			}
			if (point.z() > maxZ) {
				maxZ = point.z();
			}
		}

		minCorner = new Point(minX, minY, minZ);
		maxCorner = new Point(maxX, maxY, maxZ);
	}

}
