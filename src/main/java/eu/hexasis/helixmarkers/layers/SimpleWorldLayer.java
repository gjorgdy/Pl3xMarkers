package eu.hexasis.helixmarkers.layers;

import net.minecraft.util.math.BlockPos;
import net.pl3x.map.core.markers.Point;
import net.pl3x.map.core.markers.Vector;
import net.pl3x.map.core.markers.layer.WorldLayer;
import net.pl3x.map.core.markers.marker.Icon;
import net.pl3x.map.core.markers.marker.Marker;
import net.pl3x.map.core.markers.option.Options;
import net.pl3x.map.core.world.World;
import org.jetbrains.annotations.NotNull;

public abstract class SimpleWorldLayer extends WorldLayer {

    public final String iconId;
    public final String key;
    public final String label;
    public final String tooltip;

    public SimpleWorldLayer(String icon, String key, String label, @NotNull World world) {
        this(icon, key, label, null, world);
    }

    public SimpleWorldLayer(String icon, String key, String label, String tooltip, @NotNull World world) {
        super(key, world, () -> label);
        this.iconId = icon;
        this.key = key;
        this.label = label;
        this.tooltip = tooltip;
        load();
    }

    /**
     * Load previously created markers
     */
    abstract protected void load();

    /**
     * Add and store a new marker
     *
     * @param pos location of marker
     */
    final public void addMarker(BlockPos pos) {
        addMarker(pos.getX(), pos.getZ());
    }

    /**
     * Add and store a new marker
     *
     * @param x x-coordinate of marker
     * @param z z-coordinate of marker
     */
    abstract public void addMarker(int x, int z);

    /**
     * Remove a marker
     *
     * @param pos location of marker
     */
    final public void removeMarker(BlockPos pos) {
        removeMarker(pos.getX(), pos.getZ());
    }

    /**
     * Remove a marker
     *
     * @param x x-coordinate of marker
     * @param z z-coordinate of marker
     */
    abstract public void removeMarker(int x, int z);

    protected Marker<Icon> createMarker(int x, int z) {
        Icon marker = new Icon(
                x + ":" + z,
                new Point(x, z),
                iconId
        );
        marker.setAnchor(new Vector(8, 8));
        if (tooltip != null) {
            marker.setOptions(new Options.Builder().tooltipContent(tooltip).build());
        }
        return marker;
    }

    final public boolean hasMarker(int x, int z) {
        return hasMarker(x + ":" + z);
    }

}
