package nl.gjorgdy.pl3xmarkers.core.interfaces.entities;

import com.google.errorprone.annotations.CheckReturnValue;
import net.pl3x.map.core.markers.Point;
import org.jetbrains.annotations.NotNull;

public interface IPoint extends Comparable<IPoint> {

	int x();

	int y();

	int z();

	@CheckReturnValue
	IPoint add(int dx, int dy, int dz);

	@CheckReturnValue
	IPoint set(int x, int y, int z);

	/**
	 * Convert this point to a point as used by Pl3xMap. The y coordinate is ignored as the map is top down.
	 *
	 * @return Pl3xMap point with the same x and z coordinates as this point
	 */
	@CheckReturnValue
	default Point toPl3xPoint() {
		return new Point(x(), z());
	}

	/**
	 * Get the distance of 2 points. The y coordinate is ignored as they are not important
	 *
	 * @param other the point to measure the distance to
	 * @return the distance between the two points, calculated using the Pythagorean theorem, ignoring the y coordinate
	 */
	@CheckReturnValue
	default double distance(@NotNull IPoint other) {
		return Math.sqrt(Math.pow(x() - other.x(), 2) + Math.pow(z() - other.z(), 2));
	}

	/**
	 * Compare this point to another point. The comparison is based on the sum of the differences in x and z
	 * coordinates.
	 * The y coordinate is ignored as they are not important
	 *
	 * @param other the object to be compared.
	 * @return a negative integer, zero, or a positive integer as this point is less than, equal to, or greater than the
	 * 		specified object.
	 */
	@Override
	default int compareTo(@NotNull IPoint other) {
		return (x() - other.x()) + (z() - other.z());
	}

	default String serialize() {
		return x() + ":" + y() + ":" + z();
	}

	/**
	 * Compare this point to another point.
	 * The y coordinate is ignored if either this points or the supplied y is equal to {@code Integer.MIN_VALUE}
	 *
	 * @param x x-coordinate to compare to
	 * @param y y-coordinate to compare to
	 * @param z z-coordinate to compare to
	 * @return {@code true} if the x and z coordinates are equal and the y coordinates are either equal or one of them
	 * 		is {@code Integer.MIN_VALUE}, {@code false} otherwise
	 */
	default boolean equals(int x, int y, int z) {
		return x() == x
				&& (y() == y || y() == Integer.MIN_VALUE || y == Integer.MIN_VALUE)
				&& z() == z;
	}

}
