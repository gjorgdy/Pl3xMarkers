package nl.gjorgdy.pl3xmarkers.fabric.compat;

import net.minecraft.util.math.ChunkPos;

public record OpacChunk(ChunkPos pos, String playerName, String name, int color) {

	public String getName() {
		return (name.isEmpty() ? "" : name + " - ") + playerName + "'s claim";
	}

	public String getKey() {
		return "OpacChunk:" + pos.x + ":" + pos.z;
	}

}
