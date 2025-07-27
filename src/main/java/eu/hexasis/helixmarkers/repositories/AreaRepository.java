package eu.hexasis.helixmarkers.repositories;

import eu.hexasis.helixmarkers.Database;
import eu.hexasis.helixmarkers.HelixMarkers;
import eu.hexasis.helixmarkers.objects.Position;
import eu.hexasis.helixmarkers.entities.AreaEntity;
import eu.hexasis.helixmarkers.entities.AreaPointEntity;
import org.jetbrains.annotations.Nullable;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class AreaRepository{

    private final Database database;

    public AreaRepository(Database database) {
        this.database = database;
    }

    public List<AreaEntity> getAreas(String world) {
        try {
            return database.areas.queryBuilder()
                    .where().eq("world", world)
                    .query();
        } catch (SQLException e) {
            HelixMarkers.LOGGER.error(e.getMessage());
            return new ArrayList<>();
        }
    }

    @Nullable
    public AreaEntity getArea(String world, String label, int color) {
        try {
            return database.areas.queryBuilder()
                    .where()
                    .eq("world", world).and()
                    .eq("label", label).and()
                    .eq("color", color)
                    .queryForFirst();
        } catch (SQLException e) {
            HelixMarkers.LOGGER.error(e.getMessage());
            return null;
        }
    }

    public AreaEntity getOrCreateArea(String world, String label, int color) {
        AreaEntity area = getArea(world, label, color);
        if (area == null) {
            area = new AreaEntity(world, label, color);
            try {
                database.areas.create(area);
            } catch (SQLException e) {
                HelixMarkers.LOGGER.error(e.getMessage());
                return null;
            }
        }
        return area;
    }

    public boolean addPoint(String world, String label, int color, Position point) {
        AreaEntity area = getOrCreateArea(world, label, color);
        if (area == null) return false;
        var points = area.getPoints();
        if (points == null) return false;
        return points.add(new AreaPointEntity(area, point.X(), point.Z()));
    }

    public boolean removePoint(String world, String label, int color, Position point) {
        AreaEntity area = getOrCreateArea(world, label, color);
        if (area == null) return false;
        var points = area.getPoints();
        if (points == null) return false;
        return points.removeIf(
        p -> p.getX() == point.X()
                && p.getZ() == point.Z()
        );
    }

}
