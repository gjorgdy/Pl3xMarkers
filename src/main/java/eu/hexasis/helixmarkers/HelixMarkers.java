package eu.hexasis.helixmarkers;

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
        Icons.register(api());
        // register default markers
        Layers.register(api());
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
