package eu.hexasis.helixmarkers.markers;

import eu.hexasis.helixmarkers.entities.DynamicLinePointEntity;
import net.pl3x.map.core.markers.marker.Marker;
import net.pl3x.map.core.markers.marker.Polyline;
import net.pl3x.map.core.markers.option.Stroke;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public class LineMarkerBuilder extends MarkerBuilder<@NotNull Polyline> {

    private LineMarkerBuilder(Marker<@NotNull Polyline> marker) {
        super(marker);
    }

    public static LineMarkerBuilder newLineMarker(String key, List<DynamicLinePointEntity> points) {
        var line = new Polyline(key);
        for (DynamicLinePointEntity point : points) {
            line.addPoint(point.toPl3xPoint());
        }
        return new LineMarkerBuilder(line);
    }

    public LineMarkerBuilder stroke(int color) {
        return stroke(color, 2);
    }

    public LineMarkerBuilder stroke(int color, int weight) {
        options.setStroke(
            new Stroke(weight, color)
        );
        return this;
    }

}
