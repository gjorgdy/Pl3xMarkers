package nl.gjorgdy.pl3xmarkers.fabric.compat;

import nl.gjorgdy.pl3xmarkers.core.interfaces.entities.IPoint;

public record OpacPoint(int X, int Z) implements IPoint {

	@Override
	public int getX() {
		return X;
	}

	@Override
	public int getZ() {
		return Z;
	}

	@Override
	public IPoint add(int dx, int dz) {
		return new OpacPoint(X + dx, Z + dz);
	}

	@Override
	public IPoint set(int x, int z) {
		return new OpacPoint(x, z);
	}
}
