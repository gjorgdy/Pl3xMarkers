package nl.gjorgdy.pl3xmarkers.core.layers.primitive;

import net.pl3x.map.core.world.World;
import nl.gjorgdy.pl3xmarkers.core.Pl3xMarkersCore;
import nl.gjorgdy.pl3xmarkers.core.interfaces.ISimpleMarkerRepository;
import nl.gjorgdy.pl3xmarkers.core.interfaces.entities.ISimpleMarker;
import nl.gjorgdy.pl3xmarkers.core.markers.IconMarkerBuilder;
import nl.gjorgdy.pl3xmarkers.core.objects.InteractionResult;
import org.intellij.lang.annotations.Language;
import org.jetbrains.annotations.NotNull;

public abstract class SimpleMarkerLayer extends MarkerLayer {

    public final String iconId;
    public final String key;
    public final String label;
    @Language("HTML")
    public final String tooltip;

    public SimpleMarkerLayer(String icon, String key, String label, @Language("HTML") String tooltip, @NotNull World world, int priority) {
        super(key, label, world, priority);
        iconId = icon;
        this.key = key;
        this.label = label;
        this.tooltip = tooltip;
    }

    @Override
    public void load() {
        getRepository().foreach(this::addMarker);
    }

    final protected boolean addInternal(int x, int y, int z) {
        if (hasMarker(toMarkerKey(x, y, z))) {
            return false; // already exists
        }
        var marker = getRepository().create(x, y, z);
        if (marker != null) {
            addMarker(marker);
			return true;
        }
		return false;
    }

    /**
     * Add a new marker
     *
     * @param x x coordinate of marker
     * @param y y coordinate of marker
     * @param z z coordinate of marker
     */
    public InteractionResult add(int x, int y, int z) {
        boolean added = addInternal(x, y, z);
        return added ? InteractionResult.added("Added " + tooltip + " marker") : InteractionResult.skip();
    }

    final protected boolean removeInternal(int x, int y, int z) {
        var removed = getRepository().remove(x, y, z);
        if (removed) {
            super.removeMarker(toMarkerKey(x, y, z));
        }
		return removed;
    }

    /**
     * Change the name of a marker
     *
     * @param x x coordinate of marker
     * @param y y coordinate of marker
     * @param z z coordinate of marker
     */
    public InteractionResult setName(int x, int y, int z, String newName) {
        boolean named = setNameInternal(x, y, z, newName);
        return named ? InteractionResult.added("Renamed " + tooltip + " marker") : InteractionResult.failure(
                "Could not rename " + tooltip + " marker");
    }

    final protected boolean setNameInternal(int x, int y, int z, String newName) {
        var marker = getRepository().getOrCreate(x, y, z);
        if (marker == null) {
            return false;
        }
        marker.setName(newName);
        updateMarker(marker);
        return true;
    }

    /**
     * Change the color of a marker
     *
     * @param x x coordinate of marker
     * @param y y coordinate of marker
     * @param z z coordinate of marker
     */
    public InteractionResult setColor(int x, int y, int z, int newColor) {
        boolean colored = setColorInternal(x, y, z, newColor);
        return colored ? InteractionResult.added("Colored " + tooltip + " marker") : InteractionResult.failure(
                "Could not color " + tooltip + " marker");
    }

    final protected boolean setColorInternal(int x, int y, int z, int newColor) {
        var marker = getRepository().getOrCreate(x, y, z);
        if (marker == null) {
            return false;
        }
        marker.setColor(newColor);
        updateMarker(marker);
        return true;
    }

    /**
     * Remove a marker
     *
     * @param x x coordinate of marker
     * @param y y coordinate of marker
     * @param z z coordinate of marker
     */
    public InteractionResult remove(int x, int y, int z) {
        boolean removed = removeInternal(x, y, z);
        return removed ? InteractionResult.removed("Removed " + tooltip + " marker") : InteractionResult.skip();
    }

    private void updateMarker(ISimpleMarker markerEntity) {
        var pos = markerEntity.getPosition();
        super.removeMarker(toMarkerKey(pos.x(), pos.y(), pos.z()));
        addMarker(markerEntity);
    }

    private void addMarker(ISimpleMarker markerEntity) {
        var pos = markerEntity.getPosition();
        var icon = IconMarkerBuilder.newIconMarker(
                        toMarkerKey(pos.x(), pos.y(), pos.z()), iconId, pos.x(), pos.z()
                )
                .centerIcon(16, 16);
        @Language("HTML") var popup = createPopup(markerEntity);
        if (popup != null) {
            icon.addPopup(popup);
        } else {
            icon.addTooltip(createTooltip(markerEntity));
        }
        super.addMarker(icon.build());
    }

    @Language("HTML")
    protected String createPopup(ISimpleMarker markerEntity) {
        return null;
    }

    @Language("HTML")
    protected String createTooltip(ISimpleMarker markerEntity) {
        @Language("HTML") var name = markerEntity.getName();
        return name != null ? name : tooltip;
    }

    protected ISimpleMarkerRepository<?> getRepository() {
        return Pl3xMarkersCore.storage()
                .getWorldRepository(worldIdentifier)
                .getSimpleMarkerRepository(getKey());
    }

}