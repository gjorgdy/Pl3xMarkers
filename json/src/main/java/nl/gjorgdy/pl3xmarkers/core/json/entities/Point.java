package nl.gjorgdy.pl3xmarkers.core.json.entities;

import nl.gjorgdy.pl3xmarkers.core.interfaces.entities.IPoint;

import java.util.Objects;

public class Point implements IPoint {

	protected final int x, z;

	public Point(int x, int z) {
		this.x = x;
		this.z = z;
	}

	@Override
	public int getX() {
		return x;
	}

	@Override
	public int getZ() {
		return z;
	}

	@Override
	public IPoint add(int dx, int dz) {
		return new Point(this.x + dx, this.z + dz);
	}

	@Override
	public IPoint set(int x, int z) {
		return new Point(x, z);
	}

	@Override
	public boolean equals(Object o) {
		if (o == null || getClass() != o.getClass()) return false;
		Point point = (Point) o;
		return x == point.x && z == point.z;
	}

	@Override
	public int hashCode() {
		return Objects.hash(x, z);
	}
}
