package eu.hexasis.helixmarkers.markers;

import net.pl3x.map.core.markers.marker.Icon;
import net.pl3x.map.core.markers.marker.Marker;

public class IconBuilder extends MarkerBuilder<@org.jetbrains.annotations.NotNull Icon> {

    protected IconBuilder(Marker<@org.jetbrains.annotations.NotNull Icon> marker) {
        super(marker);
    }

    @SuppressWarnings("unused")
    public static MarkerBuilder<@org.jetbrains.annotations.NotNull Icon> newIconMarker(String key, String icon, int x, int z) {
        Icon iconMarker = new Icon(key, x, z, icon);
        return new IconBuilder(iconMarker);
    }

}
