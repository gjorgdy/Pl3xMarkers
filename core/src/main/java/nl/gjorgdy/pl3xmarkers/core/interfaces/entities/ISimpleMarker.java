package nl.gjorgdy.pl3xmarkers.core.interfaces.entities;

import org.jspecify.annotations.Nullable;

public interface ISimpleMarker extends IPointMarker {

	/**
	 * Get the name of this marker
	 *
	 * @return name of marker, or null if none set
	 */
	@Nullable String getName();

	/**
	 * Set the name of this marker
	 *
	 * @param name the new name for this marker
	 */
	void setName(String name);

	/**
	 * Get the color of this marker
	 *
	 * @return color of marker, or -1 if none set
	 */
	int getColor();

	/**
	 * Set the color of this marker
	 *
	 * @param color the new color for this marker
	 */
	void setColor(int color);

}
