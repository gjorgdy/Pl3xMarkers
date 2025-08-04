package eu.hexasis.helixmarkers.layers;

import eu.hexasis.helixmarkers.HelixMarkers;
import eu.hexasis.helixmarkers.entities.AreaEntity;
import eu.hexasis.helixmarkers.entities.DynamicLinePointEntity;
import eu.hexasis.helixmarkers.markers.LineMarkerBuilder;
import it.unimi.dsi.fastutil.Pair;
import net.pl3x.map.core.world.World;
import org.intellij.lang.annotations.Language;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

public class DynamicLineMarkerLayer extends MarkerLayer {

    public final String worldKey;
    public final String worldLabel;

    List<DynamicLinePointEntity> points = new ArrayList<>();
    List<Pair<DynamicLinePointEntity, DynamicLinePointEntity>> lines = new ArrayList<>();

    public DynamicLineMarkerLayer(String key, String label, @NotNull World world) {
        super(key, label, world);
        this.worldKey = key;
        this.worldLabel = label;
    }

    private void loadPoints() {
        points.clear();
        points = HelixMarkers.dynamicLineRepository()
                .getPoints(getWorldKey(), getLayerKey());
        points.sort(DynamicLinePointEntity::compareTo);
    }

    @Override
    public void load() {
        loadPoints();
        // construct lines
        for (int i = 0; i < points.size() - 1; i++) {
            constructLines(i);
        }
    }

    public void constructLines(int index) {
        var start = points.get(index);
        for (int i = index + 1; i < points.size() - 1; i++) {
            var point = points.get(i);
            if (Math.abs(start.getX() - point.getX()) > 64) return;
            if (Math.abs(start.getZ() - point.getZ()) > 64) return;
            if (start.goesEast() && point.goesWest() && start.getZ() == point.getZ()) {
                addLine(start, point);
                continue;
            }
            if (start.goesSouth() && point.goesNorth() && start.getX() == point.getX()) {
                addLine(start, point);
                continue;
            }
        }
    }

    public void addLine(DynamicLinePointEntity start, DynamicLinePointEntity end) {
        System.out.println("Connecting " + start + " to " + end);
        lines.add(Pair.of(start, end));
        var line = LineMarkerBuilder
            .newLineMarker(start.getId() + ":" + end.getId(), List.of(start, end))
            .stroke(0xFFFFFF);
        addMarker(line);
    }

    /**
     * Add and store a new marker
     */
    public void addPoint(@Language("HTML") String label, int color, int x, int z) {
        boolean added = HelixMarkers.areaRepository().addPoint(getWorld().getKey(), label, color, x, z);
        if (added) {
            // reload the area
            AreaEntity area = HelixMarkers.areaRepository().getArea(getWorld().getKey(), label, color);
//            if (area != null) loadArea(area);
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
//            if (area != null) loadArea(area);
        }
    }

    private record Point(int x, int z, List<Point> neighbours) {}

}