package nl.gjorgdy.pl3xmarkers.entities;

public class IconMarkerEntity {

    private String world;

    private String layer;

    private int x;

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