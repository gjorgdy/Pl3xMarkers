package nl.gjorgdy.pl3xmarkers.json.entities;

import nl.gjorgdy.pl3xmarkers.core.interfaces.entities.IPoint;

public class Point implements IPoint {

	private final int x, z;

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

}
