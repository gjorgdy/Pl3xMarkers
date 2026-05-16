package nl.gjorgdy.pl3xmarkers.core.markers;

import net.pl3x.map.core.markers.Point;
import net.pl3x.map.core.markers.marker.Marker;
import net.pl3x.map.core.markers.option.Options;
import net.pl3x.map.core.markers.option.Popup;
import net.pl3x.map.core.markers.option.Tooltip;
import org.intellij.lang.annotations.Language;

public class MarkerBuilder<T extends Marker<T>> {

    protected final Marker<T> marker;
    protected final Options options;

    protected MarkerBuilder(Marker<T> marker) {
        this.marker = marker;
        options = new Options();
    }

    @SuppressWarnings("unused")
    public Marker<?> build() {
        marker.setOptions(options);
        return marker;
    }

    @SuppressWarnings("unused")
    public MarkerBuilder<T> addTooltip(@Language("HTML") String content) {
        options.setTooltip(
            new Tooltip(content)
        );
        return this;
    }

    @SuppressWarnings({ "unused", "UnusedReturnValue" })
    public MarkerBuilder<T> addPermanentBottomTooltip(@Language("HTML") String content) {
        options.setTooltip(
                new Tooltip(content)
                        .setPermanent(true)
                        .setPane("nameplates")
                        .setDirection(Tooltip.Direction.BOTTOM)
                        .setOffset(Point.of(0, 5))
        );
        return this;
    }

    @SuppressWarnings({ "unused", "UnusedReturnValue" })
    public MarkerBuilder<T> addPermanentTooltip(@Language("HTML") String content) {
        options.setTooltip(
                new Tooltip(content)
                        .setPermanent(true)
                        .setPane("nameplates")
                        .setDirection(Tooltip.Direction.CENTER)
        );
        return this;
    }

    @SuppressWarnings("unused")
    public MarkerBuilder<T> addPopup(@Language("HTML") String content) {
        options.setPopup(
                new Popup(content)
                        .setShouldAutoClose(true)
                        .setShouldKeepInView(false)
                    .setCloseButton(true)
                        .setAutoPanPadding(Point.of(40, 40))
        );
        return this;
    }

}
