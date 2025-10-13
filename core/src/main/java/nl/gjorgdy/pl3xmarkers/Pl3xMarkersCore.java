package nl.gjorgdy.pl3xmarkers;

import nl.gjorgdy.pl3xmarkers.repositories.AreaRepository;
import nl.gjorgdy.pl3xmarkers.repositories.IconMarkerRepository;
import org.sqlite.util.LoggerFactory;

import java.sql.SQLException;

public class Pl3xMarkersCore {

    private static Database DATABASE = null;
    private static AreaRepository AREA_REPOSITORY = null;
    private static IconMarkerRepository MARKER_REPOSITORY = null;
//    public static System.Logger LOGGER = LoggerFactory.getLogger(Pl3xMarkersCore.class);

    private static Api API = null;
    static Pl3xMapHandler PL3X_MAP_HANDLER = null;

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

}
