package eu.hexasis.helixmarkers.layers;

import eu.hexasis.helixmarkers.HelixMarkers;
import eu.hexasis.helixmarkers.markers.IconBuilder;
import net.minecraft.util.math.BlockPos;
import net.pl3x.map.core.markers.marker.Marker;
import net.pl3x.map.core.world.World;
import org.intellij.lang.annotations.Language;
import org.jetbrains.annotations.NotNull;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class SimpleIconMarkerLayer extends MarkerLayer {

    public final String iconId;
    public final String key;
    public final String label;
    @Language("HTML")
    public final String tooltip;

    public SimpleIconMarkerLayer(String icon, String key, String label, @Language("HTML") String tooltip, @NotNull World world) {
        super(key, label, world);
        this.iconId = icon;
        this.key = key;
        this.label = label;
        this.tooltip = tooltip;
    }

    @Override
    public void load() {
        String query = """
                    SELECT * FROM markers WHERE world = ? AND layer = ?
                """;
        try (PreparedStatement ps = HelixMarkers.DATABASE.prepareStatement(query)) {
            ps.setString(1, getWorld().getKey());
            ps.setString(2, key);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                int x = rs.getInt(3);
                int z = rs.getInt(4);
                addMarker(
                    createSimpleMarker(x, z)
                );
            }
        } catch (SQLException e) {
            HelixMarkers.LOGGER.error(e.getMessage());
        }
    }

    /**
     * Add and store a new marker
     *
     * @param pos location of marker
     */
    public void addSimpleMarker(BlockPos pos) {
        String query = """
                    INSERT INTO markers VALUES (?, ?, ?, ?)
                    ON CONFLICT DO NOTHING
                """;
        try (PreparedStatement ps = HelixMarkers.DATABASE.prepareStatement(query)) {
            ps.setString(1, getWorld().getKey());
            ps.setString(2, key);
            ps.setInt(3, pos.getX());
            ps.setInt(4, pos.getZ());
            if (ps.executeUpdate() > 0) {
                super.addMarker(
                    createSimpleMarker(pos.getX(), pos.getZ())
                );
            }
        } catch (SQLException e) {
            HelixMarkers.LOGGER.error(e.getMessage());
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
        String query = """
                    DELETE FROM markers WHERE world = ? AND layer = ? AND x = ? AND z = ?
                """;
        try (PreparedStatement ps = HelixMarkers.DATABASE.prepareStatement(query)) {
            ps.setString(1, getWorld().getKey());
            ps.setString(2, key);
            ps.setInt(3, x);
            ps.setInt(4, z);
            if (ps.executeUpdate() > 0) {
                super.removeMarker(toMarkerKey(x, z));
            }
        } catch (SQLException e) {
            HelixMarkers.LOGGER.error(e.getMessage());
        }
    }

    protected Marker<?> createSimpleMarker(int x, int z) {

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