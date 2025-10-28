package nl.gjorgdy.pl3xmarkers.core.layers.primitive;

import nl.gjorgdy.pl3xmarkers.core.markers.MarkerBuilder;
import net.pl3x.map.core.markers.layer.WorldLayer;
import net.pl3x.map.core.world.World;
import org.jetbrains.annotations.NotNull;

public abstract class MarkerLayer extends WorldLayer {

    public final String worldIdentifier;

    public MarkerLayer(String key, String label, @NotNull World world, int priority) {
        super(key, world, () -> label);
        this.worldIdentifier = world.getKey();
		this.setPriority(priority);
    }

    /**
     * Load previously created markers
     */
    abstract public void load();

    final public String toMarkerKey(int x, int z) {
        return x + ":" + z;
    }

    public void addMarker(MarkerBuilder<?> markerBuilder) {
        addMarker(markerBuilder.build());
    }

}
