package nl.gjorgdy.pl3xmarkers.fabric.mixin;

import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Explosion;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import nl.gjorgdy.pl3xmarkers.fabric.helpers.FeedbackHelper;
import nl.gjorgdy.pl3xmarkers.fabric.listeners.BlockListener;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(Block.class)
public class BlockMixin {

    @Inject(method = "setPlacedBy", at = @At("HEAD"))
    public void setPlacedBy(Level level, BlockPos pos, BlockState state, LivingEntity by, ItemStack itemStack, CallbackInfo ci) {
	    if (!(level instanceof ServerLevel sl)) {
		    return;
	    }
	    var result = BlockListener.onPlace(sl, pos);
	    if (result != null && by instanceof ServerPlayer player) {
		    FeedbackHelper.sendFeedback(result, player);
	    }
    }

	@Inject(method = "wasExploded", at = @At("HEAD"))
	public void destroy(ServerLevel level, BlockPos pos, Explosion explosion, CallbackInfo ci) {
		if (!(level instanceof ServerLevel sl)) {
			return;
		}
		var result = BlockListener.onDestroy(sl, pos, level.getBlockState(pos));
		if (result != null) {
			FeedbackHelper.sendFeedback(result, sl, pos);
		}
	}

	@Inject(method = "playerWillDestroy", at = @At("HEAD"))
    public void playerWillDestroy(Level level, BlockPos pos, BlockState state, Player player, CallbackInfoReturnable<BlockState> cir) {
		var result = BlockListener.onDestroy(level, pos, state);
		if (result != null && player instanceof ServerPlayer serverPlayer) {
			FeedbackHelper.sendFeedback(result, serverPlayer);
		}
    }

}
