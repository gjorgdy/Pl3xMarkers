package nl.gjorgdy.pl3xmarkers.core.objects;

import nl.gjorgdy.pl3xmarkers.core.interfaces.entities.IAreaMarker;
import nl.gjorgdy.pl3xmarkers.core.interfaces.entities.IPoint;

import java.util.List;

public record Boundary(IPoint min, IPoint max, List<? extends IPoint> orderedPoints, IAreaMarker areaMarker) {
}
