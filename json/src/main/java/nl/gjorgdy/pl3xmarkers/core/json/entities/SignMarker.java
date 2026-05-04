package nl.gjorgdy.pl3xmarkers.core.json.entities;

import nl.gjorgdy.pl3xmarkers.core.interfaces.entities.ISignMarker;
import nl.gjorgdy.pl3xmarkers.core.json.repositories.MarkerRepository;

public class SignMarker extends PointMarker implements ISignMarker {

	private String[] text;

	public SignMarker(MarkerRepository<SignMarker> repository, Point point, String[] text) {
		super(repository, point);
		this.text = text;
	}

	@Override
	public String[] getText() {
		return text;
	}

	@Override
	public void setText(String[] text) {
		this.text = text;
		markDirty();
	}

	@Override
	public String getKey() {
		return super.getKey();
	}
}
