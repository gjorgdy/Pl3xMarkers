package eu.hexasis.helixmarkers;

import eu.hexasis.helixmarkers.layers.LocalWorldLayer;
import eu.hexasis.helixmarkers.objects.IconAddress;
import net.fabricmc.api.DedicatedServerModInitializer;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerLifecycleEvents;
import net.pl3x.map.core.Pl3xMap;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.Connection;

public class HelixMarkers implements DedicatedServerModInitializer {

    public static HelixMarkers INSTANCE;
    public static Connection SQLITE;
    public static Pl3xApiHandler API;
    public static Logger LOGGER = LoggerFactory.getLogger(HelixMarkers.class);

    public HelixMarkers() {
        INSTANCE = this;
        SQLITE = new SQLite().connection;
        API = new Pl3xApiHandler();
        // register default markers
        API.registerSimpleWorldLayer(w -> new LocalWorldLayer("beacon", "beacons", "Beacons", "Beacon", w));
        API.registerIconAddress(new IconAddress("/assets/markers/icons/", "beacon", "png"));
        API.registerSimpleWorldLayer(w -> new LocalWorldLayer("end_portal", "end_portals", "End Portals", "End Portal", w));
        API.registerIconAddress(new IconAddress("/assets/markers/icons/", "end_portal", "png"));
        API.registerSimpleWorldLayer(w -> new LocalWorldLayer("nether_portal", "nether_portals", "Nether Portals", "Nether Portal", w));
        API.registerIconAddress(new IconAddress("/assets/markers/icons/", "nether_portal", "png"));
    }

    @Override
    public void onInitializeServer() {
        // register event
        ServerLifecycleEvents.SERVER_STARTING.register(
            server -> Pl3xMap.api().getEventRegistry().register(API.getEventListener())
        );
    }

}
