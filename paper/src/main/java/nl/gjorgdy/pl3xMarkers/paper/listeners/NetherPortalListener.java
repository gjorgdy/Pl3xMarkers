package nl.gjorgdy.pl3xMarkers.paper.listeners;

import net.kyori.adventure.text.TextComponent;
import nl.gjorgdy.pl3xMarkers.paper.helpers.FeedbackHelper;
import nl.gjorgdy.pl3xMarkers.paper.helpers.PortalHelper;
import nl.gjorgdy.pl3xmarkers.core.Pl3xMarkersCore;
import nl.gjorgdy.pl3xmarkers.core.layers.NetherPortalMarkerLayer;
import nl.gjorgdy.pl3xmarkers.core.objects.InteractionResult;
import nl.gjorgdy.pl3xmarkers.core.registries.Layers;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.BlockType;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerPortalEvent;
import org.bukkit.event.player.PlayerTeleportEvent;
import org.jspecify.annotations.Nullable;

public class NetherPortalListener implements Listener {

    @EventHandler
    public void onPortalTeleport(PlayerPortalEvent event) {
        if (event.getCause() == PlayerTeleportEvent.TeleportCause.NETHER_PORTAL) {
            onNetherPortalTeleport(event.getFrom(), event.getPlayer());
        }
    }

    @EventHandler
    public void onUseNametag(PlayerInteractEvent event) {
        if (event.getItem() == null || event.getClickedBlock() == null) {
            return;
        }
        if (event.getItem().getType().equals(Material.NAME_TAG) && event.getClickedBlock().getType().equals(
                Material.NETHER_PORTAL)) {
            var loc = event.getClickedBlock().getLocation();
            var center = PortalHelper.getNetherPortalCenter(loc);
            if (center == null) {
                return;
            }
            var markerLayer = Pl3xMarkersCore.api()
                    .getWorld(loc.getWorld().getName())
                    .getLayer(NetherPortalMarkerLayer.class, Layers.Keys.NETHER_PORTALS);
            if (markerLayer == null) {
                return;
            }
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
            }
        }
    }

    @EventHandler
    public void onBlockBreak(BlockBreakEvent event) {
        var blockType = event.getBlock().getType().asBlockType();
        var location = event.getBlock().getLocation();
        if (blockType == BlockType.OBSIDIAN) {
            var portal = findNetherPortal(location);
            if (portal != null) {
                onNetherPortalBreak(portal, event.getPlayer());
                return;
            }
        }
        if (blockType == BlockType.NETHER_PORTAL) {
            var center = PortalHelper.getNetherPortalCenter(location);
            if (center != null) {
                onNetherPortalBreak(center, event.getPlayer());
            }
        }
    }

    @Nullable
    private Location findNetherPortal(Location location) {
        var nearby = location.clone();
        for (int x = -1; x <= 1; x++) {
            for (int y = -1; y <= 1; y++) {
                for (int z = -1; z <= 1; z++) {
                    nearby.set(location.getBlockX() + x, location.getBlockY() + y, location.getBlockZ() + z);
                    if (nearby.getBlock().getType().asBlockType() == BlockType.NETHER_PORTAL) {
                        return PortalHelper.getNetherPortalCenter(nearby);
                    }
                }
            }
        }
        return null;
    }

    private void onNetherPortalBreak(Location location, @Nullable Player player) {
        var center = PortalHelper.getNetherPortalCenter(location);
        if (center == null) {
            return;
        }
        var markerLayer = Pl3xMarkersCore.api()
                .getWorld(location.getWorld().getName())
                .getLayer(NetherPortalMarkerLayer.class, Layers.Keys.NETHER_PORTALS);
        if (markerLayer == null) {
            return;
        }
        var result = markerLayer.remove(center.getBlockX(), center.getBlockY(), center.getBlockZ());
        if (player == null) {
            FeedbackHelper.sendFeedback(result, center);
        } else {
            FeedbackHelper.sendFeedback(result, player);
        }
    }

    private void onNetherPortalTeleport(Location location, @Nullable Player player) {
        var portal = findNetherPortal(location);
        if (portal == null) {
            return;
        }
        var markerLayer = Pl3xMarkersCore.api()
                .getWorld(location.getWorld().getName())
                .getLayer(NetherPortalMarkerLayer.class, Layers.Keys.NETHER_PORTALS);
        if (markerLayer == null) {
            return;
        }
        var result = markerLayer.add(portal.getBlockX(), portal.getBlockY(), portal.getBlockZ());
        if (player == null) {
            FeedbackHelper.sendFeedback(result, portal);
        } else {
            FeedbackHelper.sendFeedback(result, player);
        }
    }

}
