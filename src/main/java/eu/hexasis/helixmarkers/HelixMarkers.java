package eu.hexasis.helixmarkers;

import eu.hexasis.helixmarkers.layers.AreaMarkerLayer;
import eu.hexasis.helixmarkers.layers.EndPortalIconMarkerLayer;
import eu.hexasis.helixmarkers.layers.NetherPortalIconMarkerLayer;
import eu.hexasis.helixmarkers.layers.IconMarkerLayer;
import eu.hexasis.helixmarkers.repositories.AreaRepository;
import eu.hexasis.helixmarkers.repositories.IconMarkerRepository;
import net.fabricmc.api.DedicatedServerModInitializer;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerLifecycleEvents;
import net.pl3x.map.core.Pl3xMap;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.SQLException;

public class HelixMarkers implements DedicatedServerModInitializer {

    private static Database DATABASE = null;
    private static AreaRepository AREA_REPOSITORY = null;
    private static IconMarkerRepository MARKER_REPOSITORY = null;
    public static Logger LOGGER = LoggerFactory.getLogger(HelixMarkers.class);

    private static Api API = null;
    static Pl3xHandler PL3X_HANDLER = null;

    public static Api api() {
        if (API == null) {
            API = new Api();
        }
        return API;
    }

    static Pl3xHandler pl3xHandler() {
        if (PL3X_HANDLER == null) {
            PL3X_HANDLER = new Pl3xHandler();
        }
        return PL3X_HANDLER;
    }

    private static Database database() {
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

    public static AreaRepository areaRepository() {
        if (AREA_REPOSITORY == null) {
            AREA_REPOSITORY = new AreaRepository(database());
        }
        return AREA_REPOSITORY;
    }

    public static IconMarkerRepository iconMarkerRepository() {
        if (MARKER_REPOSITORY == null) {
            MARKER_REPOSITORY = new IconMarkerRepository(database());
        }
        return MARKER_REPOSITORY;
    }

    @Override
    public void onInitializeServer() {
        // register default icons
        String beaconIconImage = api().registerIconImage("/assets/helix/markers/icons/", "beacon", "png");
        String endPortalIconImage = api().registerIconImage("/assets/helix/markers/icons/", "end_portal", "png");
        String netherPortalIconImage = api().registerIconImage("/assets/helix/markers/icons/", "nether_portal", "png");
        // register default markers
        api().registerMarkerLayer(w -> new IconMarkerLayer(beaconIconImage, "beacons", "Beacons", "Beacon", w));
        api().registerMarkerLayer(w -> new EndPortalIconMarkerLayer(endPortalIconImage, "end_portals", "End Portals", "End Portal", w));
        api().registerMarkerLayer(w -> new NetherPortalIconMarkerLayer(netherPortalIconImage, "nether_portals", "Nether Portals", "Nether Portal", w));
        api().registerMarkerLayer(w -> new AreaMarkerLayer("areas", "Areas", w));
        // register events
        ServerLifecycleEvents.SERVER_STARTING.register(
            server -> Pl3xMap.api().getEventRegistry().register(pl3xHandler())
        );
        ServerLifecycleEvents.SERVER_STOPPED.register(
            unused -> {
                database().close();
                api().executor.shutdown();
            }
        );
    }

}
