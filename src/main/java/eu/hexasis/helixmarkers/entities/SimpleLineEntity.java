package eu.hexasis.helixmarkers.entities;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;
import net.pl3x.map.core.markers.Point;

import java.util.List;

@DatabaseTable(tableName = "dynamic_line_points")
public class SimpleLineEntity {

    @DatabaseField(columnName = "id", generatedId = true)
    private int id;

    @DatabaseField(columnName = "world", uniqueCombo = true)
    private String world;

    @DatabaseField(columnName = "layer", uniqueCombo = true)
    private String layer;

    @DatabaseField(columnName = "ax", uniqueCombo = true)
    private int aX;

    @DatabaseField(columnName = "az", uniqueCombo = true)
    private int aZ;

    @DatabaseField(columnName = "bx", uniqueCombo = true)
    private int bX;

    @DatabaseField(columnName = "bz", uniqueCombo = true)
    private int bZ;

    @SuppressWarnings("unused") // used by ormlite
    public SimpleLineEntity() {}

    public SimpleLineEntity(String world, String layer, int aX, int aZ, int bX, int bZ) {
        this.world = world;
        this.layer = layer;
        this.aX = aX;
        this.aZ = aZ;
        this.bX = bX;
        this.bZ = bZ;
    }

    public int getId() {
        return id;
    }

    public List<Point> toPl3xPoints() {
        return List.of(new Point(aX, aZ), new Point(bX, bZ));
    }

    @Override
    public String toString() {
        return aX + ":" + aZ + "-";
    }
}
