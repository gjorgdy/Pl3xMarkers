package nl.gjorgdy.pl3xmarkers.core.layers.primitive;

import net.pl3x.map.core.world.World;
import nl.gjorgdy.pl3xmarkers.core.MarkersConfig;
import nl.gjorgdy.pl3xmarkers.core.Pl3xMarkersCore;
import nl.gjorgdy.pl3xmarkers.core.helpers.ConvexHull;
import nl.gjorgdy.pl3xmarkers.core.helpers.HtmlHelper;
import nl.gjorgdy.pl3xmarkers.core.helpers.PolygonArea;
import nl.gjorgdy.pl3xmarkers.core.interfaces.IAreaMarkerRepository;
import nl.gjorgdy.pl3xmarkers.core.interfaces.entities.IAreaMarker;
import nl.gjorgdy.pl3xmarkers.core.markers.AreaMarkerBuilder;
import nl.gjorgdy.pl3xmarkers.core.objects.Boundary;
import nl.gjorgdy.pl3xmarkers.core.objects.InteractionResult;
import nl.gjorgdy.pl3xmarkers.core.registries.Layers;
import org.intellij.lang.annotations.Language;
import org.jetbrains.annotations.NotNull;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Optional;

public class AreaMarkerLayer extends MarkerLayer {

	private HashMap<String, Boundary> boundaries;

	public AreaMarkerLayer(@NotNull World world) {
		super(Layers.Keys.AREAS, Layers.Labels.AREAS, world, MarkersConfig.AREA_MARKERS_PRIORITY);
    }

    @Override
    public void load() {
		if (MarkersConfig.FEEDBACK_AREA_ENTER_ENABLED) {
			boundaries = new HashMap<>();
		}
	    getRepository().foreach(this::loadArea);
    }

	private void loadArea(IAreaMarker area) {
		super.removeMarker(area.getKey());
		if (boundaries != null) {
			boundaries.remove(area.getKey());
		}
		var points = area.getPoints();
		if (points == null || points.isEmpty()) {
			return;
		}
        var orderedPoints = ConvexHull.calculate(new ArrayList<>(area.getPoints()));
		var popupBuilder = new StringBuilder();
		popupBuilder
				.append("<b>")
				.append(HtmlHelper.sanitize(area.getName()))
				.append("</b>");
		if (MarkersConfig.AREA_MARKERS_SHOW_SIZE) {
			var polygonArea = PolygonArea.calculate(orderedPoints);
			var areaFormatted = new DecimalFormat("#.#").format(polygonArea);
			popupBuilder
					.append("<br><i>")
					.append(areaFormatted)
					.append(" b²<i/>");
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
						.addPopup(popupBuilder.toString())
			);
		}
    }

    /**
     * Add a new point to an area
     */
    public InteractionResult addPoint(@Language("HTML") String label, int color, int x, int y, int z) {
	    var area = getRepository().getOrCreate(label, color);
	    if (area.addPoint(x, y, z)) {
			Pl3xMarkersCore.runParallel(() -> loadArea(area));
		    return InteractionResult.added("Added point to area: " + label);
        }
	    return InteractionResult.failure("Could not add point to area: " + label);
    }

    /**
     * Remove a point from an area
     */
    public InteractionResult removePoint(@Language("HTML") String label, int color, int x, int y, int z) {
	    var area = getRepository().get(label, color);
	    if (area != null && area.removePoint(x, y, z)) {
			Pl3xMarkersCore.runParallel(() -> loadArea(area));
		    if (area.getPoints().isEmpty()) {
			    getRepository().remove(label, color);
			    return InteractionResult.removed("Removed area: " + label);
		    }
		    return InteractionResult.removed("Removed point from area: " + label);
        }
	    return InteractionResult.failure("Could not remove point from area: " + label);
    }

	public Optional<Boundary> getContaining(int x, int z) {
		return boundaries.values().stream()
		   .filter(b -> b.contains(x, z))
		   .findFirst();
	}

	private IAreaMarkerRepository<?> getRepository() {
		return Pl3xMarkersCore.storage()
				.getWorldRepository(worldIdentifier)
				.getAreaMarkerRepository(getKey());
	}

}