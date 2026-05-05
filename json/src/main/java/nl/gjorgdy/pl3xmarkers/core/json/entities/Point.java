package nl.gjorgdy.pl3xmarkers.core.json.entities;

import nl.gjorgdy.pl3xmarkers.core.interfaces.entities.IPoint;

import java.util.Objects;

public record Point(int x, int y, int z) implements IPoint {

	@Override
	public IPoint add(int dx, int dy, int dz) {
		return new Point(x + dx, y + dy, z + dz);
	}

	@Override
	public IPoint set(int x, int y, int z) {
		return new Point(x, y, z);
	}

	@Override
	public boolean equals(Object o) {
		if (o == null || getClass() != o.getClass()) {
			return false;
		}
		Point point = (Point) o;
		return x == point.x && y == point.y && z == point.z;
	}

	@Override
	public int hashCode() {
		return Objects.hash(x, z);
	}
}
