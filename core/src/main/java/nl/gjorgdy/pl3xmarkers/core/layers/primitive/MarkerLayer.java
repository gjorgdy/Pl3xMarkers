package nl.gjorgdy.pl3xmarkers.core.layers.primitive;

import net.pl3x.map.core.markers.layer.WorldLayer;
import net.pl3x.map.core.world.World;
import nl.gjorgdy.pl3xmarkers.core.markers.MarkerBuilder;
import org.intellij.lang.annotations.Language;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public abstract class MarkerLayer<T> extends WorldLayer {

    public final String worldIdentifier;

    public MarkerLayer(String key, String label, @NotNull World world, int priority) {
        super(key, world, () -> label);
        worldIdentifier = world.getKey();
        setPriority(priority);
    }

    /**
     * Load previously created markers
     */
    abstract public void load();

    final public String toMarkerKey(int x, int y, int z) {
        return x + ":" + y + ":" + z;
    }

    public abstract MarkerBuilder<?> createBuilder(T object);

    public void loadMarker(T markerEntity) {
        var builder = createBuilder(markerEntity);
        if (builder == null) {
            return;
        }
        // Try to add pop-up
        @Language("HTML") var popup = createPopup(markerEntity);
        if (popup != null) {
            builder = builder.addPopup(popup);
        }
        boolean addedPermanentTooltip = false;
        // Try to add permanent bottom tooltip
        @Language("HTML") var permanentBottomTooltip = createPermanentBottomTooltip(markerEntity);
        if (permanentBottomTooltip != null) {
            builder = builder.addPermanentBottomTooltip(permanentBottomTooltip);
            addedPermanentTooltip = true;
        }
        // Try to add permanent centered tooltip
        @Language("HTML") var permanentCenteredTooltip = createPermanentCenteredTooltip(markerEntity);
        if (permanentCenteredTooltip != null && !addedPermanentTooltip) {
            builder = builder.addPermanentCenteredTooltip(permanentCenteredTooltip);
            addedPermanentTooltip = true;
        }
        // Try to add tooltip
        @Language("HTML") var tooltip = createTooltip(markerEntity);
        if (tooltip != null && !addedPermanentTooltip) {
            builder = builder.addTooltip(tooltip);
        }
        // create the marker
        super.addMarker(builder.build());
    }

    /**
     * Return something other than {@code null} to give this marker a pop-up
     *
     * @param object the object to construct a marker for
     * @return the HTML content for the pop-up, or {@code null} if none
     */
    @Nullable
    @Language("HTML")
    protected String createPopup(T object) {
        return null;
    }

    /**
     * Return something other than {@code null} to give this marker a tooltip
     *
     * @param object the object to construct a marker for
     * @return the HTML content for the tooltip, or {@code null} if none
     */
    @Nullable
    @Language("HTML")
    protected String createTooltip(T object) {
        return null;
    }

    /**
     * Return something other than {@code null} to give this marker a permanent centered tooltip
     *
     * @param object the object to construct a marker for
     * @return the HTML content for the permanent centered tooltip, or {@code null} if none
     */
    @Nullable
    @Language("HTML")
    protected String createPermanentCenteredTooltip(T object) {
        return null;
    }

    /**
     * Return something other than {@code null} to give this marker a permanent bottom tooltip
     *
     * @param object the object to construct a marker for
     * @return the HTML content for the permanent bottom tooltip, or {@code null} if none
     */
    @Nullable
    @Language("HTML")
    protected String createPermanentBottomTooltip(T object) {
        return null;
    }

}
