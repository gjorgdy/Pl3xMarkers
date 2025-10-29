package nl.gjorgdy.pl3xMarkers.paper.listeners;

import net.kyori.adventure.text.serializer.plain.PlainTextComponentSerializer;
import nl.gjorgdy.pl3xMarkers.paper.helpers.FeedbackHelper;
import nl.gjorgdy.pl3xmarkers.core.Pl3xMarkersCore;
import org.bukkit.Location;
import org.bukkit.block.Banner;
import org.bukkit.block.BlockType;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;

public class NodeListener implements Listener {

    @EventHandler
    public void onPlaceBlock(BlockPlaceEvent event) {
        if (event.getBlock().getState() instanceof Banner banner && isLodestoneBelow(banner.getLocation())) {
            onPlaceNode(banner, event.getPlayer());
        }
    }

    @EventHandler
    public void onBreakBlock(BlockBreakEvent event) {
        if (event.getBlock().getType().asBlockType() == BlockType.LODESTONE) {
            var blockUp = event.getBlock().getLocation().clone().add(0, 1, 0).getBlock();
            if (blockUp.getState() instanceof Banner banner) {
                onBreakNode(banner, event.getPlayer());
            }
        }
        else if (event.getBlock().getState() instanceof Banner banner && isLodestoneBelow(banner.getLocation())) {
            onBreakNode(banner, event.getPlayer());
        }
    }

    private boolean isLodestoneBelow(Location location) {
        var blockBelow = location.clone().add(0, -1, 0).getBlock();
        return blockBelow.getType().asBlockType() == BlockType.LODESTONE;
    }

    /**
     * Called when a banner is placed on a lodestone
     * @param banner The banner that was placed
     */
    private void onPlaceNode(Banner banner, Player player) {
        var name = banner.customName();
        if (name == null) return;
        var result = Pl3xMarkersCore.api().addAreaPoint(
                banner.getLocation().getWorld().getName(),
                PlainTextComponentSerializer.plainText().serialize(name),
                banner.getBaseColor().getColor().asRGB(),
                banner.getLocation().getBlockX(),
                banner.getLocation().getBlockZ()
        );
		FeedbackHelper.sendFeedback(result, player);
    }

    /**
     * Called when a banner is broken on a lodestone
     * @param banner The banner that was broken
     */
    private void onBreakNode(Banner banner, Player player) {
        var name = banner.customName();
        if (name == null) return;
        var result = Pl3xMarkersCore.api().removeAreaPoint(
                banner.getLocation().getWorld().getName(),
                PlainTextComponentSerializer.plainText().serialize(name),
                banner.getBaseColor().getColor().asRGB(),
                banner.getLocation().getBlockX(),
                banner.getLocation().getBlockZ()
        );
		FeedbackHelper.sendFeedback(result, player);
    }

}
