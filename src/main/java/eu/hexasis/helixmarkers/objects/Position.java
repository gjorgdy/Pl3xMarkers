package eu.hexasis.helixmarkers.objects;

import org.jetbrains.annotations.NotNull;

public record Position (
    int X,
    int Z
) implements Comparable<Position> {

    public double distance(@NotNull Position other) {
        return Math.sqrt(Math.pow(this.X - other.X, 2) + Math.pow(this.Z - other.Z, 2));
    }

    @Override
    public int compareTo(@NotNull Position other) {
        return (this.X - other.X) + (this.Z - other.Z);
    }

}
