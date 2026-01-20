package nl.gjorgdy.pl3xmarkers.core.layers.primitive;

import net.pl3x.map.core.world.World;
import nl.gjorgdy.pl3xmarkers.core.Pl3xMarkersCore;
import nl.gjorgdy.pl3xmarkers.core.helpers.HtmlHelper;
import nl.gjorgdy.pl3xmarkers.core.interfaces.entities.INamedIconMarker;
import nl.gjorgdy.pl3xmarkers.core.markers.IconMarkerBuilder;
import org.intellij.lang.annotations.Language;
import org.jetbrains.annotations.NotNull;

public class NamedIconMarkerLayer extends MarkerLayer {

    public final String iconId;
    public final String key;
    public final String label;
    @Language("HTML")
    public final String tooltip;

    public NamedIconMarkerLayer(String icon, String key, String label, @Language("HTML") String tooltip, @NotNull World world, int priority) {
        super(key, label, world, priority);
        this.iconId = icon;
        this.key = key;
        this.label = label;
        this.tooltip = tooltip;
    }

    @Override
    public void load() {
        Pl3xMarkersCore.storage()
			.getNamedIconMarkerRepository()
            .getNamedIconMarkers(getWorld().getKey(), key)
            .forEach(this::addMarker);
    }

    /**
     * Rename a marker
     *
     * @param x x coordinate of marker
     * @param z z coordinate of marker
     * @param newName new name of the marker
     */
    public boolean renameMarker(int x, int z, @Language("HTML") String newName) {
        return Pl3xMarkersCore.storage()
            .getNamedIconMarkerRepository()
            .renameIconMarker(getWorld().getKey(), key, x, z, newName);
    }

    /**
     * Add and store a new marker
     *
     * @param x x coordinate of marker
     * @param z z coordinate of marker
     * @param name name of the marker
     */
    public boolean createMarker(int x, int z, @Language("HTML") String name) {
        var marker = Pl3xMarkersCore.storage()
			.getNamedIconMarkerRepository()
            .createNamedIconMarker(getWorld().getKey(), key, x, z, name);
        if (marker != null) {
            addMarker(marker);
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
			.getNamedIconMarkerRepository()
            .removeNamedIconMarker(getWorld().getKey(), key, x, z);
        if (removed) super.removeMarker(toMarkerKey(x, z));
		return removed;
    }

    protected <T extends INamedIconMarker> void addMarker(T markerEntity) {
        var marker = IconMarkerBuilder.newIconMarker(
                markerEntity.getKey(),
                iconId,
                markerEntity.getLocation().getX(),
                markerEntity.getLocation().getZ()
            )
            .centerIcon(16, 16)
            .addTooltip(HtmlHelper.sanitize(markerEntity.getName()))
            .build();
        addMarker(marker);
    }

}