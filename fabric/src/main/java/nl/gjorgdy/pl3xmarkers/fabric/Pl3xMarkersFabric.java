package nl.gjorgdy.pl3xmarkers.fabric;

import net.fabricmc.api.DedicatedServerModInitializer;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerLifecycleEvents;
import nl.gjorgdy.pl3xmarkers.core.Pl3xMarkersCore;
import nl.gjorgdy.pl3xmarkers.json.JsonStorage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@SuppressWarnings("unused") // Called by fabric
public class Pl3xMarkersFabric implements DedicatedServerModInitializer {

    public static Logger LOGGER = LoggerFactory.getLogger(Pl3xMarkersCore.class);

    @Override
    public void onInitializeServer() {
		var storage = new JsonStorage();
		// initialize core
        Pl3xMarkersCore.onInitialize(false, storage);
        // register events
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
