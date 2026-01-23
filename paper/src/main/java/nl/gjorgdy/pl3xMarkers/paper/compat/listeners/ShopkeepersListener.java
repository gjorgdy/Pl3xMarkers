package nl.gjorgdy.pl3xMarkers.paper.compat.listeners;

import com.nisovin.shopkeepers.api.events.*;
import nl.gjorgdy.pl3xMarkers.paper.compat.layers.ShopkeepersMarkerLayer;
import nl.gjorgdy.pl3xmarkers.core.Pl3xMarkersCore;
import nl.gjorgdy.pl3xmarkers.core.registries.Layers;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.jspecify.annotations.Nullable;

public class ShopkeepersListener implements Listener {

	@EventHandler
	public void onShopkeeperAdd(ShopkeeperAddedEvent event) {
		updateShopkeeper(event, false);
	}

	@EventHandler
	public void onShopkeeperEdit(ShopkeeperEditedEvent event) {
		updateShopkeeper(event, false);
	}

	@EventHandler
	public void onShopkeeperTrade(ShopkeeperTradeEvent event) {
		updateShopkeeper(event, false);
	}

	@EventHandler
	public void onRemoveShopkeeper(ShopkeeperRemoveEvent event) {
		updateShopkeeper(event, true);
	}

	private void updateShopkeeper(ShopkeeperEvent event, boolean remove) {
		var location = event.getShopkeeper().getLocation();
		if (location == null) {
			return;
		}
		var layer = getLayer(location.getWorld().getName());
		if (layer == null) {
			return;
		}
		if (remove) {
			layer.removeShopkeeper(event.getShopkeeper());
		} else {
			layer.loadShopkeeper(event.getShopkeeper());
		}
	}

	@Nullable
	private ShopkeepersMarkerLayer getLayer(String worldIdentifier) {
		return Pl3xMarkersCore.api().getMarkerLayer(worldIdentifier, Layers.Keys.SHOPKEEPERS, ShopkeepersMarkerLayer.class);
	}

}
