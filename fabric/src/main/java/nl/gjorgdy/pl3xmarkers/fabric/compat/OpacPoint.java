package nl.gjorgdy.pl3xmarkers.fabric.compat;

import nl.gjorgdy.pl3xmarkers.core.interfaces.entities.IPoint;

import java.util.Objects;

public record OpacPoint(int x, int y, int z) implements IPoint {

	public static OpacPoint ofChunkPos(int x, int z) {
		return new OpacPoint(x * 16, 0, z * 16);
	}

	@Override
	public IPoint add(int dx, int dy, int dz) {
		return new OpacPoint(x + dx, 0, z + dz);
	}

	@Override
	public IPoint set(int x, int y, int z) {
		return new OpacPoint(x, 0, z);
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (!(o instanceof IPoint that)) return false;
		return x == that.x() && z == that.z();
	}

	@Override
	public int hashCode() {
		return Objects.hash(x, z);
	}

}
