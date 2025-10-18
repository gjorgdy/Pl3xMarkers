package nl.gjorgdy.pl3xmarkers.core.repositories;

import nl.gjorgdy.pl3xmarkers.core.Database;
import nl.gjorgdy.pl3xmarkers.core.entities.AreaEntity;
import nl.gjorgdy.pl3xmarkers.core.entities.AreaPointEntity;
import org.jetbrains.annotations.Nullable;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class AreaRepository {

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
            System.err.println(e.getMessage());
            return new ArrayList<>();
        }
    }

    @Nullable
    public AreaEntity getArea(String world, String label, int color) {
        try {
            return database.areas.queryBuilder()
                    .where()
                    .eq("world", world).and()
                    .eq("label", label.replace("'", "")).and()
                    .eq("color", color)
                    .queryForFirst();
        } catch (SQLException e) {
            System.err.println(e.getMessage());
            return null;
        }
    }

    public AreaEntity getOrCreateArea(String world, String label, int color) {
        AreaEntity area = getArea(world, label, color);
        if (area == null) {
            area = new AreaEntity(world, label.replace("'", ""), color);
            try {
                database.areas.create(area);
            } catch (SQLException e) {
                System.err.println(e.getMessage());
                return null;
            }
        }
        return getArea(world, label, color);
    }

    public boolean addPoint(String world, String label, int color, int x, int z) {
        AreaEntity area = getOrCreateArea(world, label, color);
        if (area == null) return false;
		try {
			return database.areaPoints.create(new AreaPointEntity(area, x, z)) > 0;
		} catch (SQLException e) {
			System.err.println(e.getMessage());
			return false;
		}
    }

    public boolean removePoint(String world, String label, int color, int x, int z) {
        AreaEntity area = getArea(world, label, color);
        if (area == null) return false;
		try {
			var deleteBuilder = database.areaPoints.deleteBuilder();
			deleteBuilder
				.where()
				.eq("area_id", area.getId()).and()
				.eq("x", x).and()
				.eq("z", z);
			deleteBuilder.delete();
			return true;
		} catch (SQLException e) {
			System.err.println(e.getMessage());
			return false;
		}
    }

}
