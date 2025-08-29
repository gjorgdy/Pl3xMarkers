package nl.gjorgdy.pl3xmarkers.entities;

import org.jetbrains.annotations.Nullable;

import java.util.Collection;
import java.util.List;

public class AreaEntity {

    private int id;

    private String world;

    private String label;

    private int color;

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
    public Collection<AreaPointEntity> getPoints() {
        return List.of();
    }
}