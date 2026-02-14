package nl.gjorgdy.pl3xmarkers.core.markers;

import net.pl3x.map.core.markers.marker.Marker;
import net.pl3x.map.core.markers.marker.Polyline;
import nl.gjorgdy.pl3xmarkers.core.interfaces.entities.IPoint;

import java.util.Collection;

public class LineMarkerBuilder extends MarkerBuilder<Polyline> {

	protected LineMarkerBuilder(Marker<Polyline> marker) {
		super(marker);
	}

	public static LineMarkerBuilder newLineMarker(String key, Collection<? extends IPoint> points) {
		var line = new Polyline(key);
		points.stream().map(IPoint::toPl3xPoint).forEach(line::addPoint);
		return new LineMarkerBuilder(line);
	}
}
