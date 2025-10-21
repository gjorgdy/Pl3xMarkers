package nl.gjorgdy.pl3xmarkers.json.entities;

import nl.gjorgdy.pl3xmarkers.core.interfaces.entities.IAreaMarker;
import nl.gjorgdy.pl3xmarkers.core.interfaces.entities.IPoint;
import nl.gjorgdy.pl3xmarkers.json.repositories.AreaMarkerRepository;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class AreaMarker implements IAreaMarker {

	private final AreaMarkerRepository areaMarkerRepository;

	private final String worldIdentifier;
	private final String name;
	private final int color;
	private final List<Point> points;

	public AreaMarker(AreaMarkerRepository areaMarkerRepository, String worldIdentifier, String name, int color) {
		this.areaMarkerRepository = areaMarkerRepository;
		this.worldIdentifier = worldIdentifier;
		this.name = name;
		this.color = color;
		this.points = new ArrayList<>();
	}

	@Override
	public String getWorldIdentifier() {
		return worldIdentifier;
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
		return points.add(new Point(x, z));
	}

	@Override
	public boolean removePoint(int x, int z) {
		return points.removeIf(p -> p.getX() == x && p.getZ() == z);
	}

}
