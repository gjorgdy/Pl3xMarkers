package nl.gjorgdy.pl3xmarkers.layers.primitive;

import nl.gjorgdy.pl3xmarkers.markers.MarkerBuilder;
import net.minecraft.util.Identifier;
import net.pl3x.map.core.markers.layer.WorldLayer;
import net.pl3x.map.core.world.World;
import org.jetbrains.annotations.NotNull;

public abstract class MarkerLayer extends WorldLayer {

    public final Identifier worldIdentifier;

    public MarkerLayer(String key, String label, @NotNull World world) {
        super(key, world, () -> label);
        this.worldIdentifier = Identifier.of(world.getKey());
    }

    /**
     * Load previously created markers
     */
    abstract public void load();

    /**
     * Check if this layer should be shown in the given world
     * @param world the world to check against
     * @return true if the layer should be shown in the given world, false otherwise
     */
    abstract public boolean isInWorld(@NotNull World world);

    protected boolean isOverworld(World world) {
        return world.getKey().equals("minecraft:overworld");
    }

    protected boolean isNether(World world) {
        return world.getKey().equals("minecraft:the_nether");
    }

    protected boolean isEnd(World world) {
        return world.getKey().equals("minecraft:the_end");
    }

    final public String toMarkerKey(int x, int z) {
        return x + ":" + z;
    }

    public void addMarker(MarkerBuilder<?> markerBuilder) {
        addMarker(markerBuilder.build());
    }

}
