package nl.gjorgdy.pl3xmarkers.fabric.compat;

import net.minecraft.world.level.ChunkPos;
import nl.gjorgdy.pl3xmarkers.core.interfaces.entities.IPoint;

import java.util.List;

public record OpacChunk(ChunkPos pos, String playerName, String name, int color) {

	public String getName() {
		return (name.isEmpty() ? "" : name + " - ") + playerName + "'s claim";
	}

	public String getKey() {
		return "OpacChunk:" + pos.x() + ":" + pos.z();
	}

	public List<IPoint> getCorners() {
		return List.of(
				// northwest
				OpacPoint.ofChunkPos(pos().x(), pos().z()),
				// northeast
				OpacPoint.ofChunkPos(pos().x() + 1, pos().z()),
				// southeast
				OpacPoint.ofChunkPos(pos().x() + 1, pos().z() + 1),
				// southwest
				OpacPoint.ofChunkPos(pos().x(), pos().z() + 1)
		);
	}

	public List<OpacEdge> getEdges() {
		var corners = getCorners();
		return List.of(
				// north
				new OpacEdge(corners.get(0), corners.get(1)),
				// east
				new OpacEdge(corners.get(1), corners.get(2)),
				// south
				new OpacEdge(corners.get(2), corners.get(3)),
				// west
				new OpacEdge(corners.get(3), corners.get(0))
		);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj) {
			return true;
		}
		if (obj instanceof OpacChunk other) {
			return pos.equals(other.pos);
		}
		return false;
	}
}
