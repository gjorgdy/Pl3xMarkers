package nl.gjorgdy.pl3xmarkers.repositories;

import nl.gjorgdy.pl3xmarkers.Database;
import nl.gjorgdy.pl3xmarkers.entities.IconMarkerEntity;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class IconMarkerRepository {

    private final Database database;

    public IconMarkerRepository(Database database) {
        this.database = database;
    }

    public List<IconMarkerEntity> getMarkers(String worldKey, String layerKey) {
        try {
            return database.markers.queryBuilder()
                    .where().eq("world", worldKey)
                    .and().eq("layer", layerKey)
                    .query();
        } catch (Exception e) {
            System.out.println(e.getMessage());
//            Pl3xMarkersCore.LOGGER.error(e.getMessage());
        }
        return new ArrayList<>();
    }

    public boolean addMarker(String worldKey, String layerKey, int x, int z) {
        try {
            var marker = new IconMarkerEntity(worldKey, layerKey, x, z);
            return database.markers.create(marker) > 0;
        } catch (SQLException e) {
            if (e.getErrorCode() != 0) {
                System.out.println(e.getMessage());
//            Pl3xMarkersCore.LOGGER.error(e.getMessage());
//                Pl3xMarkersCore.LOGGER.error("[{}] {}", e.getErrorCode(), e.getCause().getMessage());
            }
            return false;
        }
    }

    public void removeMarker(String worldKey, String layerKey, int x, int z) {
        try {
            var deleteBuilder = database.markers.deleteBuilder();
            deleteBuilder.where()
                    .eq("world", worldKey).and()
                    .eq("layer", layerKey).and()
                    .eq("x", x).and()
                    .eq("z", z);
            deleteBuilder.delete();
        } catch (Exception e) {
            System.out.println(e.getMessage());
//            Pl3xMarkersCore.LOGGER.error(e.getMessage());
//            Pl3xMarkersCore.LOGGER.error(e.getMessage());
        }
    }

}
