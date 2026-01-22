package nl.gjorgdy.pl3xmarkers.fabric;

import net.fabricmc.api.DedicatedServerModInitializer;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerLifecycleEvents;
import net.fabricmc.loader.api.FabricLoader;
import nl.gjorgdy.pl3xmarkers.core.Pl3xMarkersCore;
import nl.gjorgdy.pl3xmarkers.core.json.JsonStorage;
import nl.gjorgdy.pl3xmarkers.core.registries.Layers;
import nl.gjorgdy.pl3xmarkers.fabric.compat.layers.OPACAreaMarkerLayer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@SuppressWarnings("unused") // Called by fabric
public class Pl3xMarkersFabric implements DedicatedServerModInitializer {

    public static Logger LOGGER = LoggerFactory.getLogger(Pl3xMarkersCore.class);

	public static boolean isOpacInstalled() {
		return FabricLoader.getInstance().isModLoaded("openpartiesandclaims");
	}

	public static boolean isOpacEnabled() {
		return isOpacInstalled() && FabricMarkersConfig.OPAC_MARKERS_ENABLED;
	}

    @Override
    public void onInitializeServer() {
		// register layers
		if (isOpacInstalled()) {
			Layers.register(OPACAreaMarkerLayer::new, unused -> isOpacEnabled());
		}
		// initialize core
		var storage = new JsonStorage();
		Pl3xMarkersCore.onInitialize(false, storage, FabricMarkersConfig::reload);
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
