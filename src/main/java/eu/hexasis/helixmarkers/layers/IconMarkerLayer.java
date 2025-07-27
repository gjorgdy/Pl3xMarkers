package eu.hexasis.helixmarkers.layers;

import eu.hexasis.helixmarkers.HelixMarkers;
import eu.hexasis.helixmarkers.markers.IconBuilder;
import eu.hexasis.helixmarkers.tables.SimpleMarkerEntity;
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
        try {
            var db = HelixMarkers.database();
            db.markers.queryBuilder()
                    .where().eq("world", getWorld().getKey())
                    .and().eq("layer", key)
                    .query().forEach(marker ->
                        addMarker(
                            createIconMarker(marker.getX(), marker.getZ())
                        )
                    );
        } catch (Exception e) {
            HelixMarkers.LOGGER.error(e.getMessage());
        }
    }

    /**
     * Add and store a new marker
     *
     * @param pos location of marker
     */
    public void addSimpleMarker(BlockPos pos) {
        try {
            var db = HelixMarkers.database();
            var marker = new SimpleMarkerEntity(
                this.getWorld().getKey(),
                this.key,
                pos.getX(),
                pos.getZ()
            );
            if (markerExists(pos.getX(), pos.getZ())) return;
            int i = db.markers.create(marker);
            if (i > 0) {
                super.addMarker(
                    createIconMarker(pos.getX(), pos.getZ())
                );
            }
        } catch (Exception e) {
            HelixMarkers.LOGGER.error(e.toString());
        }
    }

    private boolean markerExists(int x, int z) {
        try {
            var db = HelixMarkers.database();
            var queryBuilder = db.markers.queryBuilder();
            queryBuilder.where()
                    .eq("world", getWorld().getKey()).and()
                    .eq("layer", key).and()
                    .eq("x", x).and()
                    .eq("z", z);
            return queryBuilder.countOf() > 0;
        } catch (Exception e) {
            HelixMarkers.LOGGER.error(e.getMessage());
            return false;
        }
    }

    /**
     * Remove a marker
     *
     * @param pos location of marker
     */
    public void removeMarker(BlockPos pos) {
        removeMarker(pos.getX(), pos.getZ());
    }

    /**
     * Remove a marker
     *
     * @param x x-coordinate of marker
     * @param z z-coordinate of marker
     */
    public void removeMarker(int x, int z) {
        try {
            var db = HelixMarkers.database();
            var deleteBuilder = db.markers.deleteBuilder();
            deleteBuilder.where()
                .eq("world", getWorld().getKey()).and()
                .eq("layer", key).and()
                .eq("x", x).and()
                .eq("z", z);
            deleteBuilder.delete();
        } catch (Exception e) {
            HelixMarkers.LOGGER.error(e.getMessage());
        }
    }

    protected Marker<?> createIconMarker(int x, int z) {
        return IconBuilder.newIconMarker(
                toMarkerKey(x, z),
                iconId,
                x, z
            )
            .centerIcon(16, 16)
            .addTooltip(tooltip)
            .build();
    }

}