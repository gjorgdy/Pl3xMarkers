package eu.hexasis.helixmarkers;

import eu.hexasis.helixcore.HelixCore;
import eu.hexasis.helixmarkers.layers.SimpleIconMarkerLayer;
import net.fabricmc.api.DedicatedServerModInitializer;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerLifecycleEvents;
import net.pl3x.map.core.Pl3xMap;
import org.intellij.lang.annotations.Language;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.Connection;

public class HelixMarkers implements ModInitializer, DedicatedServerModInitializer {

    public static Connection LOCAL_STORAGE = null;
    public static Logger LOGGER = LoggerFactory.getLogger(HelixMarkers.class);

    private static Api API = null;
    static ApiHandler API_HANDLER = null;

    public static Api api() {
        if (API == null) {
            API = new Api();
        }
        return API;
    }

    static ApiHandler apiHandler() {
        if (API_HANDLER == null) {
            API_HANDLER = new ApiHandler();
        }
        return API_HANDLER;
    }

    @Override
    public void onInitializeServer() {
        // load SQLite Database
        @Language("SQL") String tableQuery = """
                    CREATE TABLE IF NOT EXISTS markers (
                        world VARCHAR(32),
                        layer VARCHAR(32),
                        x int,
                        z int,
                        PRIMARY KEY (world, layer, x, z)
                    )
                    """;
        LOCAL_STORAGE = HelixCore.api().getStorage("markers", tableQuery);
        // register default markers
        api().registerMarkerLayer(w -> new SimpleIconMarkerLayer("beacon", "beacons", "Beacons", "Beacon", w));
        api().registerIcon("/assets/helix/markers/icons/", "beacon", "png");
        api().registerMarkerLayer(w -> new SimpleIconMarkerLayer("end_portal", "end_portals", "End Portals", "End Portal", w));
        api().registerIcon("/assets/helix/markers/icons/", "end_portal", "png");
        api().registerMarkerLayer(w -> new SimpleIconMarkerLayer("nether_portal", "nether_portals", "Nether Portals", "Nether Portal", w));
        api().registerIcon("/assets/helix/markers/icons/", "nether_portal", "png");
        api().registerMarkerLayer(w -> new SimpleIconMarkerLayer("lodestone", "lodestones", "Lodestones", "Lodestone", w));
        api().registerIcon("/assets/helix/markers/icons/", "lodestone", "png");
        // register event
        ServerLifecycleEvents.SERVER_STARTING.register(
                server -> Pl3xMap.api().getEventRegistry().register(apiHandler())
        );
    }

    @Override
    public void onInitialize() {
    }

}
