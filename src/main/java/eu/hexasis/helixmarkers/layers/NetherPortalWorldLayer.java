package eu.hexasis.helixmarkers.layers;

import net.pl3x.map.core.markers.marker.Icon;
import net.pl3x.map.core.markers.marker.Marker;
import net.pl3x.map.core.world.World;
import org.jetbrains.annotations.NotNull;

public class NetherPortalWorldLayer extends SimpleWorldLayer {

    public NetherPortalWorldLayer(@NotNull World world) {
        super("nether_portal", "nether_portals", "Nether Portals", "Nether Portal", world);
    }

    @Override
    protected void load() {

    }

    @Override
    public void addMarker(int x, int z) {
        if (hasMarker(x, z)) return;
        Marker<Icon> marker = createMarker(x, z);
        addMarker(marker);
    }

    @Override
    public void removeMarker(int x, int z) {
        super.removeMarker(x + ":" + z);
    }

}