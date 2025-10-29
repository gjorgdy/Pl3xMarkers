package nl.gjorgdy.pl3xmarkers.fabric.helpers;

import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvent;
import net.minecraft.sound.SoundEvents;
import net.minecraft.text.Style;
import net.minecraft.text.Text;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Box;
import net.minecraft.world.World;
import nl.gjorgdy.pl3xmarkers.core.Pl3xMarkersCore;
import nl.gjorgdy.pl3xmarkers.core.objects.InteractionResult;

public class FeedbackHelper {

	public static void sendFeedback(InteractionResult result, ServerPlayerEntity player) {
		if (result.state() == InteractionResult.State.SKIP || Pl3xMarkersCore.isFeedbackDisabled()) {
			return;
		}
		// send message
		if (Pl3xMarkersCore.areFeedbackMessagesEnabled()) {
			player.sendMessage(
					Text.of(result.message()).getWithStyle(Style.EMPTY.withColor(color(result.state()))).getFirst(),
					true
			);
		}
		// play sound
		if (Pl3xMarkersCore.areFeedbackSoundsEnabled()) {
			player.playSoundToPlayer(sound(result.state()), SoundCategory.UI, 1.0F, 1.0F);
		}
	}

	public static void sendFeedback(InteractionResult result, World world, BlockPos pos) {
		if (result.state() == InteractionResult.State.SKIP) {
			return;
		}
		var color = color(result.state());
		var sound = sound(result.state());
		// send message
		world.getNonSpectatingEntities(ServerPlayerEntity.class, box(pos)).forEach(player -> {
			// send message
			if (Pl3xMarkersCore.areFeedbackMessagesEnabled()) {
				player.sendMessage(
						Text.of(result.message()).getWithStyle(Style.EMPTY.withColor(color)).getFirst(),
						true
				);
			}
			// play sound
			if (Pl3xMarkersCore.areFeedbackSoundsEnabled()) {
				player.playSoundToPlayer(sound, SoundCategory.UI, 1.0F, 1.5F);
			}
		});
	}

	private static Box box(BlockPos center) {
		return new Box(
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
			case ADDED -> SoundEvents.ENTITY_EXPERIENCE_ORB_PICKUP;
			case REMOVED -> SoundEvents.BLOCK_LAVA_EXTINGUISH;
			case FAILURE -> SoundEvents.ENTITY_VILLAGER_NO;
			default -> SoundEvents.BLOCK_NETHER_WOOD_HIT;
		};
	}

}
