package nl.gjorgdy.pl3xmarkers.core.interfaces;

import nl.gjorgdy.pl3xmarkers.core.interfaces.entities.ISignMarker;
import org.intellij.lang.annotations.Language;

public interface ISignMarkerRepository<T extends ISignMarker> extends IMarkerRepository<T> {

	@SuppressWarnings("Unused")
	T create(int x, int y, int z, @Language("HTML") String[] text);

	/**
	 * Edit the text of a sign marker
	 *
	 * @param x    x-coordinate of sign
	 * @param y    y-coordinate of sign
	 * @param z    z-coordinate of sign
	 * @param text new text on sign
	 * @return true if edited, false if no marker exists at the given coordinates
	 */
	@SuppressWarnings("Unused")
	boolean edit(int x, int y, int z, @Language("HTML") String[] text);

	/**
	 * Edit the text of a sign marker or create a new marker if it doesn't exist
	 *
	 * @param x    x-coordinate of sign
	 * @param y    y-coordinate of sign
	 * @param z    z-coordinate of sign
	 * @param text new text on sign
	 * @return true if edited, false if created a new marker
	 */
	@SuppressWarnings("Unused")
	boolean editOrCreate(int x, int y, int z, @Language("HTML") String[] text);

	@SuppressWarnings("Unused")
	boolean remove(int x, int y, int z);

}
