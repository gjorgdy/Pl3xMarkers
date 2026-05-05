package nl.gjorgdy.pl3xmarkers.fabric.mixin;

import com.llamalad7.mixinextras.injector.wrapmethod.WrapMethod;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.server.network.FilteredText;
import net.minecraft.tags.BlockTags;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.entity.SignBlockEntity;
import net.minecraft.world.level.block.entity.SignText;
import net.minecraft.world.level.block.state.BlockState;
import nl.gjorgdy.pl3xmarkers.core.Pl3xMarkersCore;
import nl.gjorgdy.pl3xmarkers.core.layers.SignsMarkerLayer;
import nl.gjorgdy.pl3xmarkers.core.registries.Layers;
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
	public abstract SignText getText(boolean isFrontText);

	@WrapMethod(method = "updateSignText")
	private void onChangeText(Player player, boolean frontText, List<FilteredText> lines, Operation<Void> original) {
		if (isNotMarkerSign() || level == null) {
			original.call(player, frontText, lines);
			return;
		}

		var textBefore = getText(true).getMessages(false);
		original.call(player, frontText, lines);
		var textAfter = getText(true).getMessages(false);

		if (Arrays.equals(textBefore, textAfter)) {
			// no changes
			return;
		}
		var result = Pl3xMarkersCore.api()
				.getWorld(level.dimension().identifier().toString())
				.getLayer(SignsMarkerLayer.class, Layers.Keys.SIGNS)
				.set(
						worldPosition.getX(),
						worldPosition.getY(),
						worldPosition.getZ(),
						getSignTextLines()
				);
		if (player instanceof ServerPlayer serverPlayer) {
			FeedbackHelper.sendFeedback(result, serverPlayer);
		}
	}

	@Unique
	@Language("HTML")
	private String[] getSignTextLines() {
		return Arrays.stream(getText(true).getMessages(false))
				.map(Component::getString)
				.toArray(String[]::new);
	}

	@Unique
	private boolean isNotMarkerSign() {
		return level == null
				|| !level.getBlockState(worldPosition.below()).is(Blocks.LODESTONE)
				|| !getBlockState().is(BlockTags.STANDING_SIGNS);
	}

}
