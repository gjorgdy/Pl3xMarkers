package nl.gjorgdy.pl3xmarkers;

import net.fabricmc.api.DedicatedServerModInitializer;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerLifecycleEvents;
import net.pl3x.map.core.Pl3xMap;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Pl3xMarkers implements DedicatedServerModInitializer {

    public static Logger LOGGER = LoggerFactory.getLogger(Pl3xMarkersCore.class);

    @Override
    public void onInitializeServer() {
        Pl3xMarkersCore.onInitialize();
        // register events
        ServerLifecycleEvents.SERVER_STARTING.register(
            unused -> Pl3xMarkersCore.onStarted()
        );
        ServerLifecycleEvents.SERVER_STOPPED.register(
            unused -> Pl3xMarkersCore.onDisable()
        );
    }

}
