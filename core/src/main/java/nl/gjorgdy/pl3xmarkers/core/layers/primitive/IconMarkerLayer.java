package nl.gjorgdy.pl3xmarkers.core.layers.primitive;

import nl.gjorgdy.pl3xmarkers.core.Pl3xMarkersCore;
import nl.gjorgdy.pl3xmarkers.core.markers.IconMarkerBuilder;
import net.pl3x.map.core.markers.marker.Marker;
import net.pl3x.map.core.world.World;
import org.intellij.lang.annotations.Language;
import org.jetbrains.annotations.NotNull;

public class IconMarkerLayer extends MarkerLayer {

    public final String iconId;
    public final String key;
    public final String label;
    @Language("HTML")
    public final String tooltip;

    public IconMarkerLayer(String icon, String key, String label, @Language("HTML") String tooltip, @NotNull World world) {
        super(key, label, world);
        this.iconId = icon;
        this.key = key;
        this.label = label;
        this.tooltip = tooltip;
    }

    @Override
    public void load() {
        Pl3xMarkersCore.storage()
			.getIconMarkerRepository()
            .getIconMarkers(getWorld().getKey(), key)
            .forEach(marker ->
                addIconMarker(marker.getLocation().getX(), marker.getLocation().getZ())
            );
    }

    /**
     * Add and store a new marker
     *
     * @param x x coordinate of marker
     * @param z z coordinate of marker
     */
    public boolean addSimpleMarker(int x, int z) {
        var marker = Pl3xMarkersCore.storage()
			.getIconMarkerRepository()
            .createIconMarker(getWorld().getKey(), key, x, z);
        if (marker != null) {
            addIconMarker(x, z);
			return true;
        }
		return false;
    }

    /**
     * Remove a marker
     *
     * @param x x coordinate of marker
     * @param z z coordinate of marker
     */
    public boolean removeMarker(int x, int z) {
		var removed = Pl3xMarkersCore.storage()
			.getIconMarkerRepository()
            .removeIconMarker(getWorld().getKey(), key, x, z);
        if (removed) super.removeMarker(toMarkerKey(x, z));
		return removed;
    }

    private void addIconMarker(int x, int z) {
        addMarker(createIconMarker(x, z));
    }

    protected Marker<?> createIconMarker(int x, int z) {
        return IconMarkerBuilder.newIconMarker(
                toMarkerKey(x, z),
                iconId,
                x, z
            )
            .centerIcon(16, 16)
            .addTooltip(tooltip)
            .build();
    }

}