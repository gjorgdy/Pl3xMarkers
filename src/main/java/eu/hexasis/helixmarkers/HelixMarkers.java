package eu.hexasis.helixmarkers;

import eu.hexasis.helixmarkers.layers.BeaconWorldLayer;
import eu.hexasis.helixmarkers.layers.EndPortalWorldLayer;
import eu.hexasis.helixmarkers.layers.NetherPortalWorldLayer;
import eu.hexasis.helixmarkers.objects.IconAddress;
import net.fabricmc.api.DedicatedServerModInitializer;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerLifecycleEvents;
import net.pl3x.map.core.Pl3xMap;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class HelixMarkers implements DedicatedServerModInitializer {

    public static HelixMarkers INSTANCE;
    public static SQLite SQLITE;
    public static Pl3xApiHandler PL3X;
    public static Logger LOGGER = LoggerFactory.getLogger(HelixMarkers.class);

    public HelixMarkers() {
        INSTANCE = this;
        SQLITE = new SQLite();
        PL3X = new Pl3xApiHandler();
        // register default markers
        PL3X.registerSimpleWorldLayer(BeaconWorldLayer::new);
        PL3X.registerIconAddress(new IconAddress("/assets/markers/icons/", "beacon", "png"));
        PL3X.registerSimpleWorldLayer(EndPortalWorldLayer::new);
        PL3X.registerIconAddress(new IconAddress("/assets/markers/icons/", "end_portal", "png"));
        PL3X.registerSimpleWorldLayer(NetherPortalWorldLayer::new);
        PL3X.registerIconAddress(new IconAddress("/assets/markers/icons/", "nether_portal", "png"));
    }

    @Override
    public void onInitializeServer() {
        // register event
        ServerLifecycleEvents.SERVER_STARTING.register(
            server -> Pl3xMap.api().getEventRegistry().register(PL3X.getEventListener())
        );
    }

}
