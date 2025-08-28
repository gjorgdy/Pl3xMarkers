package eu.hexasis.helixmarkers.repositories;

import eu.hexasis.helixmarkers.Database;
import eu.hexasis.helixmarkers.HelixMarkers;
import eu.hexasis.helixmarkers.entities.SimpleLineEntity;
import org.jetbrains.annotations.Nullable;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class LineRepository {

    private final Database database;

    public LineRepository(Database database) {
        this.database = database;
    }

    public List<SimpleLineEntity> getPoints(String world, String layerKey) {
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

    @Nullable
    public SimpleLineEntity addLine(String world, String layer, int aX, int aZ, int bX, int bZ) {
        try {
            var line = new SimpleLineEntity(
                world, layer, aX, aZ, bX, bZ
            );
            var result = database.dynamicLinePoints.create(line);
            return result > 0 ? line : null;
        } catch (SQLException e) {
            HelixMarkers.LOGGER.error(e.getMessage());
            return null;
        }
    }

    public boolean removeLine(String world, String layer, int x, int z) {
        try {
            var db = database.dynamicLinePoints.deleteBuilder();
            db.where()
                .eq("world", world)
                .eq("layer", layer)
                .or(
                    db.where().and(
                        db.where().eq("ax", x),
                        db.where().eq("az", z)
                    ),
                    db.where().and(
                        db.where().eq("bx", x),
                        db.where().eq("bz", z)
                    )
                );
            return db.delete() > 0;
        } catch (SQLException e) {
            HelixMarkers.LOGGER.error(e.getMessage());
            return false;
        }
    }

}
