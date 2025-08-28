package eu.hexasis.helixmarkers.layers.primitive;

import eu.hexasis.helixmarkers.HelixMarkers;
import eu.hexasis.helixmarkers.markers.AreaMarkerBuilder;
import eu.hexasis.helixmarkers.entities.AreaEntity;
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
        HelixMarkers.areaRepository()
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
        boolean added = HelixMarkers.areaRepository().addPoint(getWorld().getKey(), label, color, x, z);
        if (added) {
            // reload the area
            AreaEntity area = HelixMarkers.areaRepository().getArea(getWorld().getKey(), label, color);
            if (area != null) loadArea(area);
        }
    }

    /**
     * Remove a marker
     */
    public void removePoint(@Language("HTML") String label, int color, int x, int z) {
        boolean removed = HelixMarkers.areaRepository().removePoint(getWorld().getKey(), label, color, x, z);
        if (removed) {
            // reload the area
            AreaEntity area = HelixMarkers.areaRepository().getArea(getWorld().getKey(), label, color);
            if (area != null) loadArea(area);
        }
    }

}