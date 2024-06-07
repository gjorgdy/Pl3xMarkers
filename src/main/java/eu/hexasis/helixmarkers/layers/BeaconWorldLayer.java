package eu.hexasis.helixmarkers.layers;

import eu.hexasis.helixmarkers.HelixMarkers;
import net.pl3x.map.core.markers.marker.Icon;
import net.pl3x.map.core.markers.marker.Marker;
import net.pl3x.map.core.world.World;
import org.jetbrains.annotations.NotNull;

import java.sql.PreparedStatement;
import java.sql.SQLException;

public class BeaconWorldLayer extends SimpleWorldLayer {

    public BeaconWorldLayer(@NotNull World world) {
        super("beacon", "beacons", "Beacons", "Beacon", world);
    }

    @Override
    protected void load() {

    }

    @Override
    public void addMarker(int x, int z) {
        String query = """
                    INSERT INTO markers VALUES (?, ?, ?, ?)
                """;
        try (PreparedStatement ps = HelixMarkers.SQLITE.prepareStatement(query)) {
//            ps.setString();
        } catch (SQLException e) {
            HelixMarkers.LOGGER.error(e.getMessage());
        }
        if (hasMarker(x, z)) return;
        Marker<Icon> marker = createMarker(x, z);
        addMarker(marker);
    }

    @Override
    public void removeMarker(int x, int z) {
        super.removeMarker(x + ":" + z);
    }

}