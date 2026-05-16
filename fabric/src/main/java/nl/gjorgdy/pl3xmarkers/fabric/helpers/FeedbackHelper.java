package nl.gjorgdy.pl3xmarkers.fabric.helpers;

import net.minecraft.core.BlockPos;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.Style;
import net.minecraft.network.protocol.game.ClientboundSoundEntityPacket;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
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
			playDirectSound(player, sound(result.state()), SoundSource.UI, 1.0F, 1.5F);
		}
	}

	public static void sendFeedback(InteractionResult result, Level level, BlockPos pos) {
		if (result.state() == InteractionResult.State.SKIP) {
			return;
		}
		var color = color(result.state());
		var sound = sound(result.state());
		// send message
		level.getEntitiesOfClass(ServerPlayer.class, box(pos)).forEach(player -> {
			// send message
			if (Pl3xMarkersCore.areFeedbackMessagesEnabled()) {
				sendOverlayMessage(player, result.message(), color);
			}
		});
		level.playSound(null, pos, sound, SoundSource.UI, 1.0F, 1.5F);
	}

	public static void sendOverlayMessage(ServerPlayer player, String message, int color) {
		player.sendOverlayMessage(
				Component.nullToEmpty(message).toFlatList(Style.EMPTY.withColor(color)).getFirst()
		);
	}

	private static void playDirectSound(ServerPlayer player, SoundEvent soundEvent, SoundSource soundCategory, float volume, float pitch) {
		ClientboundSoundEntityPacket packet = new ClientboundSoundEntityPacket(
				BuiltInRegistries.SOUND_EVENT.wrapAsHolder(soundEvent),
				soundCategory,
				player, // The entity "emitting" the sound (can be the player themselves)
				volume,
				pitch,
				player.getRandom().nextLong() // Seed
		);
		player.connection.send(packet);
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
