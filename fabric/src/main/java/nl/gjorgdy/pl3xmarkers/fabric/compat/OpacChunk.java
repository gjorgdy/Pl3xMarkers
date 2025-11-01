package nl.gjorgdy.pl3xmarkers.fabric.compat;

import net.minecraft.util.math.ChunkPos;
import nl.gjorgdy.pl3xmarkers.core.interfaces.entities.IPoint;

import java.util.List;

public record OpacChunk(ChunkPos pos, String playerName, String name, int color) {

	public String getName() {
		return (name.isEmpty() ? "" : name + " - ") + playerName + "'s claim";
	}

	public String getKey() {
		return "OpacChunk:" + pos.x + ":" + pos.z;
	}

	public List<IPoint> getCorners() {
		return List.of(
			// NorthWest
			new OpacPoint(pos().x * 16, pos().z * 16),
			// NorthEast
			new OpacPoint((pos().x + 1) * 16, pos().z * 16),
			// SouthEast
			new OpacPoint((pos().x + 1) * 16, (pos().z + 1) * 16),
			// SouthWest
			new OpacPoint(pos().x * 16, (pos().z + 1) * 16)
		);
	}

}
