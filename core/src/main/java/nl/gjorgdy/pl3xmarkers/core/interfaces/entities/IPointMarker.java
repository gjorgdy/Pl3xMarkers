package nl.gjorgdy.pl3xmarkers.core.interfaces.entities;

public interface IPointMarker extends IMarker {

	/**
	 * Get the position of this marker
	 *
	 * @return point of position
	 */
	IPoint getPosition();

	/**
	 * Get the key of this marker
	 *
	 * @return a composite key constructed from this markers position
	 */
	default String getKey() {
		return getPosition().serialize();
	}

}
