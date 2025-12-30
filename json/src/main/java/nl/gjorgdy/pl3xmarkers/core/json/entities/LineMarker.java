package nl.gjorgdy.pl3xmarkers.core.json.entities;

import nl.gjorgdy.pl3xmarkers.core.interfaces.entities.ILineMarker;
import nl.gjorgdy.pl3xmarkers.core.interfaces.entities.IPoint;
import nl.gjorgdy.pl3xmarkers.core.json.repositories.LineMarkerRepository;

import java.util.Collection;
import java.util.List;

public class LineMarker implements ILineMarker {

	private LineMarkerRepository repository;

	private String world;
	private String name;
	private int color = 0xFFFFFF;

	private final Point lowerPoint;
	private final Point upperPoint;

	public LineMarker(Point lowerPoint, Point upperPoint) {
		this.lowerPoint = lowerPoint;
		this.upperPoint = upperPoint;
	}

	public void setContext(LineMarkerRepository repository, String world) {
		this.world = world;
		this.repository = repository;
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
	public void setName(String name) {
		if (repository != null) {
			repository.markDirty();
		}
		this.name = name;
	}

	@Override
	public int getColor() {
		return color;
	}

	@Override
	public void setColor(int color) {
		this.color = color;
	}

	@Override
	public Collection<IPoint> getPoints() {
		return List.of(lowerPoint, upperPoint);
	}

	@Override
	public IPoint getLowerPoint() {
		return lowerPoint;
	}

	@Override
	public IPoint getUpperPoint() {
		return upperPoint;
	}
}
