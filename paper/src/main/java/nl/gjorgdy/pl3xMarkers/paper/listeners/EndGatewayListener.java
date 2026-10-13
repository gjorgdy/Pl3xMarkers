package nl.gjorgdy.pl3xMarkers.paper.listeners;

import com.destroystokyo.paper.event.player.PlayerTeleportEndGatewayEvent;
import net.kyori.adventure.text.TextComponent;
import nl.gjorgdy.pl3xMarkers.paper.helpers.FeedbackHelper;
import nl.gjorgdy.pl3xmarkers.core.MarkersConfig;
import nl.gjorgdy.pl3xmarkers.core.Pl3xMarkersCore;
import nl.gjorgdy.pl3xmarkers.core.layers.EndGatewayMarkerLayer;
import nl.gjorgdy.pl3xmarkers.core.objects.InteractionResult;
import nl.gjorgdy.pl3xmarkers.core.registries.Layers;
import org.bukkit.GameMode;
import org.bukkit.Material;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.EquipmentSlot;

public class EndGatewayListener implements Listener {

    @EventHandler
    public void onEnterGateway(PlayerTeleportEndGatewayEvent event) {
        var loc = event.getGateway().getLocation();
	    var markerLayer = Pl3xMarkersCore.api()
			    .getWorld(loc.getWorld().getName())
			    .getLayer(EndGatewayMarkerLayer.class, Layers.Keys.END_GATEWAYS);
	    if (markerLayer == null) {
		    return;
	    }
	    var result = markerLayer
			    .add(loc.getBlockX(), loc.getBlockY(), loc.getBlockZ());
		FeedbackHelper.sendFeedback(result, loc);
        var exit = event.getGateway().getExitLocation();
	    if (exit == null) {
		    return;
	    }
	    markerLayer = Pl3xMarkersCore.api()
			    .getWorld(exit.getWorld().getName())
			    .getLayer(EndGatewayMarkerLayer.class, Layers.Keys.END_GATEWAYS);
	    if (markerLayer == null) {
		    return;
	    }
	    result = markerLayer.add(exit.getBlockX(), exit.getBlockY(), exit.getBlockZ());
		FeedbackHelper.sendFeedback(result, loc);
    }

	@EventHandler
	public void onUseNametag(PlayerInteractEvent event) {
		if (event.getClickedBlock() == null || event.getHand() == EquipmentSlot.OFF_HAND || !MarkersConfig.END_GATEWAY_MARKERS_RENAME) {
			return;
		}
		if (event.getClickedBlock().getType().equals(Material.END_GATEWAY)) {
			var loc = event.getClickedBlock().getLocation();
			var markerLayer = Pl3xMarkersCore.api()
					.getWorld(loc.getWorld().getName())
					.getLayer(EndGatewayMarkerLayer.class, Layers.Keys.END_GATEWAYS);
			if (markerLayer == null) {
				return;
			}
			if (event.getItem() != null && event.getItem().getType().equals(Material.NAME_TAG)) {
				var nameComponent = event.getItem().getItemMeta().customName();
				if (nameComponent == null) {
					return;
				}
				if (nameComponent instanceof TextComponent text) {
					var result = markerLayer.setName(
							loc.getBlockX(), loc.getBlockY(), loc.getBlockZ(), text.content()
					);
					FeedbackHelper.sendFeedback(result, event.getPlayer());
					if (result.state().equals(InteractionResult.State.ADDED)) {
						if (event.getHand() != null) {
							event.getPlayer().swingMainHand();
						}
						if (!event.getPlayer().getGameMode().equals(GameMode.CREATIVE)) {
							event.getItem().subtract(1);
						}
					}
					if (result.state().equals(InteractionResult.State.FEEDBACK)) {
						if (event.getHand() != null) {
							event.getPlayer().swingMainHand();
						}
					}
				}
			} else {
				var result = markerLayer.interact(
						loc.getBlockX(), loc.getBlockY(), loc.getBlockZ()
				);
				FeedbackHelper.sendFeedback(result, event.getPlayer());
				if (result.state().equals(InteractionResult.State.FEEDBACK)) {
					if (event.getHand() != null) {
						event.getPlayer().swingMainHand();
					}
				}
			}
		}
	}

}