package nl.gjorgdy.pl3xMarkers.paper;

import nl.gjorgdy.pl3xMarkers.paper.listeners.*;
import nl.gjorgdy.pl3xmarkers.core.Pl3xMarkersCore;
import org.bukkit.event.Listener;
import org.bukkit.plugin.java.JavaPlugin;

public final class Pl3xMarkers extends JavaPlugin {

    @Override
    public void onEnable() {
        registerEvents(
                new BeaconListener(),
                new EndGatewayListener(),
                new EndPortalListener(),
                new NetherPortalListener(),
                new NodeListener()
        );
        // Plugin startup logic
        Pl3xMarkersCore.onInitialize(true);
        Pl3xMarkersCore.onStarted();
    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
        Pl3xMarkersCore.onDisable();
    }

    private void registerEvents(Listener... listener) {
        for (Listener l : listener) {
            getServer().getPluginManager().registerEvents(l, this);
        }
    }
}
