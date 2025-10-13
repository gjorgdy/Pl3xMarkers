package nl.gjorgdy.pl3xmarkers.repositories;

import nl.gjorgdy.pl3xmarkers.Database;
import nl.gjorgdy.pl3xmarkers.entities.AreaEntity;
import nl.gjorgdy.pl3xmarkers.entities.AreaPointEntity;
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
            System.out.println(e.getMessage());
//            Pl3xMarkersCore.LOGGER.error(e.getMessage());
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
            System.out.println(e.getMessage());
//            Pl3xMarkersCore.LOGGER.error(e.getMessage());
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
                System.out.println(e.getMessage());
//            Pl3xMarkersCore.LOGGER.error(e.getMessage());
                return null;
            }
        }
        return area;
    }

    public boolean addPoint(String world, String label, int color, int x, int z) {
        AreaEntity area = getOrCreateArea(world, label, color);
        if (area == null) return false;
        var points = area.getPoints();
        if (points == null) return false;
        return points.add(new AreaPointEntity(area, x, z));
    }

    public boolean removePoint(String world, String label, int color, int x, int z) {
        AreaEntity area = getOrCreateArea(world, label, color);
        if (area == null) return false;
        var points = area.getPoints();
        if (points == null) return false;
        return points.removeIf(
        p -> p.getX() == x
                && p.getZ() == z
        );
    }

}
