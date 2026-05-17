package nl.gjorgdy.pl3xmarkers.fabric.compat;

import nl.gjorgdy.pl3xmarkers.core.interfaces.entities.IPoint;

import java.util.Objects;

public record OpacEdge(IPoint p1, IPoint p2) {

	public OpacEdge {
		// Ensure a deterministic ordering for the endpoints so equals/hashCode behave
		// consistently regardless of object identity. Order by x then z using compare.
		if (compare(p1, p2) > 0) {
			IPoint temp = p1;
			p1 = p2;
			p2 = temp;
		}
	}

	private static int compare(IPoint a, IPoint b) {
		int cx = Integer.compare(a.x(), b.x());
		return cx != 0 ? cx : Integer.compare(a.z(), b.z());
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) {
			return true;
		}
		if (!(o instanceof OpacEdge(IPoint p3, IPoint p4))) {
			return false;
		}
		return (p1.equals(p3) && p2.equals(p4)) || (p1.equals(p4) && p2.equals(p3));
	}

	@Override
	public int hashCode() {
		return Objects.hash(p1, p2);
	}
}
