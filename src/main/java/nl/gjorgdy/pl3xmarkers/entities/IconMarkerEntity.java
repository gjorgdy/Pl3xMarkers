package nl.gjorgdy.pl3xmarkers.entities;

import com.j256.ormlite.dao.ForeignCollection;
import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.field.ForeignCollectionField;
import com.j256.ormlite.table.DatabaseTable;
import org.jetbrains.annotations.Nullable;

@DatabaseTable(tableName = "icon_markers")
public class IconMarkerEntity {

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

    @ForeignCollectionField(columnName = "additions", eager = true)
    private ForeignCollection<IconMarkerAdditionEntity> additions;

    @SuppressWarnings("unused") // used by ormlite
    public IconMarkerEntity() {}

    public IconMarkerEntity(String world, String layer, int x, int z) {
        this.world = world;
        this.layer = layer;
        this.x = x;
        this.z = z;
    }

    @Nullable
    public IconMarkerAdditionEntity getAdditions() {
        if (additions == null) return null;
        return additions.stream().findFirst().orElse(null);
    }

    @Nullable
    public IconMarkerAdditionEntity getAdditionsOrEmpty() {
        var additions = getAdditions();
        return additions == null ? new IconMarkerAdditionEntity() : additions;
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