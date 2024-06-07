package eu.hexasis.helixmarkers.layers;

import net.pl3x.map.core.markers.marker.Icon;
import net.pl3x.map.core.markers.marker.Marker;
import net.pl3x.map.core.world.World;
import org.jetbrains.annotations.NotNull;

public class BeaconWorldLayer extends SimpleWorldLayer {

    public BeaconWorldLayer(@NotNull World world) {
        super("beacon", "beacons", "Beacons", "Beacon", world);
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