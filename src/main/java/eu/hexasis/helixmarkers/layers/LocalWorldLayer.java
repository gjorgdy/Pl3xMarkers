package eu.hexasis.helixmarkers.layers;

import eu.hexasis.helixmarkers.HelixMarkers;
import net.pl3x.map.core.markers.marker.Icon;
import net.pl3x.map.core.markers.marker.Marker;
import net.pl3x.map.core.world.World;
import org.jetbrains.annotations.NotNull;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class LocalWorldLayer extends SimpleWorldLayer {

    public LocalWorldLayer(String icon, String key, String label, String tooltip, @NotNull World world) {
        super(icon, key, label, tooltip, world);
    }

    @Override
    protected void load() {
        String query = """
                    SELECT * FROM markers WHERE world = ? AND layer = ?
                """;
        try (PreparedStatement ps = HelixMarkers.SQLITE.prepareStatement(query)) {
            ps.setString(1, getWorld().getKey());
            ps.setString(2, key);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                Marker<Icon> marker = createMarker(
                    rs.getInt(3),
                    rs.getInt(4)
                );
                addMarker(marker);
            }
        } catch (SQLException e) {
            HelixMarkers.LOGGER.error(e.getMessage());
        }
    }

    @Override
    public void addMarker(int x, int z) {
        String query = """
                    INSERT INTO markers VALUES (?, ?, ?, ?)
                    ON CONFLICT DO NOTHING
                """;
        try (PreparedStatement ps = HelixMarkers.SQLITE.prepareStatement(query)) {
            ps.setString(1, getWorld().getKey());
            ps.setString(2, key);
            ps.setInt(3, x);
            ps.setInt(4, z);
            if (ps.executeUpdate() > 0) {
                Marker<Icon> marker = createMarker(x, z);
                super.addMarker(marker);
            }
        } catch (SQLException e) {
            HelixMarkers.LOGGER.error(e.getMessage());
        }
    }

    @Override
    public void removeMarker(int x, int z) {
        String query = """
                    DELETE FROM markers WHERE world = ? AND layer = ? AND x = ? AND z = ?
                """;
        try (PreparedStatement ps = HelixMarkers.SQLITE.prepareStatement(query)) {
            ps.setString(1, getWorld().getKey());
            ps.setString(2, key);
            ps.setInt(3, x);
            ps.setInt(4, z);
            if (ps.executeUpdate() > 0) {
                super.removeMarker(x + ":" + z);
            }
        } catch (SQLException e) {
            HelixMarkers.LOGGER.error(e.getMessage());
        }
    }

}