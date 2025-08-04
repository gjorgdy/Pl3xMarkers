package eu.hexasis.helixmarkers.repositories;

import eu.hexasis.helixmarkers.Database;
import eu.hexasis.helixmarkers.HelixMarkers;
import eu.hexasis.helixmarkers.entities.DynamicLinePointEntity;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class DynamicLineRepository {

    private final Database database;

    public DynamicLineRepository(Database database) {
        this.database = database;
    }

    public List<DynamicLinePointEntity> getPoints(String world, String layerKey) {
        try {
            return database.dynamicLinePoints.queryBuilder()
                    .where().eq("world", world)
                    .and().eq("layer", layerKey)
                    .query();
        } catch (SQLException e) {
            HelixMarkers.LOGGER.error(e.getMessage());
            return new ArrayList<>();
        }
    }

    public boolean addOrUpdatePoint(String world, String layer, int x, int z) {
        try {
            var point = new DynamicLinePointEntity(
                world, layer, x, z
            );
            var result = database.dynamicLinePoints.createOrUpdate(point);
            return result.isUpdated() || result.isCreated();
        } catch (SQLException e) {
            HelixMarkers.LOGGER.error(e.getMessage());
            return false;
        }
    }

    public boolean removePoint(String world, String layer, int x, int z) {
        try {
            var deleteBuilder = database.dynamicLinePoints.deleteBuilder();
            deleteBuilder.where()
                .eq("world", world)
                .eq("layer", layer)
                .eq("x", x)
                .eq("z", z);
            return deleteBuilder.delete() > 0;
        } catch (SQLException e) {
            HelixMarkers.LOGGER.error(e.getMessage());
            return false;
        }
    }

}
