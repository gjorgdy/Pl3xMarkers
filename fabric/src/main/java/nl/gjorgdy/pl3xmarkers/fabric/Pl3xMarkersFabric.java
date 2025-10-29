package nl.gjorgdy.pl3xmarkers.fabric;

import net.fabricmc.api.DedicatedServerModInitializer;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerLifecycleEvents;
import net.fabricmc.loader.api.FabricLoader;
import nl.gjorgdy.pl3xmarkers.core.Pl3xMarkersCore;
import nl.gjorgdy.pl3xmarkers.core.registries.Layers;
import nl.gjorgdy.pl3xmarkers.fabric.compat.layers.OPACAreaMarkerLayer;
import nl.gjorgdy.pl3xmarkers.core.json.JsonStorage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.atomic.AtomicBoolean;

@SuppressWarnings("unused") // Called by fabric
public class Pl3xMarkersFabric implements DedicatedServerModInitializer {

    public static Logger LOGGER = LoggerFactory.getLogger(Pl3xMarkersCore.class);
	private static final AtomicBoolean serverStarted = new AtomicBoolean(false);

	public static boolean isOpacInstalled() {
		return FabricLoader.getInstance().isModLoaded("openpartiesandclaims");
	}

	public static boolean isOpacLoaded() {
		return isOpacInstalled() && serverStarted.get();
	}

    @Override
    public void onInitializeServer() {
		// register layers
		Layers.register(OPACAreaMarkerLayer::new, unused -> isOpacInstalled());
		// initialize core
		var storage = new JsonStorage();
		Pl3xMarkersCore.onInitialize(false, storage);
        // register events
		ServerLifecycleEvents.SERVER_STARTED.register(
			server -> {
				System.out.println("Pl3xMarkersFabric: Server started");
				serverStarted.set(true);
			}
		);
		ServerLifecycleEvents.AFTER_SAVE.register(
			(server, flush, force) -> storage.write()
		);
        ServerLifecycleEvents.SERVER_STARTING.register(
            unused -> Pl3xMarkersCore.onStarted()
        );
        ServerLifecycleEvents.SERVER_STOPPED.register(
            unused -> Pl3xMarkersCore.onDisable()
        );
    }

}
