package nl.gjorgdy.pl3xmarkers.json.entities;

import nl.gjorgdy.pl3xmarkers.core.interfaces.entities.IIconMarker;
import nl.gjorgdy.pl3xmarkers.core.interfaces.entities.IPoint;

public class IconMarker implements IIconMarker {

	private final String worldIdentifier;
	private final String layerKey;
	private final Point location;

	public IconMarker(String worldIdentifier, String layerKey, int x, int z) {
		this.worldIdentifier = worldIdentifier;
		this.layerKey = layerKey;
		location = new Point(x, z);
	}

	@Override
	public String getWorldIdentifier() {
		return worldIdentifier;
	}

	@Override
	public String getLayerKey() {
		return layerKey;
	}

	@Override
	public IPoint getLocation() {
		return location;
	}

}
