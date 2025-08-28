package eu.hexasis.helixmarkers.layers;

import eu.hexasis.helixmarkers.HelixMarkers;
import eu.hexasis.helixmarkers.entities.AreaEntity;
import eu.hexasis.helixmarkers.entities.SimpleLineEntity;
import eu.hexasis.helixmarkers.markers.LineMarkerBuilder;
import it.unimi.dsi.fastutil.Pair;
import net.pl3x.map.core.world.World;
import org.intellij.lang.annotations.Language;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

public class LineMarkerLayer extends MarkerLayer {

    public final String worldKey;
    public final String worldLabel;

    List<SimpleLineEntity> lines = new ArrayList<>();

    public LineMarkerLayer(String key, String label, @NotNull World world) {
        super(key, label, world);
        this.worldKey = key;
        this.worldLabel = label;
    }

    @Override
    public void load() {
        lines = HelixMarkers.LineRepository()
                .getPoints(getWorldKey(), getLayerKey());
        // construct lines
        for (int i = 0; i < lines.size() - 1; i++) {
            constructLines(i);
        }
    }

    public void addLine(int aX, int aZ, int bX, int bZ) {
        HelixMarkers.LineRepository().addOrUpdatePoint(getWorldKey(), getLayerKey(), aX, aZ);
        System.out.println("Connecting " + start + " to " + end);
        lines.add(Pair.of(start, end));
        var line = LineMarkerBuilder
            .newLineMarker(String.valueOf(id))
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