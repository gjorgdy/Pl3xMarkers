package nl.gjorgdy.pl3xmarkers.core.interfaces.entities;

import net.pl3x.map.core.markers.Point;
import org.jetbrains.annotations.NotNull;

public interface IPoint extends Comparable<IPoint> {

	int getX();

	int getZ();

	default double distance(@NotNull IPoint other) {
		return Math.sqrt(Math.pow(this.getX() - other.getX(), 2) + Math.pow(this.getZ() - other.getZ(), 2));
	}

	IPoint add(int dx, int dz);

	IPoint set(int x, int z);

	default Point toPl3xPoint() {
		return new Point(getX(), getZ());
	}

	@Override
	default int compareTo(@NotNull IPoint other) {
		return (this.getX() - other.getX()) + (this.getZ() - other.getZ());
	}

}
