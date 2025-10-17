package nl.gjorgdy.pl3xmarkers.core.layers.primitive;

import nl.gjorgdy.pl3xmarkers.core.markers.MarkerBuilder;
import net.pl3x.map.core.markers.layer.WorldLayer;
import net.pl3x.map.core.world.World;
import org.jetbrains.annotations.NotNull;

public abstract class MarkerLayer extends WorldLayer {

    public final String worldIdentifier;

    public MarkerLayer(String key, String label, @NotNull World world) {
        super(key, world, () -> label);
        this.worldIdentifier = world.getKey();
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
        return world.getKey().equals("minecraft:overworld") // fabric
                || world.getKey().equals("world"); // bukkit
    }

    protected boolean isNether(World world) {
        return world.getKey().equals("minecraft:the_nether") // fabric
                || world.getKey().equals("world_nether"); // bukkit;
    }

    protected boolean isEnd(World world) {
        return world.getKey().equals("minecraft:the_end") // fabric
                || world.getKey().equals("world_the_end"); // bukkit;
    }

    final public String toMarkerKey(int x, int z) {
        return x + ":" + z;
    }

    public void addMarker(MarkerBuilder<?> markerBuilder) {
        addMarker(markerBuilder.build());
    }

}
