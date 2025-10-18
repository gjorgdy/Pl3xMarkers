package nl.gjorgdy.pl3xmarkers.core.markers;

import nl.gjorgdy.pl3xmarkers.core.entities.AreaPointEntity;
import net.pl3x.map.core.markers.marker.Marker;
import net.pl3x.map.core.markers.marker.Polygon;
import net.pl3x.map.core.markers.marker.Polyline;
import net.pl3x.map.core.markers.option.Fill;
import net.pl3x.map.core.markers.option.Stroke;

import java.util.List;

public class AreaMarkerBuilder extends MarkerBuilder<Polygon> {

    private AreaMarkerBuilder(Marker<Polygon> marker) {
        super(marker);
    }

    public static AreaMarkerBuilder newAreaMarker(String key, List<AreaPointEntity> points) {
        var line = new Polyline(key);
		points.stream().map(AreaPointEntity::toPl3xPoint).forEach(line::addPoint);
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

    public AreaMarkerBuilder stroke(int color) {
        return stroke(color, 2);
    }

    public AreaMarkerBuilder stroke(int color, int weight) {
        options.setStroke(
            new Stroke(weight, setAlpha(color, 255))
				.setEnabled(true)
        );
        return this;
    }

    public static int setAlpha(int color, int alpha) {
        alpha = alpha & 0xFF; // Ensure alpha is in 0-255 range
        return (color & 0x00FFFFFF) | (alpha << 24);
    }

}
