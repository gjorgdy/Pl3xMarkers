package nl.gjorgdy.pl3xmarkers.core.json.entities;

import nl.gjorgdy.pl3xmarkers.core.interfaces.entities.IPoint;
import nl.gjorgdy.pl3xmarkers.core.interfaces.entities.IPointMarker;
import nl.gjorgdy.pl3xmarkers.core.json.repositories.MarkerRepository;

public abstract class PointMarker extends Marker implements IPointMarker {

	private final Point point;

	public PointMarker(MarkerRepository<? extends PointMarker> repository, Point point) {
		super(repository);
		this.point = point;
	}

	@Override
	public IPoint getPosition() {
		return point;
	}
}
