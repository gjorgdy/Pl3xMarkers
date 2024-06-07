package eu.hexasis.helixmarkers.layers;

import net.pl3x.map.core.markers.marker.Icon;
import net.pl3x.map.core.markers.marker.Marker;
import net.pl3x.map.core.world.World;
import org.jetbrains.annotations.NotNull;

public class EndPortalWorldLayer extends SimpleWorldLayer {

    public EndPortalWorldLayer(@NotNull World world) {
        super("end_portal", "end_portals", "End Portals", "End Portal", world);
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