package nl.gjorgdy.pl3xmarkers.core.interfaces.entities;

import org.jetbrains.annotations.NotNull;
import org.jspecify.annotations.Nullable;

import java.util.Collection;
import java.util.Locale;

public interface IAreaMarker extends IMarker {

	/**
	 * Get the name of this area
	 *
	 * @return the name of this area
	 */
	@NotNull String getName();

	/**
	 * Get the color of this area
	 *
	 * @return the color of this area as integer
	 */
	int getColor();

	/**
	 * Get all points of this area
	 *
	 * @return a collection of points of this area
	 */
	Collection<? extends IPoint> getPoints();

	/**
	 * Check if this area has points
	 *
	 * @return true, if it has no points
	 */
	boolean isEmpty();

	/**
	 * Get a point of the lowest x and z coordinates of this area
	 *
	 * @return the minimum outer corner with y 0
	 */
	@Nullable IPoint getMinCorner();

	/**
	 * Get a point of the highest x and z coordinates of this area
	 *
	 * @return the maximum outer corner with y 0
	 */
	@Nullable IPoint getMaxCorner();

	/**
	 * Add a point to this area
	 *
	 * @param x the x-coordinate
	 * @param y the y-coordinate
	 * @param z the z-coordinate
	 * @return true if the point was added, false if this point already exists
	 */
	boolean addPoint(int x, int y, int z);

	/**
	 * Remove a point to this area
	 *
	 * @param x the x-coordinate
	 * @param y the y-coordinate
	 * @param z the z-coordinate
	 * @return true if the point was removed, false if this point doesn't exist
	 */
	boolean removePoint(int x, int y, int z);

	/**
	 * Get the key of this area
	 *
	 * @return a composite of the name of this area and color
	 */
	default String getKey() {
		return getName().toLowerCase(Locale.ROOT).trim().replace(" ", "_") + ":" + getColor();
	}

}
