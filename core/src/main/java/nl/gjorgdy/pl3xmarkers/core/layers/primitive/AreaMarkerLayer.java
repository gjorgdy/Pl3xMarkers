package nl.gjorgdy.pl3xmarkers.core.layers.primitive;

import nl.gjorgdy.pl3xmarkers.core.Pl3xMarkersCore;
import nl.gjorgdy.pl3xmarkers.core.helpers.ConvexHull;
import nl.gjorgdy.pl3xmarkers.core.helpers.HtmlHelper;
import nl.gjorgdy.pl3xmarkers.core.interfaces.entities.IAreaMarker;
import nl.gjorgdy.pl3xmarkers.core.markers.AreaMarkerBuilder;
import net.pl3x.map.core.world.World;
import org.intellij.lang.annotations.Language;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;

public class AreaMarkerLayer extends MarkerLayer {

    public AreaMarkerLayer(String key, String label, @NotNull World world, int priority) {
        super(key, label, world, priority);
    }

    @Override
    public void load() {
        Pl3xMarkersCore.storage()
			.getAreaMarkerRepository()
            .getAreas(worldIdentifier)
            .forEach(this::loadArea);
    }

    public void loadArea(IAreaMarker area) {
        if (super.hasMarker(area.getKey())) {
            super.removeMarker(area.getKey());
        }
		var points = area.getPoints();
		if (points == null || points.isEmpty()) {
			return;
		}
        var sortedPoints = ConvexHull.calculate(new ArrayList<>(area.getPoints()));
		if (!sortedPoints.isEmpty()) {
			super.addMarker(
				AreaMarkerBuilder
					.newAreaMarker(area.getKey(), sortedPoints)
					.fill(area.getColor())
					.stroke(area.getColor())
					.addPopup(HtmlHelper.sanitize(area.getName()))
			);
		}
    }

    /**
     * Add a new point to an area
     */
    public boolean addPoint(@Language("HTML") String label, int color, int x, int z) {
		var area = Pl3xMarkersCore.storage()
					   .getAreaMarkerRepository()
					   .getOrCreateArea(worldIdentifier, label, color);
        if (area.addPoint(x, z)) {
			Pl3xMarkersCore.runParallel(() -> loadArea(area));
			return true;
        }
		return false;
    }

    /**
     * Remove a point from an area
     */
    public boolean removePoint(@Language("HTML") String label, int color, int x, int z) {
		var area = Pl3xMarkersCore.storage()
					   .getAreaMarkerRepository()
					   .getArea(worldIdentifier, label, color);
        if (area != null && area.removePoint(x, z)) {
			Pl3xMarkersCore.runParallel(() -> loadArea(area));
			return true;
        }
		return false;
    }

}