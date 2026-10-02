package nl.gjorgdy.pl3xmarkers.core.layers.primitive;

import net.pl3x.map.core.world.World;
import nl.gjorgdy.pl3xmarkers.core.MarkersConfig;
import nl.gjorgdy.pl3xmarkers.core.Pl3xMarkersCore;
import nl.gjorgdy.pl3xmarkers.core.helpers.ConvexHull;
import nl.gjorgdy.pl3xmarkers.core.helpers.HtmlHelper;
import nl.gjorgdy.pl3xmarkers.core.interfaces.IAreaMarkerRepository;
import nl.gjorgdy.pl3xmarkers.core.interfaces.IBoundary;
import nl.gjorgdy.pl3xmarkers.core.interfaces.entities.IAreaMarker;
import nl.gjorgdy.pl3xmarkers.core.interfaces.entities.IPoint;
import nl.gjorgdy.pl3xmarkers.core.markers.AreaMarkerBuilder;
import nl.gjorgdy.pl3xmarkers.core.markers.MarkerBuilder;
import nl.gjorgdy.pl3xmarkers.core.objects.CircleBoundary;
import nl.gjorgdy.pl3xmarkers.core.objects.InteractionResult;
import nl.gjorgdy.pl3xmarkers.core.objects.PolygonBoundary;
import nl.gjorgdy.pl3xmarkers.core.registries.Layers;
import org.intellij.lang.annotations.Language;
import org.jetbrains.annotations.NotNull;
import org.jspecify.annotations.Nullable;

import java.text.DecimalFormat;
import java.util.*;

public class AreaMarkerLayer extends StoredMarkerLayer<IAreaMarker, IAreaMarkerRepository<? extends IAreaMarker>> {

	private HashMap<String, IBoundary> boundaries;

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
		// If there are 2 points in line, make a circle instead of a polygon
		if (points.size() == 2 && areInline(points)) {
			var sorted = points.stream()
					.sorted(Comparator.comparingInt(IPoint::x).thenComparingInt(IPoint::z))
					.toList();
			var center = sorted.get(0).middle(sorted.get(1));
			var radius = (int) Math.round(sorted.get(0).distance(sorted.get(1)) / 2);
			if (MarkersConfig.FEEDBACK_AREA_ENTER_ENABLED) {
				boundaries.put(
						area.getKey(),
						new CircleBoundary(center, radius, area)
				);
			}
			return AreaMarkerBuilder.newAreaMarker(area.getKey(), center, radius)
					.fill(area.getColor())
					.stroke(area.getColor());
		}

		var orderedPoints = ConvexHull.calculate(new ArrayList<>(area.getPoints()));
		if (MarkersConfig.FEEDBACK_AREA_ENTER_ENABLED) {
			boundaries.put(
				area.getKey(),
				new PolygonBoundary(area.getMinCorner(), area.getMaxCorner(), orderedPoints, area)
			);
		}
		if (!orderedPoints.isEmpty()) {
			return AreaMarkerBuilder.newAreaMarker(area.getKey(), orderedPoints)
					.fill(area.getColor())
					.stroke(area.getColor());
		}
		return null;
	}

	private boolean areInline(Collection<? extends IPoint> points) {
		IPoint lastPoint = null;
		for (var point : points) {
			if (lastPoint == null) {
				lastPoint = point;
				continue;
			}
			if (lastPoint.x() != point.x() && lastPoint.z() != point.z()) {
				return false;
			}
			lastPoint = point;
		}
		return true;
	}

	private String createContent(IAreaMarker area) {
		var popupBuilder = new StringBuilder();
		popupBuilder.append(HtmlHelper.sanitize(area.getName()));
		if (MarkersConfig.AREA_MARKERS_SHOW_SIZE) {
			var polygonArea = boundaries.get(area.getKey()).size();
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
			    return InteractionResult.added(MarkersConfig.MESSAGE_AREA_CREATE.replace("{label}", label));
		    }
		    return InteractionResult.added(MarkersConfig.MESSAGE_AREA_POINT_ADD.replace("{label}", label));
        }
	    return InteractionResult.failure(MarkersConfig.MESSAGE_AREA_POINT_ADD_FAILED.replace("{label}", label));
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
			    return InteractionResult.removed(MarkersConfig.MESSAGE_AREA_REMOVE.replace("{label}", label));
		    }
		    return InteractionResult.removed(MarkersConfig.MESSAGE_AREA_POINT_REMOVE.replace("{label}", label));
        }
	    return InteractionResult.skip();
    }

	public Optional<IBoundary> getContaining(int x, int z) {
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