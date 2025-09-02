package nl.gjorgdy.pl3xmarkers.entities;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

@DatabaseTable(tableName = "icon_markers_additions")
public class IconMarkerAdditionEntity {

    @DatabaseField(columnName = "marker_id", foreign = true, foreignAutoRefresh = true)
    private IconMarkerEntity marker;

    @DatabaseField(columnName = "label")
    private String label;

    @DatabaseField(columnName = "color")
    private int color;

    @SuppressWarnings("unused") // used by ormlite
    public IconMarkerAdditionEntity() {}

    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
    }

    public int getColor() {
        return color;
    }

    public void setColor(int color) {
        this.color = color;
    }

}