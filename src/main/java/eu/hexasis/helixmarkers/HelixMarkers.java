package eu.hexasis.helixmarkers;

import eu.hexasis.helixmarkers.layers.SimpleMarkerLayer;
import net.fabricmc.api.DedicatedServerModInitializer;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerLifecycleEvents;
import net.pl3x.map.core.Pl3xMap;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.Connection;

public class HelixMarkers implements ModInitializer, DedicatedServerModInitializer {

    public static Connection LOCAL_STORAGE = null;
    public static Logger LOGGER = LoggerFactory.getLogger(HelixMarkers.class);

    private static ApiHandler API = null;

    public static ApiHandler api() {
        if (API == null) {
            API = new ApiHandler();
        }
        return API;
    }

    @Override
    public void onInitializeServer() {
        LOCAL_STORAGE = new eu.hexasis.helixmarkers.SQLite().connection;
        // register default markers
        api().registerMarkerLayer(w -> new SimpleMarkerLayer("beacon", "beacons", "Beacons", "Beacon", w));
        api().registerIcon("/assets/helix/markers/icons/", "beacon", "png");
        api().registerMarkerLayer(w -> new SimpleMarkerLayer("end_portal", "end_portals", "End Portals", "End Portal", w));
        api().registerIcon("/assets/helix/markers/icons/", "end_portal", "png");
        api().registerMarkerLayer(w -> new SimpleMarkerLayer("nether_portal", "nether_portals", "Nether Portals", "Nether Portal", w));
        api().registerIcon("/assets/helix/markers/icons/", "nether_portal", "png");
        // register event
        ServerLifecycleEvents.SERVER_STARTING.register(
                server -> Pl3xMap.api().getEventRegistry().register(API.getEventListener())
        );
    }

    @Override
    public void onInitialize() {
    }

}
