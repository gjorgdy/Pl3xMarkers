package eu.hexasis.helixmarkers.markers;

import net.pl3x.map.core.markers.Vector;
import net.pl3x.map.core.markers.marker.Icon;
import net.pl3x.map.core.markers.marker.Marker;
import net.pl3x.map.core.markers.option.Options;
import net.pl3x.map.core.markers.option.Popup;
import net.pl3x.map.core.markers.option.Tooltip;
import org.intellij.lang.annotations.Language;

public class MarkerBuilder {

    private final Marker<?> marker;
    private final Options options;

    private MarkerBuilder(Marker<?> marker) {
        this.marker = marker;
        this.options = new Options();
    }

    @SuppressWarnings("unused")
    public Marker<?> build() {
        marker.setOptions(options);
        return marker;
    }

    @SuppressWarnings("unused")
    public static MarkerBuilder newIconMarker(String key, String icon, int x, int z) {
        Icon iconMarker = new Icon(key, x, z, icon);
        return new MarkerBuilder(iconMarker);
    }

    @SuppressWarnings("unused")
    public MarkerBuilder centerIcon(int width, int height) {
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

    @SuppressWarnings("unused")
    public MarkerBuilder addTooltip(@Language("HTML") String content) {
        options.setTooltip(
            new Tooltip(content)
        );
        return this;
    }

    @SuppressWarnings("unused")
    public MarkerBuilder addPopup(@Language("HTML") String content) {
        options.setPopup(
                new Popup(content)
                    .setShouldAutoClose(false)
                    .setShouldKeepInView(true)
                    .setCloseButton(true)
        );
        return this;
    }

}
