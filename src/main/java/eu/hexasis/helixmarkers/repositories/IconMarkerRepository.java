package eu.hexasis.helixmarkers.repositories;

import eu.hexasis.helixmarkers.Database;
import eu.hexasis.helixmarkers.HelixMarkers;
import eu.hexasis.helixmarkers.tables.IconMarkerEntity;

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
            HelixMarkers.LOGGER.error(e.getMessage());
        }
        return new ArrayList<>();
    }

    private boolean markerExists(String worldKey, String layerKey, int x, int z) {
        try {
            var queryBuilder = database.markers.queryBuilder();
            queryBuilder.where()
                .eq("world", worldKey).and()
                .eq("layer", layerKey).and()
                .eq("x", x).and()
                .eq("z", z);
            return queryBuilder.countOf() > 0;
        } catch (Exception e) {
            HelixMarkers.LOGGER.error(e.getMessage());
            return false;
        }
    }

    public boolean addMarker(String worldKey, String layerKey, int x, int z) {
        try {
            var db = HelixMarkers.database();
            var marker = new IconMarkerEntity(worldKey, layerKey, x, z);
            if (markerExists(worldKey, layerKey, x, z)) return false;
            return db.markers.create(marker) > 0;
        } catch (Exception e) {
            HelixMarkers.LOGGER.error(e.toString());
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
            HelixMarkers.LOGGER.error(e.getMessage());
        }
    }

}
