package nl.gjorgdy.pl3xmarkers.core.interfaces.entities;

import net.pl3x.map.core.markers.Point;
import org.jetbrains.annotations.NotNull;

public interface IPoint extends Comparable<IPoint>, IKeyMarker {

	int getX();

	int getZ();

	default double distance(@NotNull IPoint other) {
		return Math.sqrt(Math.pow(getX() - other.getX(), 2) + Math.pow(getZ() - other.getZ(), 2));
	}

	IPoint add(int dx, int dz);

	IPoint set(int x, int z);

	@Override
	default String getKey () {
		return getX() + ":" + getZ();
	}

	default Point toPl3xPoint() {
		return new Point(getX(), getZ());
	}

	@Override
	default int compareTo(@NotNull IPoint other) {
		return (getX() - other.getX()) + (getZ() - other.getZ());
	}

}
