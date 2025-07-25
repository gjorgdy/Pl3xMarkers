package eu.hexasis.helixmarkers;

import eu.hexasis.helixmarkers.layers.AreaMarkerLayer;
import eu.hexasis.helixmarkers.layers.EndPortalMarkerLayer;
import eu.hexasis.helixmarkers.layers.NetherPortalMarkerLayer;
import eu.hexasis.helixmarkers.layers.SimpleIconMarkerLayer;
import eu.hexasis.helixmarkers.repositories.AreaRepository;
import net.fabricmc.api.DedicatedServerModInitializer;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerLifecycleEvents;
import net.pl3x.map.core.Pl3xMap;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.SQLException;

public class HelixMarkers implements DedicatedServerModInitializer {

    private static Database DATABASE = null;
    public static AreaRepository AREA_REPOSITORY = null;
    public static Logger LOGGER = LoggerFactory.getLogger(HelixMarkers.class);

    private static Api API = null;
    static Pl3xHandler PL3X_HANDLER = null;

    public static Api api() {
        if (API == null) {
            API = new Api();
        }
        return API;
    }

    static Pl3xHandler apiHandler() {
        if (PL3X_HANDLER == null) {
            PL3X_HANDLER = new Pl3xHandler();
        }
        return PL3X_HANDLER;
    }

    public static Database database() {
        if (DATABASE == null) {
            try {
                DATABASE = new Database();
            } catch (SQLException e) {
                LOGGER.error("Failed to create/read database ", e);
                throw new RuntimeException(e);
            }
        }
        return DATABASE;
    }

    @Override
    public void onInitializeServer() {
        database();
        AREA_REPOSITORY = new AreaRepository();
        // register default icons
        api().registerIcon("/assets/helix/markers/icons/", "beacon", "png");
        api().registerIcon("/assets/helix/markers/icons/", "end_portal", "png");
        api().registerIcon("/assets/helix/markers/icons/", "nether_portal", "png");
        // register default markers
        api().registerMarkerLayer(w -> new SimpleIconMarkerLayer("beacon", "beacons", "Beacons", "Beacon", w));
        api().registerMarkerLayer(w -> new EndPortalMarkerLayer("end_portal", "end_portals", "End Portals", "End Portal", w));
        api().registerMarkerLayer(w -> new NetherPortalMarkerLayer("nether_portal", "nether_portals", "Nether Portals", "Nether Portal", w));
        api().registerMarkerLayer(w -> new AreaMarkerLayer("areas", "Areas", w));
        // register events
        ServerLifecycleEvents.SERVER_STARTING.register(
                server -> Pl3xMap.api().getEventRegistry().register(apiHandler())
        );
        ServerLifecycleEvents.SERVER_STOPPED.register(
            unused -> {
                database().close();
                api().executor.shutdown();
            }
        );
    }

}
