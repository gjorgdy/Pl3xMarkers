package eu.hexasis.helixmarkers.entities;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

@DatabaseTable(tableName = "fancy_icon_markers")
public class FancyIconMarkerEntity extends IconMarkerEntity {

    @DatabaseField(columnName = "label")
    private String label;

    @DatabaseField(columnName = "color")
    private int color;

    @SuppressWarnings("unused") // used by ormlite
    public FancyIconMarkerEntity() {}

    public FancyIconMarkerEntity(String label, int color, String world, String layer, int x, int z) {
        super(world, layer, x, z);
        this.label = label;
        this.color = color;
    }

    public String getLabel() {
        return label;
    }

    public int getColor() {
        return color;
    }
}