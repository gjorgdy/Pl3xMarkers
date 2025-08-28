package nl.gjorgdy.pl3xmarkers.markers;

import net.pl3x.map.core.markers.Vector;
import net.pl3x.map.core.markers.marker.Icon;
import net.pl3x.map.core.markers.marker.Marker;

public class IconMarkerBuilder extends MarkerBuilder<@org.jetbrains.annotations.NotNull Icon> {

    protected IconMarkerBuilder(Marker<@org.jetbrains.annotations.NotNull Icon> marker) {
        super(marker);
    }

    @SuppressWarnings("unused")
    public static IconMarkerBuilder newIconMarker(String key, String icon, int x, int z) {
        Icon iconMarker = new Icon(key, x, z, icon);
        return new IconMarkerBuilder(iconMarker);
    }

    @SuppressWarnings("unused")
    public IconMarkerBuilder centerIcon(int width, int height) {
        if (marker instanceof Icon icon) {
            int xOffset = width / 2;
            int yOffset = height / 2;
            icon.setAnchor(
                    new Vector(xOffset, yOffset)
            );
        } else {
            throw new RuntimeException("Marker is not of the correct type Marker<Icon>");
        }
        return this;
    }

}
