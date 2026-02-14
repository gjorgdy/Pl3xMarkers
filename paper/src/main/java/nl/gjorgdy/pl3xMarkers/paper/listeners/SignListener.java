package nl.gjorgdy.pl3xMarkers.paper.listeners;

import com.destroystokyo.paper.event.block.BlockDestroyEvent;
import net.kyori.adventure.text.serializer.plain.PlainTextComponentSerializer;
import nl.gjorgdy.pl3xMarkers.paper.helpers.FeedbackHelper;
import nl.gjorgdy.pl3xmarkers.core.Pl3xMarkersCore;
import nl.gjorgdy.pl3xmarkers.core.objects.InteractionResult;
import org.bukkit.block.BlockType;
import org.bukkit.block.data.type.Sign;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.SignChangeEvent;
import org.intellij.lang.annotations.Language;

import java.util.Arrays;

public class SignListener implements Listener {

	@EventHandler
	public void onSignChange(SignChangeEvent event) {
		// early return if the block is a wall sign
		if (!(event.getBlock().getBlockData() instanceof Sign)) {
			return;
		}
		var world = event.getBlock().getWorld();
		var location = event.getBlock().getLocation();
		if (world.getBlockAt(location.add(0, -1, 0)).getType().asBlockType() == BlockType.LODESTONE) {
			@Language("HTML") var text = event.lines().stream()
												 .map(component -> PlainTextComponentSerializer.plainText().serialize(component))
												 .toArray(String[]::new);
			InteractionResult result;
			if (Arrays.stream(text).allMatch(String::isBlank)) {
				result = Pl3xMarkersCore.api().removeSignMarker(
						world.getName(),
						location.getBlockX(),
						location.getBlockZ()
				);
			} else {
				result = Pl3xMarkersCore.api().editSignMarker(
						world.getName(),
						location.getBlockX(),
						location.getBlockZ(),
						text
				);
			}
			FeedbackHelper.sendFeedback(result, event.getPlayer());
		}
	}

	@EventHandler
	public void onSignBreak(BlockBreakEvent event) {
		if (event.getBlock().getBlockData() instanceof Sign) {
			var world = event.getBlock().getWorld();
			var location = event.getBlock().getLocation();
			if (world.getBlockAt(location.add(0, -1, 0)).getType().asBlockType() == BlockType.LODESTONE) {
				var result = Pl3xMarkersCore.api().removeSignMarker(
						world.getName(),
						location.getBlockX(),
						location.getBlockZ()
				);
				FeedbackHelper.sendFeedback(result, event.getPlayer());
			}
		}
	}

	@EventHandler
	public void onSignPhysics(BlockDestroyEvent event) {
		if (event.getBlock().getBlockData() instanceof Sign) {
			var world = event.getBlock().getWorld();
			var location = event.getBlock().getLocation();
			var result = Pl3xMarkersCore.api().removeSignMarker(
					world.getName(),
					location.getBlockX(),
					location.getBlockZ()
			);
			FeedbackHelper.sendFeedback(result, location);
		}
	}
}
