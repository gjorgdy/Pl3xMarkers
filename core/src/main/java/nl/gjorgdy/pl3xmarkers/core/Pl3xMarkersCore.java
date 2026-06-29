package nl.gjorgdy.pl3xmarkers.core;

import net.pl3x.map.core.Pl3xMap;
import nl.gjorgdy.pl3xmarkers.core.interfaces.ILogger;
import nl.gjorgdy.pl3xmarkers.core.interfaces.IStorage;
import nl.gjorgdy.pl3xmarkers.core.interfaces.api.IApi;

import java.nio.file.Path;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

public class Pl3xMarkersCore {

	private static final ExecutorService executor = new ThreadPoolExecutor(2, 8, 5L, TimeUnit.SECONDS,new LinkedBlockingQueue<>(1000));
    private static boolean IS_BUKKIT = false;
    private static IStorage STORAGE = null;
	private static ILogger LOGGER = null;
	private static IApi API = null;
    private static Pl3xMapHandler PL3X_MAP_HANDLER = null;
	private static Runnable RELOAD_CONFIG = MarkersConfig::reload;

	public static void print(String msg) {
		if (LOGGER != null) {
			LOGGER.print("[INFO] " + msg);
		}
	}

	public static boolean isBukkit() {
        return IS_BUKKIT;
    }

	public static void reload() {
		RELOAD_CONFIG.run();
		API = new Api();
		print("Loaded config and markers");
	}

	public static boolean isFeedbackDisabled() {
		return !MarkersConfig.FEEDBACK_MESSAGES_ENABLED && !MarkersConfig.FEEDBACK_SOUNDS_ENABLED;
	}

	public static boolean areFeedbackMessagesEnabled() {
		return MarkersConfig.FEEDBACK_MESSAGES_ENABLED;
	}

	public static boolean areFeedbackSoundsEnabled() {
		return MarkersConfig.FEEDBACK_SOUNDS_ENABLED;
	}

	public static Path getMainDir() {
		return isBukkit() ? Path.of("plugins/Pl3xMarkers") : Path.of("config/pl3xmarkers");
	}

	public static IApi api() {
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

	public static void runParallel(Runnable task) {
		executor.execute(task);
	}

	public static void onInitialize(boolean isBukkit, IStorage storage, ILogger logger, Runnable reloadConfig) {
		Pl3xMarkersCore.RELOAD_CONFIG = reloadConfig;
		LOGGER = logger;
		STORAGE = storage;
		IS_BUKKIT = isBukkit;
	}

    public static void onStarted() {
        Pl3xMap.api().getEventRegistry().register(Pl3xMarkersCore.pl3xHandler());
    }

    public static void onDisable() {
        Pl3xMarkersCore.storage().close();
        executor.shutdown();
    }

	public static void reloadMarkers() {
		pl3xHandler().reload();
	}

}
