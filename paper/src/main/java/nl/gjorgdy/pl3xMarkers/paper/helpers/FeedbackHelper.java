package nl.gjorgdy.pl3xMarkers.paper.helpers;

import net.kyori.adventure.bossbar.BossBar;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.TextColor;
import net.kyori.adventure.title.TitlePart;
import nl.gjorgdy.pl3xmarkers.core.Pl3xMarkersCore;
import nl.gjorgdy.pl3xmarkers.core.objects.InteractionResult;
import org.bukkit.Location;
import org.bukkit.Sound;
import org.bukkit.entity.Player;

import java.util.HashMap;
import java.util.UUID;

public class FeedbackHelper {

	public static HashMap<UUID, BossBar> playerBossBars = new HashMap<>();

	public static void sendFeedback(InteractionResult result, Player player) {
		if (result.state() == InteractionResult.State.SKIP || Pl3xMarkersCore.isFeedbackDisabled()) {
			return;
		}
		// send message
		if (Pl3xMarkersCore.areFeedbackMessagesEnabled()) {
			player.sendActionBar(
					Component.text(result.message()).color(TextColor.color(color(result.state())))
			);
		}
		// play sound
		if (Pl3xMarkersCore.areFeedbackSoundsEnabled()) {
			player.playSound(player.getLocation(), sound(result.state()), 1.0F, 1.5F);
		}
	}

	public static void sendFeedback(InteractionResult result, Location pos) {
		if (result.state() == InteractionResult.State.SKIP) {
			return;
		}
		var color = color(result.state());
		var sound = sound(result.state());
		// send message
		pos.getWorld().getNearbyPlayers(pos, 8).forEach(player -> {
			// send message
			if (Pl3xMarkersCore.areFeedbackMessagesEnabled()) {
				player.sendActionBar(
						Component.text(result.message()).color(TextColor.color(color))
				);
			}
			// play sound
			if (Pl3xMarkersCore.areFeedbackSoundsEnabled()) {
				player.playSound(player.getLocation(), sound, 1.0F, 1.5F);
			}
		});
	}

	public static void sendOverlayMessage(Player player, String message, int color) {
		player.sendActionBar(
				Component.text(message).color(TextColor.color(color))
		);
	}

	public static void sendTitle(Player player, String message, int color) {
		player.sendTitlePart(TitlePart.TITLE, Component.text(message).color(TextColor.color(color)));
		player.sendTitlePart(TitlePart.SUBTITLE, Component.empty());
	}

	public static void sendSubTitle(Player player, String message, int color) {
		player.sendTitlePart(TitlePart.TITLE, Component.empty());
		player.sendTitlePart(TitlePart.SUBTITLE, Component.text(message).color(TextColor.color(color)));
	}

	public static void showBossBar(Player player, String message, int color) {
		playerBossBars.computeIfAbsent(player.getUniqueId(), _ -> {
			                               var bar = BossBar.bossBar(
					                               Component.text(message).color(TextColor.color(color)),
					                               0,
					                               BossBar.Color.PURPLE,
					                               BossBar.Overlay.PROGRESS
			                               );
			                               player.showBossBar(bar);
			                               return bar;
		                               }
		);
	}

	public static void hideBossBar(Player player) {
		playerBossBars.computeIfPresent(player.getUniqueId(), (_, bar) -> {
			                                player.hideBossBar(bar);
			                                return null;
		                                }
		);
	}

	private static int color(InteractionResult.State state) {
		return switch (state) {
			case ADDED -> 0x59ff59;
			case FEEDBACK -> 0x9bb1bb;
			case REMOVED -> 0x6a6a6a;
			case FAILURE -> 0xff5959;
			default -> 0xffffff;
		};
	}

	private static Sound sound(InteractionResult.State state) {
		return switch (state) {
			case ADDED -> Sound.ENTITY_EXPERIENCE_ORB_PICKUP;
			case FEEDBACK -> Sound.UI_BUTTON_CLICK;
			case REMOVED -> Sound.BLOCK_LAVA_EXTINGUISH;
			case FAILURE -> Sound.ENTITY_VILLAGER_NO;
			default -> Sound.BLOCK_NETHER_WOOD_HIT;
		};
	}

}
