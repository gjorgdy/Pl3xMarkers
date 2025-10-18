package nl.gjorgdy.pl3xmarkers.core.entities;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;
import net.pl3x.map.core.markers.Point;
import org.jetbrains.annotations.NotNull;

@DatabaseTable(tableName = "area_points")
public class AreaPointEntity implements Comparable<AreaPointEntity> {

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

    public double distance(@NotNull AreaPointEntity other) {
        return Math.sqrt(Math.pow(this.x - other.x, 2) + Math.pow(this.z - other.z, 2));
    }

	public AreaPointEntity add(int dx, int dz) {
		return new AreaPointEntity(this.area, this.x + dx, this.z + dz);
	}

	public AreaPointEntity set(int x, int z) {
		return new AreaPointEntity(this.area, x, z);
	}

    @Override
    public int compareTo(@NotNull AreaPointEntity other) {
        return (this.x - other.x) + (this.z - other.z);
    }

    @Override
    public String toString() {
        return area.getLabel() + ":" + x + ":" + z;
    }

    public Point toPl3xPoint() {
        return new Point(x, z);
    }

}