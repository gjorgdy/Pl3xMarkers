package nl.gjorgdy.pl3xmarkers.json.entities;

import nl.gjorgdy.pl3xmarkers.core.interfaces.entities.IIconMarker;
import nl.gjorgdy.pl3xmarkers.core.interfaces.entities.IPoint;

import java.util.Objects;

public class IconMarker implements IIconMarker {

	private final String world;
	private final String layer;
	private final Point location;

	public IconMarker(String worldIdentifier, String layerKey, int x, int z) {
		this.world = worldIdentifier;
		this.layer = layerKey;
		location = new Point(x, z);
	}

	@Override
	public String getWorld() {
		return world;
	}

	@Override
	public String getLayer() {
		return layer;
	}

	@Override
	public IPoint getLocation() {
		return location;
	}

	@Override
	public boolean equals(Object o) {
		if (o == null || getClass() != o.getClass()) return false;
		IconMarker that = (IconMarker) o;
		return Objects.equals(world, that.world) && Objects.equals(layer, that.layer) && Objects.equals(location, that.location);
	}

	@Override
	public int hashCode() {
		return Objects.hash(world, layer, location);
	}
}
