package nl.gjorgdy.pl3xmarkers.layers.primitive;

import nl.gjorgdy.pl3xmarkers.Pl3xMarkersCore;
import nl.gjorgdy.pl3xmarkers.markers.AreaMarkerBuilder;
import nl.gjorgdy.pl3xmarkers.entities.AreaEntity;
import net.pl3x.map.core.world.World;
import org.intellij.lang.annotations.Language;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;

public class AreaMarkerLayer extends MarkerLayer {

    public AreaMarkerLayer(String key, String label, @NotNull World world) {
        super(key, label, world);
    }

    @Override
    public void load() {
        Pl3xMarkersCore.areaRepository()
            .getAreas(getWorld().getKey())
            .forEach(this::loadArea);
    }

    @Override
    public boolean isInWorld(@NotNull World world) {
        return true;
    }

    public void loadArea(AreaEntity area) {
        if (super.hasMarker(area.getKey())) {
            super.removeMarker(area.getKey());
        }
        var points = area.getPoints();
        if (points != null && !points.isEmpty()) {
            super.addMarker(
                AreaMarkerBuilder
                    .newAreaMarker(area.getKey(), new ArrayList<>(points))
                    .fill(area.getColor())
                    .stroke(area.getColor())
                    .addPopup(area.getLabel())
            );
        }
    }

    /**
     * Add and store a new marker
     */
    public void addPoint(@Language("HTML") String label, int color, int x, int z) {
        boolean added = Pl3xMarkersCore.areaRepository().addPoint(getWorld().getKey(), label, color, x, z);
        if (added) {
            // reload the area
            AreaEntity area = Pl3xMarkersCore.areaRepository().getArea(getWorld().getKey(), label, color);
            if (area != null) loadArea(area);
        }
    }

    /**
     * Remove a marker
     */
    public void removePoint(@Language("HTML") String label, int color, int x, int z) {
        boolean removed = Pl3xMarkersCore.areaRepository().removePoint(getWorld().getKey(), label, color, x, z);
        if (removed) {
            // reload the area
            AreaEntity area = Pl3xMarkersCore.areaRepository().getArea(getWorld().getKey(), label, color);
            if (area != null) loadArea(area);
        }
    }

}