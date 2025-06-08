package eu.hexasis.helixmarkers.objects;

import java.util.List;

public record Area(
    String world,
    String label,
    int color,
    List<Position> points
) {

    public void addPoint(int x, int z) {
        points.add(new Position(x, z));
    }

    public String Key() {
        return label + "," + color;
    }

}
