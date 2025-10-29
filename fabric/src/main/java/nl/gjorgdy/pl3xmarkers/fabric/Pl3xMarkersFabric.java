package nl.gjorgdy.pl3xmarkers.fabric;

import net.fabricmc.api.DedicatedServerModInitializer;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerLifecycleEvents;
import net.fabricmc.loader.api.FabricLoader;
import nl.gjorgdy.pl3xmarkers.core.Layers;
import nl.gjorgdy.pl3xmarkers.core.Pl3xMarkersCore;
import nl.gjorgdy.pl3xmarkers.fabric.compat.layers.OPACAreaMarkerLayer;
import nl.gjorgdy.pl3xmarkers.core.json.JsonStorage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@SuppressWarnings("unused") // Called by fabric
public class Pl3xMarkersFabric implements DedicatedServerModInitializer {

    public static Logger LOGGER = LoggerFactory.getLogger(Pl3xMarkersCore.class);

	public static boolean isOpacLoaded() {
		return FabricLoader.getInstance().isModLoaded("openpartiesandclaims");
	}

    @Override
    public void onInitializeServer() {
		// register layers
		if (isOpacLoaded()) Layers.registerLayerFactory(OPACAreaMarkerLayer::new);
		// initialize core
		var storage = new JsonStorage();
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
