package eu.hexasis.helixmarkers.layers;

import eu.hexasis.helixmarkers.HelixMarkers;
import eu.hexasis.helixmarkers.markers.IconMarkerBuilder;
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
        HelixMarkers.iconMarkerRepository()
            .getMarkers(getWorld().getKey(), key)
            .forEach(marker ->
                addIconMarker(marker.getX(), marker.getZ())
            );
    }

    /**
     * Add and store a new marker
     *
     * @param pos location of marker
     */
    public void addSimpleMarker(BlockPos pos) {
        boolean added = HelixMarkers.iconMarkerRepository()
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
        HelixMarkers.iconMarkerRepository()
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