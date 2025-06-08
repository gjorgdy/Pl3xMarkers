package eu.hexasis.helixmarkers.markers;

import eu.hexasis.helixmarkers.helpers.ConvexHull;
import eu.hexasis.helixmarkers.objects.Position;
import net.pl3x.map.core.markers.Point;
import net.pl3x.map.core.markers.marker.Marker;
import net.pl3x.map.core.markers.marker.Polygon;
import net.pl3x.map.core.markers.marker.Polyline;
import net.pl3x.map.core.markers.option.Fill;
import net.pl3x.map.core.markers.option.Stroke;

import java.util.List;


public class AreaBuilder extends MarkerBuilder<@org.jetbrains.annotations.NotNull Polygon> {

    private AreaBuilder(Marker<@org.jetbrains.annotations.NotNull Polygon> marker) {
        super(marker);
    }

    public static AreaBuilder newArea(String key, List<Position> points) {
        var line = new Polyline(key);
        for (Position point : ConvexHull.calculate(points)) {
            line.addPoint(new Point(point.X(), point.Z()));
        }
        Polygon area = new Polygon(key, line);
        return new AreaBuilder(area);
    }

    public AreaBuilder fill(int color) {
        options.setFill(
            new Fill(setAlpha(color, 64))
                .setEnabled(true)
        );
        return this;
    }

    public AreaBuilder stroke(int color) {
        options.setStroke(
            new Stroke(2, color)
        );
        return this;
    }

    public static int setAlpha(int color, int alpha) {
        alpha = alpha & 0xFF; // Ensure alpha is in 0-255 range
        return (color & 0x00FFFFFF) | (alpha << 24);
    }

}
