package nl.gjorgdy.pl3xMarkers.paper.listeners;

import net.kyori.adventure.text.TextComponent;
import nl.gjorgdy.pl3xMarkers.paper.helpers.FeedbackHelper;
import nl.gjorgdy.pl3xMarkers.paper.helpers.PortalHelper;
import nl.gjorgdy.pl3xmarkers.core.MarkersConfig;
import nl.gjorgdy.pl3xmarkers.core.Pl3xMarkersCore;
import nl.gjorgdy.pl3xmarkers.core.layers.EndPortalMarkerLayer;
import nl.gjorgdy.pl3xmarkers.core.objects.InteractionResult;
import nl.gjorgdy.pl3xmarkers.core.registries.Layers;
import org.bukkit.GameMode;
import org.bukkit.Material;
import org.bukkit.block.BlockType;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerPortalEvent;
import org.bukkit.inventory.EquipmentSlot;

public class EndPortalListener implements Listener {

    @EventHandler
    public void onPortalTeleport(PlayerPortalEvent event) {
        if (event.getFrom().getBlock().getType().asBlockType() == BlockType.END_PORTAL) {
            var center = PortalHelper.getEndPortalCenter(event.getFrom());
            if (center == null) {
                return;
            }
            var markerLayer = Pl3xMarkersCore.api()
                    .getWorld(center.getWorld().getName())
                    .getLayer(EndPortalMarkerLayer.class, Layers.Keys.END_PORTALS);
            if (markerLayer == null) {
                return;
            }
            var result = markerLayer.add(center.getBlockX(), center.getBlockY(), center.getBlockZ());
			FeedbackHelper.sendFeedback(result, event.getPlayer());
        }
    }

    @EventHandler
    public void onUseNametag(PlayerInteractEvent event) {
        if (event.getClickedBlock() == null || event.getHand() == EquipmentSlot.OFF_HAND || !MarkersConfig.END_PORTAL_MARKERS_RENAME) {
            return;
        }
        if (event.getClickedBlock().getType().equals(Material.END_PORTAL)) {
            var loc = event.getClickedBlock().getLocation();
            var center = PortalHelper.getEndPortalCenter(loc);
            if (center == null) {
                return;
            }
            var markerLayer = Pl3xMarkersCore.api()
                    .getWorld(loc.getWorld().getName())
                    .getLayer(EndPortalMarkerLayer.class, Layers.Keys.END_PORTALS);
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
                            center.getBlockX(), center.getBlockY(), center.getBlockZ(), text.content()
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
                        center.getBlockX(), center.getBlockY(), center.getBlockZ()
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
