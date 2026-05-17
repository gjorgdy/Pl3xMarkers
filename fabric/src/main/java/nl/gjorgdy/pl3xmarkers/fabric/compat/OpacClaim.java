package nl.gjorgdy.pl3xmarkers.fabric.compat;

import nl.gjorgdy.pl3xmarkers.core.interfaces.entities.IPoint;

import java.util.ArrayList;
import java.util.List;

public class OpacClaim {

	public final String key;
	public final String name;
	public final int color;

	public final List<OpacChunk> chunks;

	public OpacClaim(String name, int color) {
		key = createKey(name, color);
		chunks = new ArrayList<>();
		this.name = name;
		this.color = color;
	}

	public static String createKey(String name, int color) {
		return "OpacClaim:" + name + ":" + color;
	}

	public void addChunk(OpacChunk chunk) {
		chunks.add(chunk);
	}

	public void removeChunk(OpacChunk chunk) {
		chunks.remove(chunk);
	}

	public List<List<IPoint>> getPolygons() {
		return chunks.stream().map(OpacChunk::getCorners).toList();
	}

}
