package eu.hexasis.helixmarkers.layers;

import eu.hexasis.helixmarkers.HelixMarkers;
import eu.hexasis.helixmarkers.markers.AreaMarkerBuilder;
import eu.hexasis.helixmarkers.objects.Position;
import eu.hexasis.helixmarkers.tables.AreaEntity;
import net.pl3x.map.core.world.World;
import org.intellij.lang.annotations.Language;
import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.stream.Collectors;

public class AreaMarkerLayer extends MarkerLayer {

    public final String worldKey;
    public final String worldLabel;

    public AreaMarkerLayer(String key, String label, @NotNull World world) {
        super(key, label, world);
        this.worldKey = key;
        this.worldLabel = label;
    }

    @Override
    public void load() {
        HelixMarkers.areaRepository()
            .getAreas(getWorld().getKey())
            .forEach(this::loadArea);
    }

    public void loadArea(AreaEntity area) {
        if (super.hasMarker(area.getKey())) {
            super.removeMarker(area.getKey());
        }
        var pointsCol = area.getPoints();
        if (pointsCol != null && !pointsCol.isEmpty()) {
            List<Position> points = pointsCol
                .stream()
                .map(p -> new Position(p.getX(), p.getZ()))
                .collect(Collectors.toList());
            super.addMarker(
                AreaMarkerBuilder
                    .newAreaMarker(area.getKey(), points)
                    .fill(area.getColor())
                    .stroke(area.getColor())
                    .addPopup(area.getLabel())
            );
        }
    }

    /**
     * Add and store a new marker
     *
     * @param pos position of point
     */
    public void addPoint(@Language("HTML") String label, int color, Position pos) {
        boolean added = HelixMarkers.areaRepository().addPoint(getWorld().getKey(), label, color, pos);
        if (added) {
            // reload the area
            AreaEntity area = HelixMarkers.areaRepository().getArea(getWorld().getKey(), label, color);
            if (area != null) loadArea(area);
        }
    }

    /**
     * Remove a marker
     *
     * @param pos position of point
     */
    public void removePoint(@Language("HTML") String label, int color, Position pos) {
        boolean removed = HelixMarkers.areaRepository().removePoint(getWorld().getKey(), label, color, pos);
        if (removed) {
            // reload the area
            AreaEntity area = HelixMarkers.areaRepository().getArea(getWorld().getKey(), label, color);
            if (area != null) loadArea(area);
        }
    }

}