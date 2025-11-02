package nl.gjorgdy.pl3xmarkers.core.interfaces;

import nl.gjorgdy.pl3xmarkers.core.interfaces.entities.ILineMarker;
import nl.gjorgdy.pl3xmarkers.core.interfaces.entities.IPoint;

import java.util.Collection;

public interface ILineMarkerRepository<T extends ILineMarker> {

	/**
	 * Get all line markers in a world
	 * @param worldIdentifier the world identifier
	 * @return collection of line markers
	 */
	@SuppressWarnings("Unused")
	Collection<T> getLineMarkers(String worldIdentifier);

	/**
	 * Get line marker by its start and end points
	 * @param worldIdentifier the world identifier
	 * @param lowerPoint the lower point
	 * @param higherPoint the higher point
	 * @return the line marker or null if not found
	 */
	@SuppressWarnings("Unused")
	T getLineMarker(String worldIdentifier, IPoint lowerPoint, IPoint higherPoint);

	/**
	 * Get line marker by a point on the line
	 * @param worldIdentifier the world identifier
	 * @param point a point on the line
	 * @return the line marker or null if not found
	 */
	@SuppressWarnings("Unused")
	T getLineMarker(String worldIdentifier, IPoint point);

	/**
	 * Create a line marker between two points
	 * @param worldIdentifier the world identifier
	 * @param pointA the first point
	 * @param pointB the second point
	 * @return the created line marker
	 */
	@SuppressWarnings("Unused")
	T createLineMarker(String worldIdentifier, IPoint pointA, IPoint pointB);

	/**
	 * Remove a line marker by its start and end points
	 * @param worldIdentifier the world identifier
	 * @param lowerPoint the lower point
	 * @param higherPoint the higher point
	 * @return true if removed, false if not found
	 */
	@SuppressWarnings("Unused")
	boolean removeLineMarker(String worldIdentifier, IPoint lowerPoint, IPoint higherPoint);

	/**
	 * Remove a line marker by a point on the line
	 * @param worldIdentifier the world identifier
	 * @param point a point on the line
	 * @return true if removed, false if not found
	 */
	@SuppressWarnings("Unused")
	boolean removeLineMarker(String worldIdentifier, IPoint point);

}