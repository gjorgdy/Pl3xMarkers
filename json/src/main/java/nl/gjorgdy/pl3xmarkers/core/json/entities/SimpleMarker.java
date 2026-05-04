package nl.gjorgdy.pl3xmarkers.core.json.entities;

import nl.gjorgdy.pl3xmarkers.core.interfaces.entities.ISimpleMarker;
import nl.gjorgdy.pl3xmarkers.core.json.repositories.MarkerRepository;

public class SimpleMarker extends PointMarker implements ISimpleMarker {

	private String name;
	private int color;

	public SimpleMarker(MarkerRepository<SimpleMarker> repository, Point point) {
		super(repository, point);
	}

	public SimpleMarker(MarkerRepository<SimpleMarker> repository, Point point, String name, int color) {
		super(repository, point);
		this.name = name;
		this.color = color;
	}

	@Override
	public String getName() {
		return name;
	}

	@Override
	public void setName(String name) {
		this.name = name;
		markDirty();
	}

	@Override
	public int getColor() {
		return color;
	}

	@Override
	public void setColor(int color) {
		this.color = color;
		markDirty();
	}

}
