package nl.gjorgdy.pl3xmarkers.core.json.entities;

import nl.gjorgdy.pl3xmarkers.core.interfaces.entities.INamedIconMarker;

public class NamedIconMarker extends IconMarker implements INamedIconMarker {

	protected String name;

	public NamedIconMarker(String worldIdentifier, String layerKey, int x, int z, String name) {
		super(worldIdentifier, layerKey, x, z);
		this.name = name;
	}

	@Override
	public String getName() {
		return name;
	}

	@Override
	public void setName(String name) {
		this.name = name;
	}

}
