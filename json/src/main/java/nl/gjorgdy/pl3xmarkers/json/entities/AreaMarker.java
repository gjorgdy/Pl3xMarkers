package nl.gjorgdy.pl3xmarkers.json.entities;

import nl.gjorgdy.pl3xmarkers.core.interfaces.entities.IAreaMarker;
import nl.gjorgdy.pl3xmarkers.core.interfaces.entities.IPoint;
import nl.gjorgdy.pl3xmarkers.json.repositories.AreaMarkerRepository;

import java.util.*;

public class AreaMarker implements IAreaMarker {

	public transient AreaMarkerRepository areaMarkerRepository;

	private final String world;
	private final String name;
	private final int color;
	private final Set<Point> points;

	public AreaMarker(AreaMarkerRepository areaMarkerRepository, String worldIdentifier, String name, int color) {
		this.areaMarkerRepository = areaMarkerRepository;
		this.world = worldIdentifier;
		this.name = name;
		this.color = color;
		this.points = new HashSet<>();
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
	public boolean addPoint(int x, int z) {
		var added = points.add(new Point(x, z));
		areaMarkerRepository.markDirty();
		areaMarkerRepository.write();
		return added;
	}

	@Override
	public boolean removePoint(int x, int z) {
		var removed = points.removeIf(p -> p.getX() == x && p.getZ() == z);
		areaMarkerRepository.markDirty();
		areaMarkerRepository.write();
		return removed;
	}

	@Override
	public boolean equals(Object o) {
		if (o == null || getClass() != o.getClass()) return false;
		AreaMarker that = (AreaMarker) o;
		return color == that.color && Objects.equals(areaMarkerRepository, that.areaMarkerRepository) && Objects.equals(world, that.world) && Objects.equals(name, that.name) && Objects.equals(points, that.points);
	}

	@Override
	public int hashCode() {
		return Objects.hash(areaMarkerRepository, world, name, color, points);
	}
}
