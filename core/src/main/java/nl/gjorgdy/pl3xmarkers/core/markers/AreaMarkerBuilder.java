package nl.gjorgdy.pl3xmarkers.core.markers;

import net.pl3x.map.core.markers.marker.Circle;
import net.pl3x.map.core.markers.marker.Marker;
import net.pl3x.map.core.markers.marker.Polygon;
import net.pl3x.map.core.markers.marker.Polyline;
import net.pl3x.map.core.markers.option.Fill;
import net.pl3x.map.core.markers.option.Stroke;
import nl.gjorgdy.pl3xmarkers.core.interfaces.entities.IPoint;

import java.util.List;

public class AreaMarkerBuilder<T extends Marker<T>> extends MarkerBuilder<T> {

    private AreaMarkerBuilder(Marker<T> marker) {
        super(marker);
    }

    public static AreaMarkerBuilder<Polygon> newAreaMarker(String key, List<IPoint> points) {
        var line = new Polyline(key);
		points.stream().map(IPoint::toPl3xPoint).forEach(line::addPoint);
        Polygon area = new Polygon(key, line);
        return new AreaMarkerBuilder<>(area);
    }

    public static AreaMarkerBuilder<Circle> newAreaMarker(String key, IPoint center, int radius) {
        var circle = new Circle(key, center.x(), center.z(), radius);
        return new AreaMarkerBuilder<>(circle);
    }

    public static int setAlpha(int color, int alpha) {
        alpha = alpha & 0xFF; // Ensure alpha is in 0-255 range
        return (color & 0x00FFFFFF) | (alpha << 24);
    }

    public AreaMarkerBuilder<T> fill(int color) {
        return fill(color, 96);
    }

    public AreaMarkerBuilder<T> fill(int color, int alpha) {
        options.setFill(
                new Fill(setAlpha(color, alpha))
                        .setEnabled(true)
        );
        return this;
    }

    public AreaMarkerBuilder<T> stroke(int color) {
        return stroke(color, 2);
    }

    public AreaMarkerBuilder<T> stroke(int color, int weight) {
        options.setStroke(
            new Stroke(weight, setAlpha(color, 255))
				.setEnabled(true)
        );
        return this;
    }

}
