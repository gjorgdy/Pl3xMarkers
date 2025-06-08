package eu.hexasis.helixmarkers.repositories;

import eu.hexasis.helixmarkers.HelixMarkers;
import eu.hexasis.helixmarkers.objects.Area;
import eu.hexasis.helixmarkers.objects.Position;
import org.intellij.lang.annotations.Language;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class AreaRepository{

    private final Connection db;

    public AreaRepository(Connection sqlConnection) {
        this.db = sqlConnection;
    }

    public List<Area> getAreas(String world) {
        String query = """
                    SELECT * FROM areas WHERE world = ?
                    ORDER BY world, label, color, x, z
                """;
        try (PreparedStatement ps = db.prepareStatement(query)) {
            ps.setString(1, world);
            ResultSet rs = ps.executeQuery();
            List<Area> areas = new ArrayList<>();
            while (rs.next()) {
                var _world = rs.getString("world");
                var _label = rs.getString("label");
                var _color = rs.getInt("color");
                // if different area
                if (areas.isEmpty() || !areas.getLast().label().equals(_label) || areas.getLast().color() != _color) {
                    areas.add(new Area(_world, _label, _color, new ArrayList<>()));
                }
                // add point
                areas.getLast().addPoint(
                    rs.getInt("x"),
                    rs.getInt("z")
                );
            }
            return areas;
        } catch (SQLException e) {
            HelixMarkers.LOGGER.error(e.getMessage());
            return null;
        }
    }

    public Area getArea(String world, String label, int color) {
        String query = """
                    SELECT * FROM areas WHERE world = ? AND label = ? AND color = ?
                    ORDER BY x, z
                """;
        try (PreparedStatement ps = db.prepareStatement(query)) {
            ps.setString(1, world);
            ps.setString(2, label);
            ps.setInt(3, color);
            ResultSet rs = ps.executeQuery();
            var area = new Area(world, label, color, new ArrayList<>());
            while (rs.next()) {
                area.addPoint(
                    rs.getInt("x"),
                    rs.getInt("z")
                );
            }
            return area;
        } catch (SQLException e) {
            HelixMarkers.LOGGER.error(e.getMessage());
            return null;
        }
    }

    public boolean addPoint(String world, String label, int color, Position point) {
        @Language("SQL") String query = """
                    INSERT INTO areas VALUES (?, ?, ?, ?, ?)
                """;
        return ExecuteUpdate(world, label, color, point, query);
    }

    public boolean removePoint(String world, String label, int color, Position point) {
        @Language("SQL") String query = """
                    DELETE FROM areas WHERE world = ? AND label = ? AND color = ? AND x = ? AND z = ?
                """;
        return ExecuteUpdate(world, label, color, point, query);
    }

    private boolean ExecuteUpdate(String world, String label, int color, Position point, @Language("SQL")  String query) {
        try (PreparedStatement ps = db.prepareStatement(query)) {
            ps.setString(1, world);
            ps.setString(2, label);
            ps.setInt(3, color);
            ps.setInt(4, point.X());
            ps.setInt(5, point.Z());
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            HelixMarkers.LOGGER.error(e.getMessage());
            return false;
        }
    }

}
