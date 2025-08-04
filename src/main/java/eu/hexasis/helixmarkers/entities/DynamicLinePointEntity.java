package eu.hexasis.helixmarkers.entities;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;
import net.pl3x.map.core.markers.Point;
import org.jetbrains.annotations.NotNull;

@DatabaseTable(tableName = "dynamic_line_points")
public class DynamicLinePointEntity implements Comparable<DynamicLinePointEntity> {

    @DatabaseField(columnName = "id", generatedId = true)
    private int id;

    @DatabaseField(columnName = "world", uniqueCombo = true)
    private String world;

    @DatabaseField(columnName = "layer", uniqueCombo = true)
    private String layer;

    @DatabaseField(columnName = "x", uniqueCombo = true)
    private int x;

    @DatabaseField(columnName = "z", uniqueCombo = true)
    private int z;

    @DatabaseField(columnName = "directions")
    private byte directions;

    @SuppressWarnings("unused") // used by ormlite
    public DynamicLinePointEntity() {}

    public DynamicLinePointEntity(String world, String layer, int x, int z) {
        this.x = x;
        this.z = z;
    }

    @Override
    public int compareTo(@NotNull DynamicLinePointEntity other) {
        return (this.x - other.x) + (this.z - other.z);
    }

    public int getId() {
        return id;
    }

    public int getX() {
        return x;
    }

    public int getZ() {
        return z;
    }

    public boolean goesNorth() {
        return (directions & 0x01) == 0x01;
    }

    public boolean goesNorthEast() {
        return (directions & 0x02) == 0x02;
    }

    public boolean goesEast() {
        return (directions & 0x04) == 0x04;
    }

    public boolean goesSouthEast() {
        return (directions & 0x08) == 0x08;
    }

    public boolean goesSouth() {
        return (directions & 0x10) == 0x10;
    }

    public boolean goesSouthWest() {
        return (directions & 0x20) == 0x20;
    }

    public boolean goesWest() {
        return (directions & 0x40) == 0x40;
    }

    public boolean goesNorthWest() {
        return (directions & 0x80) == 0x80;
    }

    public Point toPl3xPoint() {
        return new Point(x, z);
    }

    @Override
    public String toString() {
        return x + ":" + z + ":" + directions;
    }
}
