package nl.gjorgdy.pl3xMarkers.paper.listeners;

import nl.gjorgdy.pl3xmarkers.core.Pl3xMarkersCore;
import nl.gjorgdy.pl3xmarkers.core.json.JsonStorage;
import nl.gjorgdy.pl3xmarkers.core.json_old.OldJsonStorage;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.server.ServerLoadEvent;
import org.bukkit.event.world.WorldSaveEvent;
import org.bukkit.plugin.java.JavaPlugin;

public class SaveListener implements Listener {

	private final JsonStorage storage;
	private final JavaPlugin plugin;

	public SaveListener(JsonStorage storage, JavaPlugin plugin) {
		this.storage = storage;
		this.plugin = plugin;
	}

	@EventHandler
	@SuppressWarnings("unused") // Event
	public void onSave(WorldSaveEvent event) {
		storage.write();
	}

	// MIGRATION LOGIC - TO BE REMOVED BEFORE RELEASE
	@EventHandler
	public void onServerLoad(ServerLoadEvent event) {
		var oldStorage = new OldJsonStorage();
		if (oldStorage.folderExists()) {
			plugin.getLogger().info("Migrating data from old storage...");
			storage.migrate(oldStorage);
			oldStorage.rename();
			Pl3xMarkersCore.reloadMarkers();
			plugin.getLogger().info("Migration complete!");
		}
	}
	// MIGRATION LOGIC - TO BE REMOVED BEFORE RELEASE

}
