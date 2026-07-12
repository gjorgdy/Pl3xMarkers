package nl.gjorgdy.pl3xmarkers.core.interfaces.entities;

public interface IMarker {

	/**
	 * Get the world identifier this marker is in
	 *
	 * @return a world identifier as string
	 */
	String getWorldIdentifier();

	/**
	 * Get the key of the layer this marker is in
	 *
	 * @return a layer key as string
	 */
	String getLayerKey();

	/**
	 * Gets the key for this marker
	 *
	 * @return the key of this marker
	 */
	String getKey();

}
