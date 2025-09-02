package nl.gjorgdy.pl3xmarkers.repositories;

import nl.gjorgdy.pl3xmarkers.Database;
import nl.gjorgdy.pl3xmarkers.Pl3xMarkers;
import nl.gjorgdy.pl3xmarkers.entities.IconMarkerEntity;
import org.jetbrains.annotations.Nullable;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class IconMarkerRepository {

    protected final Database database;

    public IconMarkerRepository(Database database) {
        this.database = database;
    }

    public List<IconMarkerEntity> getMarkers(String worldKey, String layerKey) {
        try {
            return database.iconMarkers.queryBuilder()
                    .where().eq("world", worldKey)
                    .and().eq("layer", layerKey)
                    .query();
        } catch (Exception e) {
            Pl3xMarkers.LOGGER.error(e.getMessage());
        }
        return new ArrayList<>();
    }

    @Nullable
    public IconMarkerEntity getMarker(String worldKey, String layerKey, int x, int z) {
        try {
            var queryBuilder = database.iconMarkers.queryBuilder();
            queryBuilder.where()
                    .eq("world", worldKey).and()
                    .eq("layer", layerKey).and()
                    .eq("x", x).and()
                    .eq("z", z);
            return queryBuilder.queryForFirst();
        } catch (Exception e) {
            Pl3xMarkers.LOGGER.error(e.getMessage());
            return null;
        }
    }

    public IconMarkerEntity addMarker(String worldKey, String layerKey, int x, int z) {
        try {
            var marker = new IconMarkerEntity(worldKey, layerKey, x, z);
            return database.iconMarkers.create(marker) > 0 ? marker : null;
        } catch (SQLException e) {
            if (e.getErrorCode() != 0) {
                Pl3xMarkers.LOGGER.error("[{}] {}", e.getErrorCode(), e.getCause().getMessage());
            }
            return null;
        }
    }

    public void removeMarker(String worldKey, String layerKey, int x, int z) {
        try {
            var deleteBuilder = database.iconMarkers.deleteBuilder();
            deleteBuilder.where()
                    .eq("world", worldKey).and()
                    .eq("layer", layerKey).and()
                    .eq("x", x).and()
                    .eq("z", z);
            deleteBuilder.delete();
        } catch (Exception e) {
            Pl3xMarkers.LOGGER.error(e.getMessage());
        }
    }

}
