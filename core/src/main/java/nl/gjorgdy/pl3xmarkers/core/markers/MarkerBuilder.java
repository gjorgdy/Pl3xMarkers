package nl.gjorgdy.pl3xmarkers.core.markers;

import net.pl3x.map.core.markers.marker.Marker;
import net.pl3x.map.core.markers.option.Options;
import net.pl3x.map.core.markers.option.Popup;
import net.pl3x.map.core.markers.option.Stroke;
import net.pl3x.map.core.markers.option.Tooltip;
import org.intellij.lang.annotations.Language;

public abstract class MarkerBuilder<T extends Marker<T>> {

    protected final Marker<T> marker;
    protected final Options options;

    protected MarkerBuilder(Marker<T> marker) {
        this.marker = marker;
        this.options = new Options();
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

    @SuppressWarnings("unused")
    public MarkerBuilder<T> addPopup(@Language("HTML") String content) {
        options.setPopup(
                new Popup(content)
                    .setShouldAutoClose(false)
                    .setShouldKeepInView(true)
                    .setCloseButton(true)
        );
        return this;
    }

	public MarkerBuilder<T> stroke(int color) {
		return stroke(color, 2);
	}

	public MarkerBuilder<T> stroke(int color, int weight) {
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
