package nl.gjorgdy.pl3xmarkers.core;

import net.pl3x.map.core.Pl3xMap;
import nl.gjorgdy.pl3xmarkers.core.interfaces.IStorage;

public class Pl3xMarkersCore {

    private static boolean IS_BUKKIT = false;
    private static IStorage STORAGE = null;

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

    public static IStorage storage() {
        if (STORAGE == null) {
			throw new RuntimeException("Failed to create/read database ");
        }
        return STORAGE;
    }

    public static void onInitialize(boolean isBukkit, IStorage storage) {
		STORAGE = storage;
        IS_BUKKIT = isBukkit;
    }

    public static void onStarted() {
        Pl3xMap.api().getEventRegistry().register(Pl3xMarkersCore.pl3xHandler());
    }

    public static void onDisable() {
        Pl3xMarkersCore.storage().close();
        Pl3xMarkersCore.api().executor.shutdown();
    }

}
