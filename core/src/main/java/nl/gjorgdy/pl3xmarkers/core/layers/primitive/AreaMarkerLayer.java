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
import nl.gjorgdy.pl3xmarkers.core.markers.MarkerBuilder;
import nl.gjorgdy.pl3xmarkers.core.objects.Boundary;
import nl.gjorgdy.pl3xmarkers.core.objects.InteractionResult;
import nl.gjorgdy.pl3xmarkers.core.registries.Layers;
import org.intellij.lang.annotations.Language;
import org.jetbrains.annotations.NotNull;
import org.jspecify.annotations.Nullable;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Optional;

public class AreaMarkerLayer extends StoredMarkerLayer<IAreaMarker, IAreaMarkerRepository<? extends IAreaMarker>> {

	private HashMap<String, Boundary> boundaries;

	public AreaMarkerLayer(@NotNull World world) {
		super(Layers.Keys.AREAS, Layers.Labels.AREAS, world, MarkersConfig.AREA_MARKERS_PRIORITY);
    }

    @Override
    public void load() {
		if (MarkersConfig.FEEDBACK_AREA_ENTER_ENABLED) {
			boundaries = new HashMap<>();
		}
	    getRepository().foreach(this::loadMarker);
    }

	@Override
	public MarkerBuilder<?> createBuilder(IAreaMarker area) {
		super.removeMarker(area);
		if (boundaries != null) {
			boundaries.remove(area.getKey());
		}
		var points = area.getPoints();
		if (points == null || points.isEmpty()) {
			return null;
		}
		var orderedPoints = ConvexHull.calculate(new ArrayList<>(area.getPoints()));
		if (MarkersConfig.FEEDBACK_AREA_ENTER_ENABLED) {
			boundaries.put(
				area.getKey(),
				new Boundary(area.getMinCorner(), area.getMaxCorner(), orderedPoints, area)
			);
		}
		if (!orderedPoints.isEmpty()) {
			return AreaMarkerBuilder.newAreaMarker(area.getKey(), orderedPoints)
					.fill(area.getColor())
					.stroke(area.getColor());
		}
		return null;
	}

	private String createContent(IAreaMarker area) {
		var popupBuilder = new StringBuilder();
		popupBuilder.append(HtmlHelper.sanitize(area.getName()));
		if (MarkersConfig.AREA_MARKERS_SHOW_SIZE) {
			var polygonArea = PolygonArea.calculate(boundaries.get(area.getKey()).orderedPoints());
			var areaFormatted = new DecimalFormat("#.#").format(polygonArea);
			popupBuilder
					.append("<br><i>")
					.append(areaFormatted)
					.append(" b²<i/>");
		}
		return popupBuilder.toString();
	}

	@Override
	protected @Nullable String createPermanentCenteredTooltip(IAreaMarker object) {
		return MarkersConfig.AREA_MARKERS_MARKERS_ALWAYS_SHOW_NAME ? createContent(object) : null;
	}

	@Override
	protected @Nullable String createPopup(IAreaMarker object) {
		return !MarkersConfig.AREA_MARKERS_MARKERS_ALWAYS_SHOW_NAME ? createContent(object) : null;
	}

	@Override
	public Optional<? extends IAreaMarker> getMarker(String key) {
		return getRepository().stream()
				.filter(m -> m.getKey().equals(key))
				.findFirst();
	}

	@Override
	public Optional<String> getClosestMarker(int x, int y, int z) {
		getRepository().stream()
				.min(Comparator.comparingDouble(m -> distanceFromArea(m, x, y, z)))
				.map(IAreaMarker::getKey);
		return Optional.empty();
	}

	private double distanceFromArea(IAreaMarker area, int x, int y, int z) {
		area.getPoints().stream()
				.min(Comparator.comparingDouble(m -> m.distance(x, y, z)))
				.map(m -> m.distance(x, y, z))
				.orElse(Double.MAX_VALUE);
		return Double.MAX_VALUE;
	}

    /**
     * Add a new point to an area
     */
    public InteractionResult addPoint(@Language("HTML") String label, int color, int x, int y, int z) {
	    var area = getRepository().getOrCreate(label, color);
	    if (area.addPoint(x, y, z)) {
		    Pl3xMarkersCore.runParallel(() -> loadMarker(area));
		    if (area.getPoints().size() == 1) {
			    return InteractionResult.added("Created area: " + label);
		    }
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
		    Pl3xMarkersCore.runParallel(() -> loadMarker(area));
		    if (area.isEmpty()) {
			    super.removeMarker(area);
			    getRepository().remove(label, color);
			    return InteractionResult.removed("Removed area: " + label);
		    }
		    return InteractionResult.removed("Removed point from area: " + label);
        }
	    return InteractionResult.skip();
    }

	public Optional<Boundary> getContaining(int x, int z) {
		return boundaries.values().stream()
		   .filter(b -> b.contains(x, z))
		   .findFirst();
	}

	@Override
	protected IAreaMarkerRepository<? extends IAreaMarker> getRepository() {
		return Pl3xMarkersCore.storage()
				.getWorldRepository(worldIdentifier)
				.getAreaMarkerRepository(getKey());
	}

}