package nl.gjorgdy.pl3xmarkers.fabric.helpers;

import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.Style;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.AABB;
import nl.gjorgdy.pl3xmarkers.core.Pl3xMarkersCore;
import nl.gjorgdy.pl3xmarkers.core.objects.InteractionResult;

public class FeedbackHelper {

	public static void sendFeedback(InteractionResult result, ServerPlayer player) {
		if (result.state() == InteractionResult.State.SKIP || Pl3xMarkersCore.isFeedbackDisabled()) {
			return;
		}
		// send message
		if (Pl3xMarkersCore.areFeedbackMessagesEnabled()) {
			sendOverlayMessage(player, result.message(), color(result.state()));
		}
		// play sound
		if (Pl3xMarkersCore.areFeedbackSoundsEnabled()) {
			player.playSound(sound(result.state()), 1.0F, 1.0F);
		}
	}

	public static void sendFeedback(InteractionResult result, Level world, BlockPos pos) {
		if (result.state() == InteractionResult.State.SKIP) {
			return;
		}
		var color = color(result.state());
		var sound = sound(result.state());
		// send message
		world.getEntitiesOfClass(ServerPlayer.class, box(pos)).forEach(player -> {
			// send message
			if (Pl3xMarkersCore.areFeedbackMessagesEnabled()) {
				sendOverlayMessage(player, result.message(), color);
			}
			// play sound
			if (Pl3xMarkersCore.areFeedbackSoundsEnabled()) {
				player.playSound(sound, 1.0F, 1.5F);
			}
		});
	}

	public static void sendOverlayMessage(ServerPlayer player, String message, int color) {
		player.sendOverlayMessage(
				Component.nullToEmpty(message).toFlatList(Style.EMPTY.withColor(color)).getFirst()
		);
	}

	private static AABB box(BlockPos center) {
		return new AABB(
			center.getX() - 8, center.getY() - 8, center.getZ() - 8,
			center.getX() + 8, center.getY() + 8, center.getZ() + 8
		);
	}

	private static int color(InteractionResult.State state) {
		return switch (state) {
			case ADDED -> 0x59ff59;
			case REMOVED -> 0x6a6a6a;
			case FAILURE -> 0xff5959;
			default -> 0xffffff;
		};
	}

	private static SoundEvent sound(InteractionResult.State state) {
		return switch (state) {
			case ADDED -> SoundEvents.EXPERIENCE_ORB_PICKUP;
			case REMOVED -> SoundEvents.LAVA_EXTINGUISH;
			case FAILURE -> SoundEvents.VILLAGER_NO;
			default -> SoundEvents.NETHER_WOOD_HIT;
		};
	}

}
