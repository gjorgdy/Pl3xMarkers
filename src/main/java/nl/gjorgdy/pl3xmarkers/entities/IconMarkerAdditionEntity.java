package nl.gjorgdy.pl3xmarkers.entities;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

@DatabaseTable(tableName = "icon_marker_additions")
public class IconMarkerAdditionEntity {

    @DatabaseField(columnName = "id", generatedId = true, uniqueCombo = true)
    private int id;

    @DatabaseField(columnName = "marker_id", foreign = true, foreignAutoRefresh = true, uniqueCombo = true)
    private IconMarkerEntity marker;

    @DatabaseField(columnName = "name")
    private String name;

    @DatabaseField(columnName = "color")
    private int color;

    @SuppressWarnings("unused") // used by ormlite
    public IconMarkerAdditionEntity() {}

    public IconMarkerAdditionEntity(IconMarkerEntity marker) {
        this.marker = marker;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getColor() {
        return color;
    }

    public void setColor(int color) {
        this.color = color;
    }

}