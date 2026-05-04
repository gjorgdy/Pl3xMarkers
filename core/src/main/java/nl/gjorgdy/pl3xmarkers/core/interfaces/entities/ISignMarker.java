package nl.gjorgdy.pl3xmarkers.core.interfaces.entities;

public interface ISignMarker extends IPointMarker {

	/**
	 * Get the text of a sign
	 *
	 * @return an array of strings for every line of the sign
	 */
	String[] getText();

	/**
	 * Set the text of a sign
	 *
	 * @param text an array of strings for every line of the sign
	 */
	void setText(String[] text);

}
