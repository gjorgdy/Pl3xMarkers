package nl.gjorgdy.pl3xmarkers.layers.primitive;

import nl.gjorgdy.pl3xmarkers.Pl3xMarkers;
import nl.gjorgdy.pl3xmarkers.markers.IconMarkerBuilder;
import net.minecraft.util.math.BlockPos;
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
        Pl3xMarkers.iconMarkerRepository()
            .getMarkers(getWorld().getKey(), key)
            .forEach(marker ->
                addIconMarker(marker.getX(), marker.getZ())
            );
    }

    @Override
    public boolean isInWorld(@NotNull World world) {
        return true;
    }

    /**
     * Add and store a new marker
     *
     * @param pos location of marker
     */
    public void addSimpleMarker(BlockPos pos) {
        boolean added = Pl3xMarkers.iconMarkerRepository()
            .addMarker(getWorld().getKey(), key, pos.getX(), pos.getZ());
        if (added) {
            addIconMarker(pos.getX(), pos.getZ());
        }
    }

    /**
     * Remove a marker
     *
     * @param pos location of marker
     */
    public void removeMarker(BlockPos pos) {
        Pl3xMarkers.iconMarkerRepository()
            .removeMarker(getWorld().getKey(), key, pos.getX(), pos.getZ());
        super.removeMarker(toMarkerKey(pos.getX(), pos.getZ()));
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