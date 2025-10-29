package nl.gjorgdy.pl3xMarkers.paper.listeners;

import nl.gjorgdy.pl3xmarkers.core.json.JsonStorage;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.world.WorldSaveEvent;

public class SaveListener implements Listener {

	private final JsonStorage storage;

	public SaveListener(JsonStorage storage) {
		this.storage = storage;
	}

	@EventHandler
	@SuppressWarnings("unused") // Event
	public void onSave(WorldSaveEvent event) {
		storage.write();
	}

}
