package eu.hexasis.helixmarkers.tables;

import com.j256.ormlite.dao.ForeignCollection;
import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.field.ForeignCollectionField;
import com.j256.ormlite.table.DatabaseTable;
import org.jetbrains.annotations.Nullable;

@DatabaseTable(tableName = "areas")
public class AreaEntity {

    @DatabaseField(columnName = "id", generatedId = true)
    private int id;

    @DatabaseField(columnName = "world", uniqueCombo = true)
    private String world;

    @DatabaseField(columnName = "label", uniqueCombo = true)
    private String label;

    @DatabaseField(columnName = "color", uniqueCombo = true)
    private int color;

    @ForeignCollectionField(columnName = "points", eager = true)
    private ForeignCollection<AreaPointEntity> points;

    @SuppressWarnings("unused") // used by ormlite
    public AreaEntity() {}

    public AreaEntity(String world, String label, int color) {
        this.world = world;
        this.label = label;
        this.color = color;
    }

    public String getWorld() {
        return world;
    }

    public String getLabel() {
        return label;
    }

    public int getColor() {
        return color;
    }

    public String getKey() {
        return label + "," + color;
    }

    @Nullable
    public ForeignCollection<AreaPointEntity> getPoints() {
        return points;
    }
}