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
        // register default icons
        Icons.register(Pl3xMarkersCore.api());
        // register default markers
        Layers.register(Pl3xMarkersCore.api());
        // register events
        ServerLifecycleEvents.SERVER_STARTING.register(
            server -> Pl3xMap.api().getEventRegistry().register(Pl3xMarkersCore.pl3xHandler())
        );
        ServerLifecycleEvents.SERVER_STOPPED.register(
            unused -> {
                Pl3xMarkersCore.database().close();
                Pl3xMarkersCore.api().executor.shutdown();
            }
        );
    }

}
