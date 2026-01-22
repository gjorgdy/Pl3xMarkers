package nl.gjorgdy.pl3xmarkers.fabric.mixin;

import com.llamalad7.mixinextras.injector.wrapmethod.WrapMethod;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.block.entity.SignBlockEntity;
import net.minecraft.block.entity.SignText;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.registry.tag.BlockTags;
import net.minecraft.server.filter.FilteredMessage;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.text.Text;
import net.minecraft.util.math.BlockPos;
import nl.gjorgdy.pl3xmarkers.core.Pl3xMarkersCore;
import nl.gjorgdy.pl3xmarkers.core.objects.InteractionResult;
import nl.gjorgdy.pl3xmarkers.fabric.helpers.FeedbackHelper;
import org.intellij.lang.annotations.Language;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;

import java.util.Arrays;
import java.util.List;

@Mixin(SignBlockEntity.class)
public abstract class SignBlockEntityMixin extends BlockEntity {

	public SignBlockEntityMixin(BlockEntityType<?> type, BlockPos pos, BlockState state) {
		super(type, pos, state);
	}

	@Shadow
	public abstract SignText getText(boolean front);

	@WrapMethod(method = "tryChangeText")
	private void onChangeText(PlayerEntity player, boolean front, List<FilteredMessage> messages, Operation<Void> original) {
		if (isNotMarkerSign() || world == null) {
			original.call(player, front, messages);
			return;
		}

		var textBefore = getText(true).getMessages(false);
		boolean wasEmpty = Arrays.stream(textBefore)
								   .allMatch(text -> text.getLiteralString() == null || text.getLiteralString().isEmpty());
		original.call(player, front, messages);
		var textAfter = getText(true).getMessages(false);

		if (Arrays.equals(textBefore, textAfter)) {
			// no changes
			return;
		}

		InteractionResult result;
		if (wasEmpty) {
			result = Pl3xMarkersCore.api().addSignMarker(
					world.getRegistryKey().getValue().toString(),
					pos.getX(),
					pos.getZ(),
					getSignTextLines()
			);
		} else {
			result = Pl3xMarkersCore.api().editSignMarker(
					world.getRegistryKey().getValue().toString(),
					pos.getX(),
					pos.getZ(),
					getSignTextLines()
			);
		}

		if (player instanceof ServerPlayerEntity serverPlayer) {
			FeedbackHelper.sendFeedback(result, serverPlayer);
		}
	}

	@Unique
	@Language("HTML")
	private String[] getSignTextLines() {
		return Arrays.stream(getText(true).getMessages(false))
					   .map(Text::getString)
					   .toArray(String[]::new);
	}

	@Unique
	private boolean isEmpty() {
		return Arrays.stream(getText(true).getMessages(false))
					   .allMatch(text -> text.getLiteralString() == null || text.getLiteralString().isEmpty());
	}

	@Unique
	private boolean isNotMarkerSign() {
		return world == null
					   || !world.getBlockState(pos.down()).isOf(Blocks.LODESTONE)
					   || !getCachedState().isIn(BlockTags.STANDING_SIGNS);
	}

}
