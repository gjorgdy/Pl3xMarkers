package nl.gjorgdy.pl3xmarkers.core.interfaces;

import nl.gjorgdy.pl3xmarkers.core.interfaces.entities.ILineMarker;
import nl.gjorgdy.pl3xmarkers.core.interfaces.entities.IPoint;

import java.util.Collection;
import java.util.List;

public interface ILineMarkerRepository<T extends ILineMarker<P>, P extends IPoint> {

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
	 * @param firstPoint the lower point
	 * @param lastPoint the higher point
	 * @return the line marker or null if not found
	 */
	@SuppressWarnings("Unused")
	T getLineMarker(String worldIdentifier, IPoint firstPoint, IPoint lastPoint);

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
	 * @param points the points on the line
	 * @return the created line marker
	 */
	@SuppressWarnings("Unused")
	T createLineMarker(String worldIdentifier, List<? extends IPoint> points);

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