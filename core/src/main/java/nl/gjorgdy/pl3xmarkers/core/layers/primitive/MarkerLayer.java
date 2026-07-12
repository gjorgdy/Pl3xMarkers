package nl.gjorgdy.pl3xmarkers.core.layers.primitive;

import net.pl3x.map.core.markers.layer.WorldLayer;
import net.pl3x.map.core.world.World;
import nl.gjorgdy.pl3xmarkers.core.interfaces.entities.IMarker;
import nl.gjorgdy.pl3xmarkers.core.markers.MarkerBuilder;
import org.jetbrains.annotations.NotNull;

public abstract class MarkerLayer<T extends IMarker> extends WorldLayer {

    public final String worldIdentifier;

    public MarkerLayer(String key, String label, @NotNull World world, int priority) {
        super(key, world, () -> label);
        worldIdentifier = world.getKey();
        setPriority(priority);
    }

    /**
     * Load previously created markers
     */
    abstract public void load();

    final public String toMarkerKey(int x, int y, int z) {
        return x + ":" + y + ":" + z;
    }

    protected void addMarker(MarkerBuilder<?> markerBuilder) {
        addMarker(markerBuilder.build());
    }

    protected void updateMarker(T markerEntity) {
        removeMarker(markerEntity);
        loadMarker(markerEntity);
    }

    protected void removeMarker(T markerEntity) {
        super.removeMarker(markerEntity.getKey());
    }

    public boolean hasMarker(T markerEntity) {
        return super.hasMarker(markerEntity.getKey());
    }

    public abstract void loadMarker(T markerEntity);
}
