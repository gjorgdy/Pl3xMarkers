package nl.gjorgdy.pl3xmarkers.core;

import net.pl3x.map.core.Pl3xMap;
import nl.gjorgdy.pl3xmarkers.core.repositories.AreaRepository;
import nl.gjorgdy.pl3xmarkers.core.repositories.IconMarkerRepository;

import java.sql.SQLException;

public class Pl3xMarkersCore {

    private static boolean IS_BUKKIT = false;
    private static Database DATABASE = null;
    private static AreaRepository AREA_REPOSITORY = null;
    private static IconMarkerRepository MARKER_REPOSITORY = null;

    private static Api API = null;
    static Pl3xMapHandler PL3X_MAP_HANDLER = null;

    public static boolean isBukkit() {
        return IS_BUKKIT;
    }

    public static Api api() {
        if (API == null) {
            API = new Api();
        }
        return API;
    }

    static Pl3xMapHandler pl3xHandler() {
        if (PL3X_MAP_HANDLER == null) {
            PL3X_MAP_HANDLER = new Pl3xMapHandler();
        }
        return PL3X_MAP_HANDLER;
    }

    static Database database() {
        if (DATABASE == null) {
            try {
                DATABASE = new Database();
            } catch (SQLException e) {
                System.out.println("Failed to create/read database ");
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

    public static void onInitialize(boolean isBukkit) {
        IS_BUKKIT = isBukkit;
    }

    public static void onStarted() {
        Pl3xMap.api().getEventRegistry().register(Pl3xMarkersCore.pl3xHandler());
    }

    public static void onDisable() {
        Pl3xMarkersCore.database().close();
        Pl3xMarkersCore.api().executor.shutdown();
    }

}
