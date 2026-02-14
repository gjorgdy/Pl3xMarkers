package nl.gjorgdy.pl3xmarkers.core.json.entities;

import nl.gjorgdy.pl3xmarkers.core.interfaces.entities.ISignMarker;

public class SignMarker extends IconMarker implements ISignMarker {

	protected String[] text;

	public SignMarker(String worldIdentifier, String layerKey, int x, int z, String[] text) {
		super(worldIdentifier, layerKey, x, z);
		this.text = text;
	}

	@Override
	public String[] getText() {
		return text;
	}

	@Override
	public void setText(String[] text) {
		this.text = text;
	}

}
