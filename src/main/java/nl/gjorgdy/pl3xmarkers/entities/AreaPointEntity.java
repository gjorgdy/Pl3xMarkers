package nl.gjorgdy.pl3xmarkers.entities;

import net.pl3x.map.core.markers.Point;
import org.jetbrains.annotations.NotNull;

public class AreaPointEntity implements Comparable<AreaPointEntity> {

    private int id;

    private AreaEntity area;

    private int x;

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