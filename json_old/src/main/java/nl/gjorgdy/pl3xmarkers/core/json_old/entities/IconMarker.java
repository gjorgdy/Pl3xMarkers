package nl.gjorgdy.pl3xmarkers.core.json_old.entities;

import nl.gjorgdy.pl3xmarkers.core.deprecated.interfaces.entities.IIconMarker;
import nl.gjorgdy.pl3xmarkers.core.deprecated.interfaces.entities.IPoint;

import java.util.Objects;

public class IconMarker extends Point implements IIconMarker {

	private transient String world;
	private transient String layer;

	public IconMarker(int x, int z) {
		super(x, z);
	}

	public IconMarker(String worldIdentifier, String layerKey, int x, int z) {
		super(x,z);
		world = worldIdentifier;
		layer = layerKey;
	}

	public void setContext(String worldIdentifier, String layerKey) {
		if (world == null) {
			world = worldIdentifier;
		}
		if (layer == null) {
			layer = layerKey;
		}
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
		return this;
	}

	@Override
	public boolean equals(Object o) {
		if (o == null || getClass() != o.getClass()) {
			return false;
		}
		IconMarker that = (IconMarker) o;
		return Objects.equals(world, that.world) && Objects.equals(layer, that.layer) && x == that.x && z == that.z;
	}

	@Override
	public int hashCode() {
		return Objects.hash(world, layer, x, z);
	}
}
