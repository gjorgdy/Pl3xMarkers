package nl.gjorgdy.pl3xmarkers.core.layers.primitive;

import net.pl3x.map.core.world.World;
import nl.gjorgdy.pl3xmarkers.core.interfaces.entities.ILineMarker;
import nl.gjorgdy.pl3xmarkers.core.markers.LineMarkerBuilder;
import org.jetbrains.annotations.NotNull;

public class LineMarkerLayer extends MarkerLayer {

	public LineMarkerLayer(String key, String label, @NotNull World world, int priority) {
		super(key, label, world, priority);
	}

	@Override
	public void load() {
		// No-op
	}

	public void loadLine(ILineMarker lineMarker) {
		// remove if exists
		removeMarker(lineMarker.getKey());
		// add new
		var builder = LineMarkerBuilder
		  .newLineMarker(lineMarker.getKey(), lineMarker.getPoints())
		  .addPopup(lineMarker.getName())
		  .stroke(lineMarker.getColor());
		addMarker(builder.build());
	}

}
