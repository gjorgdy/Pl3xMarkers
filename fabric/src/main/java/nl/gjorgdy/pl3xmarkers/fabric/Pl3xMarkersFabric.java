package nl.gjorgdy.pl3xmarkers.fabric;

import net.fabricmc.api.DedicatedServerModInitializer;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerLifecycleEvents;
import net.fabricmc.loader.api.FabricLoader;
import nl.gjorgdy.pl3xmarkers.core.Layers;
import nl.gjorgdy.pl3xmarkers.core.Pl3xMarkersCore;
import nl.gjorgdy.pl3xmarkers.fabric.compat.layers.OPACAreaMarkerLayer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

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
        Pl3xMarkersCore.onInitialize(false);
        // register events
        ServerLifecycleEvents.SERVER_STARTING.register(
            unused -> Pl3xMarkersCore.onStarted()
        );
        ServerLifecycleEvents.SERVER_STOPPED.register(
            unused -> Pl3xMarkersCore.onDisable()
        );
    }

}
