package nl.gjorgdy.pl3xmarkers.core.interfaces.entities;

public interface IIconMarker extends IKeyMarker {

	String getWorld();

	String getLayer();

	IPoint getLocation();

	@Override
	default String getKey() {
		return getLocation().getX() + ":" + getLocation().getZ();
	}

}
