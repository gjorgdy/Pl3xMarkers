package nl.gjorgdy.pl3xmarkers.core.layers.primitive;

import net.pl3x.map.core.world.World;
import nl.gjorgdy.pl3xmarkers.core.MarkersConfig;
import nl.gjorgdy.pl3xmarkers.core.Pl3xMarkersCore;
import nl.gjorgdy.pl3xmarkers.core.interfaces.ISimpleMarkerRepository;
import nl.gjorgdy.pl3xmarkers.core.interfaces.entities.ISimpleMarker;
import nl.gjorgdy.pl3xmarkers.core.markers.IconMarkerBuilder;
import nl.gjorgdy.pl3xmarkers.core.markers.MarkerBuilder;
import nl.gjorgdy.pl3xmarkers.core.objects.InteractionResult;
import org.intellij.lang.annotations.Language;
import org.jetbrains.annotations.NotNull;

import java.util.Comparator;
import java.util.Optional;

public abstract class SimpleMarkerLayer extends StoredMarkerLayer<ISimpleMarker, ISimpleMarkerRepository<? extends ISimpleMarker>> {

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
        getRepository().foreach(this::loadMarker);
    }

    final protected boolean addInternal(int x, int y, int z) {
        if (hasMarker(toMarkerKey(x, y, z))) {
            return false; // already exists
        }
        var marker = getRepository().create(x, y, z);
        if (marker != null) {
            loadMarker(marker);
			return true;
        }
		return false;
    }

    /**
     * Interact with a marker
     *
     * @param x x coordinate of marker
     * @param y y coordinate of marker
     * @param z z coordinate of marker
     */
    public InteractionResult interact(int x, int y, int z) {
        var marker = getMarker(toMarkerKey(x, y, z));
        if (marker.isEmpty()) {
            return InteractionResult.skip();
        }
        var name = marker.map(ISimpleMarker::getName).orElse("");
        if (name.isEmpty()) {
            return InteractionResult.skip();
        }
        return InteractionResult.feedback(name);
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
        return added ? InteractionResult.added(MarkersConfig.MESSAGE_MARKER_ADD.replace("{type}", tooltip)) : InteractionResult.skip();
    }

    @Override
    public Optional<? extends ISimpleMarker> getMarker(String key) {
        return getRepository().stream()
                .filter(m -> m.getKey().equals(key))
                .findFirst();
    }

    @Override
    public Optional<String> getClosestMarker(int x, int y, int z) {
        var closestMarker = getRepository().stream()
                .min((m1, m2) -> Comparator.comparingDouble(m ->
                                                                    ((ISimpleMarker) m).getPosition().distance(x, y, z)
                ).compare(m1, m2));
        return closestMarker.map(ISimpleMarker::getKey);
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
        return setNameInternal(x, y, z, newName);
    }

    final protected InteractionResult setNameInternal(int x, int y, int z, String newName) {
        var marker = getRepository().getOrCreate(x, y, z);
        if (marker == null) {
            return InteractionResult.failure(MarkersConfig.MESSAGE_MARKER_RENAME_FAILED.replace("{type}", tooltip));
        }
        if (marker.getName() != null && marker.getName().equals(newName)) {
            return InteractionResult.feedback(marker.getName());
        }
        marker.setName(newName);
        updateMarker(marker);
        return InteractionResult.added(
                MarkersConfig.MESSAGE_MARKER_RENAME.replace("{type}", tooltip).replace("{name}", newName));
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
        return colored ? InteractionResult.added(MarkersConfig.MESSAGE_MARKER_COLOR.replace("{type}", tooltip)) : InteractionResult.failure(
            MarkersConfig.MESSAGE_MARKER_COLOR_FAILED.replace("{type}", tooltip));
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

    @Override
    public MarkerBuilder<?> createBuilder(ISimpleMarker object) {
        var pos = object.getPosition();
        return IconMarkerBuilder.newIconMarker(
                        toMarkerKey(pos.x(), pos.y(), pos.z()), iconId, pos.x(), pos.z()
                )
                .centerIcon(16, 16);
    }

    @Override
    @Language("HTML")
    protected String createTooltip(ISimpleMarker markerEntity) {
        @Language("HTML") var name = markerEntity.getName();
        return name != null ? name : tooltip;
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
        return removed ? InteractionResult.removed(MarkersConfig.MESSAGE_MARKER_REMOVE.replace("{type}", tooltip)) : InteractionResult.skip();
    }

    @Override
    protected ISimpleMarkerRepository<? extends ISimpleMarker> getRepository() {
        return Pl3xMarkersCore.storage()
                .getWorldRepository(worldIdentifier)
                .getSimpleMarkerRepository(getKey());
    }

}