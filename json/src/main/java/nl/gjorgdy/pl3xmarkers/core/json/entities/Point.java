package nl.gjorgdy.pl3xmarkers.core.json.entities;

import nl.gjorgdy.pl3xmarkers.core.interfaces.entities.IPoint;

public record Point(int x, int y, int z) implements IPoint {

	@Override
	public IPoint add(int dx, int dy, int dz) {
		return new Point(x + dx, y + dy, z + dz);
	}

	@Override
	public IPoint set(int x, int y, int z) {
		return new Point(x, y, z);
	}
}
