package nl.gjorgdy.pl3xmarkers.core;

import net.pl3x.map.core.Pl3xMap;
import nl.gjorgdy.pl3xmarkers.core.interfaces.IStorage;

import java.nio.file.Path;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

public class Pl3xMarkersCore {

    private static boolean IS_BUKKIT = false;
    private static IStorage STORAGE = null;

    private static Api API = null;
    static Pl3xMapHandler PL3X_MAP_HANDLER = null;

	private static final ExecutorService executor = new ThreadPoolExecutor(2, 8, 5L, TimeUnit.SECONDS,new LinkedBlockingQueue<>(1000));

	public static boolean isBukkit() {
        return IS_BUKKIT;
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

	public static void runParallel(Runnable task) {
		executor.execute(task);
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
        executor.shutdown();
    }

}
