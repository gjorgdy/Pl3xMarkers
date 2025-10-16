package nl.gjorgdy.pl3xMarkers;

import nl.gjorgdy.pl3xmarkers.Pl3xMarkersCore;
import org.bukkit.event.Listener;
import org.bukkit.plugin.java.JavaPlugin;

public final class Pl3xMarkers extends JavaPlugin {

    @Override
    public void onEnable() {
        registerEvents(
                new NetherPortalListener(),
                new BeaconListener()
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
