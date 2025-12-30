package nl.gjorgdy.pl3xmarkers.core.markers;

import net.pl3x.map.core.markers.marker.Marker;
import net.pl3x.map.core.markers.marker.Polygon;
import net.pl3x.map.core.markers.marker.Polyline;
import net.pl3x.map.core.markers.option.Fill;
import net.pl3x.map.core.markers.option.Stroke;
import nl.gjorgdy.pl3xmarkers.core.interfaces.entities.IPoint;

import java.util.List;

public class AreaMarkerBuilder extends MarkerBuilder<Polygon> {

    private AreaMarkerBuilder(Marker<Polygon> marker) {
        super(marker);
    }

    public static AreaMarkerBuilder newAreaMarker(String key, List<IPoint> points) {
        var line = new Polyline(key);
		points.stream().map(IPoint::toPl3xPoint).forEach(line::addPoint);
        Polygon area = new Polygon(key, line);
        return new AreaMarkerBuilder(area);
    }

    public AreaMarkerBuilder fill(int color) {
        return fill(color, 96);
    }

    public AreaMarkerBuilder fill(int color, int alpha) {
        options.setFill(
                new Fill(setAlpha(color, alpha))
                        .setEnabled(true)
        );
        return this;
    }

}
