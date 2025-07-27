package eu.hexasis.helixmarkers.entities;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

@DatabaseTable(tableName = "icon_markers")
public class IconMarkerEntity {

    @DatabaseField(columnName = "world", uniqueCombo = true)
    private String world;

    @DatabaseField(columnName = "layer", uniqueCombo = true)
    private String layer;

    @DatabaseField(columnName = "x", uniqueCombo = true)
    private int x;

    @DatabaseField(columnName = "z", uniqueCombo = true)
    private int z;

    @SuppressWarnings("unused") // used by ormlite
    public IconMarkerEntity() {}

    public IconMarkerEntity(String world, String layer, int x, int z) {
        this.world = world;
        this.layer = layer;
        this.x = x;
        this.z = z;
    }

    public String getWorld() {
        return world;
    }

    public int getX() {
        return x;
    }

    public int getZ() {
        return z;
    }

    @Override
    public String toString() {
        return world + ":" + layer + ":" + x + ":" + z;
    }
}