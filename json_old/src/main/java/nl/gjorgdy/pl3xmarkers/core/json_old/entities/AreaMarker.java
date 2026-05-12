package nl.gjorgdy.pl3xmarkers.core.json_old.entities;

import nl.gjorgdy.pl3xmarkers.core.deprecated.interfaces.entities.IAreaMarker;
import nl.gjorgdy.pl3xmarkers.core.deprecated.interfaces.entities.IPoint;
import nl.gjorgdy.pl3xmarkers.core.json_old.repositories.AreaMarkerRepository;

import java.util.Collection;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

public class AreaMarker implements IAreaMarker {

	private final String name;
	private final int color;
	private final Set<Point> points;
	private transient AreaMarkerRepository repository;
	private transient String world;

	public AreaMarker(AreaMarkerRepository areaMarkerRepository, String worldIdentifier, String name, int color) {
		repository = areaMarkerRepository;
		world = worldIdentifier;
		this.name = name;
		this.color = color;
		points = new HashSet<>();
	}

	public void setContext(AreaMarkerRepository repository, String worldIdentifier) {
		if (this.repository == null) {
			this.repository = repository;
		}
		if (world == null) {
			world = worldIdentifier;
		}
	}

	@Override
	public String getWorld() {
		return world;
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
	public IPoint getMinCorner() {
		var minX = points.stream().map(Point::getX).min(Integer::compareTo);
		var minZ = points.stream().map(Point::getZ).min(Integer::compareTo);
		if (minX.isPresent() && minZ.isPresent()) {
			return new Point(minX.get(), minZ.get());
		}
		return null;
	}

	@Override
	public IPoint getMaxCorner() {
		var maxX = points.stream().map(Point::getX).max(Integer::compareTo);
		var maxZ = points.stream().map(Point::getZ).max(Integer::compareTo);
		if (maxX.isPresent() && maxZ.isPresent()) {
			return new Point(maxX.get(), maxZ.get());
		}
		return null;
	}

	@Override
	public boolean addPoint(int x, int z) {
		var added = points.add(new Point(x, z));
		repository.markDirty();
		return added;
	}

	@Override
	public boolean removePoint(int x, int z) {
		var removed = points.removeIf(p -> p.getX() == x && p.getZ() == z);
		// If no points are left, remove the entire area marker
		if (points.isEmpty()) {
			removed = removed && repository.removeArea(this);
		}
		repository.markDirty();
		return removed;
	}

	@Override
	public boolean equals(Object o) {
		if (o == null || getClass() != o.getClass()) {
			return false;
		}
		AreaMarker that = (AreaMarker) o;
		return color == that.color && Objects.equals(world, that.world) && Objects.equals(name, that.name) && Objects.equals(points, that.points);
	}

	@Override
	public int hashCode() {
		return Objects.hash(world, name, color, points);
	}
}
