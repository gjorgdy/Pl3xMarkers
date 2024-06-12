package eu.hexasis.helixmarkers.layers;

import eu.hexasis.helixmarkers.markers.MarkerBuilder;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.BlockPos;
import net.pl3x.map.core.markers.layer.WorldLayer;
import net.pl3x.map.core.world.World;
import org.jetbrains.annotations.NotNull;

public abstract class MarkerLayer extends WorldLayer {

    public final Identifier worldIdentifier;

    public MarkerLayer(String key, String label, @NotNull World world) {
        super(key, world, () -> label);
        this.worldIdentifier = new Identifier(world.getKey());
    }

    /**
     * Load previously created markers
     */
    abstract public void load();

    final public String toMarkerKey(BlockPos pos) {
        return toMarkerKey(pos.getX(), pos.getZ());
    }

    final public String toMarkerKey(int x, int z) {
        return x + ":" + z;
    }

    public void addMarker(MarkerBuilder markerBuilder) {
        addMarker(markerBuilder.build());
    }

}
