package nl.gjorgdy.pl3xmarkers.core.interfaces.entities;

public interface IIconMarker {

	String getWorld();

	String getLayer();

	IPoint getLocation();

	default String getKey() {
		return getLocation().getX() + ":" + getLocation().getZ();
	}

}
