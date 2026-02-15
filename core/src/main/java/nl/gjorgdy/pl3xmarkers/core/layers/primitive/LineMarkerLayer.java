package nl.gjorgdy.pl3xmarkers.core.layers.primitive;

import net.pl3x.map.core.world.World;
import nl.gjorgdy.pl3xmarkers.core.Pl3xMarkersCore;
import nl.gjorgdy.pl3xmarkers.core.interfaces.entities.ILineMarker;
import nl.gjorgdy.pl3xmarkers.core.interfaces.entities.IPoint;
import nl.gjorgdy.pl3xmarkers.core.markers.LineMarkerBuilder;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public class LineMarkerLayer extends MarkerLayer {

	public LineMarkerLayer(String key, String label, @NotNull World world, int priority) {
		super(key, label, world, priority);
	}

	@Override
	public void load() {
		Pl3xMarkersCore.storage()
				.getRaillineMarkerRepository()
				.getLineMarkers(worldIdentifier)
				.forEach(this::loadLine);
	}

	public void createLine(List<? extends IPoint> points) {
		var marker = Pl3xMarkersCore.storage()
							 .getRaillineMarkerRepository()
							 .createLineMarker(worldIdentifier, points);
		if (marker != null) {
			loadLine(marker);
			System.out.println("Created line marker: " + marker);
		}
	}

	public void loadLine(ILineMarker<? extends IPoint> lineMarker) {
		// remove if exists
		removeMarker(lineMarker.getKey());
		// add new
		var builder = LineMarkerBuilder
		  .newLineMarker(lineMarker.getKey(), lineMarker.getPoints())
							  .stroke(0xFFFFFF);
		addMarker(builder.build());
	}

}
