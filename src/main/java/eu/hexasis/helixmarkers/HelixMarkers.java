package eu.hexasis.helixmarkers;

import eu.hexasis.helixcore.HelixCore;
import eu.hexasis.helixmarkers.layers.AreaMarkerLayer;
import eu.hexasis.helixmarkers.layers.EndPortalMarkerLayer;
import eu.hexasis.helixmarkers.layers.NetherPortalMarkerLayer;
import eu.hexasis.helixmarkers.layers.SimpleIconMarkerLayer;
import eu.hexasis.helixmarkers.repositories.AreaRepository;
import net.fabricmc.api.DedicatedServerModInitializer;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerLifecycleEvents;
import net.pl3x.map.core.Pl3xMap;
import org.intellij.lang.annotations.Language;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.Connection;

public class HelixMarkers implements ModInitializer, DedicatedServerModInitializer {

    public static Connection DATABASE = null;
    public static AreaRepository AREA_REPOSITORY = null;
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
        @Language("SQL") String markersQuery = """
                    CREATE TABLE IF NOT EXISTS markers (
                        world VARCHAR(32),
                        layer VARCHAR(32),
                        x int,
                        z int,
                        PRIMARY KEY (world, layer, x, z)
                    );
                    """;
        @Language("SQL") String areasQuery = """
                    CREATE TABLE IF NOT EXISTS areas (
                        world VARCHAR(32),
                        label VARCHAR(32),
                        color int,
                        x int,
                        z int,
                        PRIMARY KEY (world, x, z)
                    );
                    """;
        DATABASE = HelixCore.api().getStorage("markers", markersQuery, areasQuery);
        AREA_REPOSITORY = new AreaRepository(DATABASE);
        // register default markers
        api().registerMarkerLayer(w -> new SimpleIconMarkerLayer("beacon", "beacons", "Beacons", "Beacon", w));
        api().registerIcon("/assets/helix/markers/icons/", "beacon", "png");
        api().registerMarkerLayer(w -> new EndPortalMarkerLayer("end_portal", "end_portals", "End Portals", "End Portal", w));
        api().registerIcon("/assets/helix/markers/icons/", "end_portal", "png");
        api().registerMarkerLayer(w -> new NetherPortalMarkerLayer("nether_portal", "nether_portals", "Nether Portals", "Nether Portal", w));
        api().registerIcon("/assets/helix/markers/icons/", "nether_portal", "png");
        api().registerMarkerLayer(w -> new AreaMarkerLayer("areas", "Areas", w));
        // register events
        ServerLifecycleEvents.SERVER_STARTING.register(
                server -> Pl3xMap.api().getEventRegistry().register(apiHandler())
        );
        ServerLifecycleEvents.SERVER_STOPPED.register(
            unused -> api().executor.shutdown()
        );
    }

    @Override
    public void onInitialize() {
    }

}
