package nl.gjorgdy.pl3xmarkers.core.layers.primitive;

import nl.gjorgdy.pl3xmarkers.core.MarkersConfig;
import nl.gjorgdy.pl3xmarkers.core.Pl3xMarkersCore;
import nl.gjorgdy.pl3xmarkers.core.helpers.ConvexHull;
import nl.gjorgdy.pl3xmarkers.core.helpers.HtmlHelper;
import nl.gjorgdy.pl3xmarkers.core.helpers.PolygonArea;
import nl.gjorgdy.pl3xmarkers.core.interfaces.entities.IAreaMarker;
import nl.gjorgdy.pl3xmarkers.core.markers.AreaMarkerBuilder;
import net.pl3x.map.core.world.World;
import nl.gjorgdy.pl3xmarkers.core.objects.Boundary;
import org.intellij.lang.annotations.Language;
import org.jetbrains.annotations.NotNull;

import java.text.DecimalFormat;
import java.util.*;

public class AreaMarkerLayer extends MarkerLayer {

	private HashMap<String, Boundary> boundaries;

    public AreaMarkerLayer(String key, String label, @NotNull World world, int priority) {
        super(key, label, world, priority);
    }

    @Override
    public void load() {
		if (MarkersConfig.FEEDBACK_AREA_ENTER_ENABLED) {
			boundaries = new HashMap<>();
		}
        Pl3xMarkersCore.storage()
			.getAreaMarkerRepository()
            .getAreas(worldIdentifier)
            .forEach(this::loadArea);
    }

    public void loadArea(IAreaMarker area) {
		super.removeMarker(area.getKey());
		if (boundaries != null) boundaries.remove(area.getKey());
		var points = area.getPoints();
		if (points == null || points.isEmpty()) {
			return;
		}
        var orderedPoints = ConvexHull.calculate(new ArrayList<>(area.getPoints()));
		@Language("HTML") var popup = "<b>" + HtmlHelper.sanitize(area.getName()) + "</b>";
		if (MarkersConfig.AREA_MARKERS_SHOW_SIZE) {
			var polygonArea = PolygonArea.calculate(orderedPoints);
			var areaFormatted = new DecimalFormat("#.#").format(polygonArea);
			popup += "<br><i>" + areaFormatted + " b²<i/>";
		}
		if (MarkersConfig.FEEDBACK_AREA_ENTER_ENABLED) {
			boundaries.put(
				area.getKey(),
				new Boundary(area.getMinCorner(), area.getMaxCorner(), orderedPoints, area)
			);
		}
		if (!orderedPoints.isEmpty()) {
			super.addMarker(
				AreaMarkerBuilder
					.newAreaMarker(area.getKey(), orderedPoints)
					.fill(area.getColor())
					.stroke(area.getColor())
					.addPopup(popup)
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

	public Optional<Boundary> getAreaBoundary(int x, int z) {
		return boundaries.values().stream()
		   .filter(b -> b.contains(x, z))
		   .findFirst();
	}

}