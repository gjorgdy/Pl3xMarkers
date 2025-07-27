package eu.hexasis.helixmarkers.entities;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

@DatabaseTable(tableName = "area_points")
public class AreaPointEntity {

    @DatabaseField(columnName = "id", generatedId = true)
    private int id;

    @DatabaseField(columnName = "area_id", uniqueCombo = true, foreign = true, foreignAutoRefresh = true)
    private AreaEntity area;

    @DatabaseField(columnName = "x", uniqueCombo = true)
    private int x;

    @DatabaseField(columnName = "z", uniqueCombo = true)
    private int z;

    @SuppressWarnings("unused") // used by ormlite
    public AreaPointEntity() {}

    public AreaPointEntity(AreaEntity area, int x, int z) {
        this.area = area;
        this.x = x;
        this.z = z;
    }

    public int getX() {
        return x;
    }

    public int getZ() {
        return z;
    }

    @Override
    public String toString() {
        return area.getLabel() + ":" + x + ":" + z;
    }
}