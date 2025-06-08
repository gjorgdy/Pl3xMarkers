package eu.hexasis.helixmarkers.layers;

import eu.hexasis.helixmarkers.HelixMarkers;
import eu.hexasis.helixmarkers.markers.AreaBuilder;
import eu.hexasis.helixmarkers.objects.Area;
import eu.hexasis.helixmarkers.objects.Position;
import eu.hexasis.helixmarkers.repositories.AreaRepository;
import net.pl3x.map.core.world.World;
import org.intellij.lang.annotations.Language;
import org.jetbrains.annotations.NotNull;

public class AreaMarkerLayer extends MarkerLayer {

    public final String worldKey;
    public final String worldLabel;

    public AreaMarkerLayer(String key, String label, @NotNull World world) {
        super(key, label, world);
        this.worldKey = key;
        this.worldLabel = label;
    }

    private AreaRepository repo() {
        return HelixMarkers.AREA_REPOSITORY;
    }

    @Override
    public void load() {
        HelixMarkers.AREA_REPOSITORY
            .getAreas(getWorld().getKey())
            .forEach(this::loadArea);
    }

    public void loadArea(Area area) {
        if (super.hasMarker(area.Key())) {
            super.removeMarker(area.Key());
        }
        if (!area.points().isEmpty()) {
            super.addMarker(
                AreaBuilder
                    .newArea(area.Key(), area.points())
                    .fill(area.color())
                    .stroke(area.color())
                    .addPopup(area.label())
            );
        }
    }

    /**
     * Add and store a new marker
     *
     * @param pos position of point
     */
    public void addPoint(@Language("HTML") String label, int color, Position pos) {
        boolean added = repo().addPoint(getWorld().getKey(), label, color, pos);
        if (added) {
            // reload the area
            loadArea(repo().getArea(getWorld().getKey(), label, color));
        }
    }

    /**
     * Remove a marker
     *
     * @param pos position of point
     */
    public void removePoint(@Language("HTML") String label, int color, Position pos) {
        boolean removed = repo().removePoint(getWorld().getKey(), label, color, pos);
        if (removed) {
            // reload the area
            loadArea(repo().getArea(getWorld().getKey(), label, color));
        }
    }

}