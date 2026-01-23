package nl.gjorgdy.pl3xMarkers.paper;

import nl.gjorgdy.pl3xMarkers.paper.compat.layers.ShopkeepersMarkerLayer;
import nl.gjorgdy.pl3xMarkers.paper.compat.listeners.ShopkeepersListener;
import nl.gjorgdy.pl3xMarkers.paper.listeners.*;
import nl.gjorgdy.pl3xmarkers.core.Pl3xMarkersCore;
import nl.gjorgdy.pl3xmarkers.core.json.JsonStorage;
import nl.gjorgdy.pl3xmarkers.core.objects.Boundary;
import nl.gjorgdy.pl3xmarkers.core.registries.Layers;
import org.bukkit.event.Listener;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.HashMap;
import java.util.UUID;

@SuppressWarnings("unused") // Called by paper
public final class Pl3xMarkersPaper extends JavaPlugin {

    private final HashMap<UUID, Boundary> playerBoundaries = new HashMap<>();

    private final JsonStorage storage = new JsonStorage();

    @Override
    public void onLoad() {
        // Plugin startup logic
        Pl3xMarkersCore.onInitialize(true, storage);
        super.onLoad();
    }

    @Override
    public void onEnable() {
        // register layers
        if (getServer().getPluginManager().isPluginEnabled("Shopkeepers")) {
            Layers.register(ShopkeepersMarkerLayer::new, unused -> true);
            registerEvents(
                    new ShopkeepersListener()
            );
        }
        // register event listeners
        registerEvents(
                new BeaconListener(),
                new EndGatewayListener(),
                new EndPortalListener(),
                new MovementListener(playerBoundaries),
                new NetherPortalListener(),
                new NodeListener(),
                new SaveListener(storage),
                new SignListener()
        );
        // Plugin startup logic
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
