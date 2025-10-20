package nl.gjorgdy.pl3xmarkers.core.layers.primitive;

import nl.gjorgdy.pl3xmarkers.core.helpers.WorldHelpers;
import nl.gjorgdy.pl3xmarkers.core.markers.MarkerBuilder;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.world.ServerWorld;
import nl.gjorgdy.pl3xmarkers.markers.MarkerBuilder;
import net.minecraft.util.Identifier;
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

    final protected MinecraftServer getServer() {
        if (getWorld().getLevel() instanceof ServerWorld serverWorld) {
            return serverWorld.getServer();
        }
        throw new IllegalStateException("World is not a server world");
    }

    /**
     * Check if this layer should be shown in the given world
     * @param world the world to check against
     * @return true if the layer should be shown in the given world, false otherwise
     */
    abstract public boolean isInWorld(@NotNull World world);

    protected boolean isOverworld(World world) {
        return WorldHelpers.isOverworld(world.getKey());
    }

    protected boolean isNether(World world) {
        return WorldHelpers.isNether(world.getKey());
    }

    protected boolean isEnd(World world) {
        return WorldHelpers.isEnd(world.getKey());
    }

    final public String toMarkerKey(int x, int z) {
        return x + ":" + z;
    }

    public void addMarker(MarkerBuilder<?> markerBuilder) {
        addMarker(markerBuilder.build());
    }

}
