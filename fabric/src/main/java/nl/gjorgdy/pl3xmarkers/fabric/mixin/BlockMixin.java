package nl.gjorgdy.pl3xmarkers.fabric.mixin;

import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.entity.BannerBlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import nl.gjorgdy.pl3xmarkers.core.Pl3xMarkersCore;
import nl.gjorgdy.pl3xmarkers.core.layers.BeaconMarkerLayer;
import nl.gjorgdy.pl3xmarkers.core.layers.primitive.AreaMarkerLayer;
import nl.gjorgdy.pl3xmarkers.core.registries.Layers;
import nl.gjorgdy.pl3xmarkers.fabric.helpers.FeedbackHelper;
import org.intellij.lang.annotations.Language;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(Block.class)
public class BlockMixin {

    @Inject(method = "setPlacedBy", at = @At("HEAD"))
    public void setPlacedBy(Level level, BlockPos pos, BlockState state, LivingEntity by, ItemStack itemStack, CallbackInfo ci) {
        // area markers
	    var blockEntity = level.getBlockEntity(pos);
	    if (blockEntity instanceof BannerBlockEntity banner && level.getBlockState(pos.below()).is(Blocks.LODESTONE)) {
            @Language("HTML") var name = banner.getName().tryCollapseToString();
		    if (name == null) {
			    return;
		    }
		    var markerLayer = Pl3xMarkersCore.api()
				    .getWorld(level.dimension().identifier().toString())
				    .getLayer(AreaMarkerLayer.class, Layers.Keys.AREAS);
		    if (markerLayer == null) {
			    return;
		    }
		    var result = markerLayer.addPoint(
				    name, banner.getBaseColor().getTextureDiffuseColor(),
				    pos.getX(), pos.getY(), pos.getZ()
		    );
		    if (by instanceof ServerPlayer player) {
				FeedbackHelper.sendFeedback(result, player);
			}
        }
    }

    @Inject(method = "playerWillDestroy", at = @At("HEAD"))
    public void playerWillDestroy(Level level, BlockPos pos, BlockState state, Player player, CallbackInfoReturnable<BlockState> cir) {
        // beacon markers
        if (state.is(Blocks.BEACON)) {
	        var markerLayer = Pl3xMarkersCore.api()
			        .getWorld(level.dimension().identifier().toString())
			        .getLayer(BeaconMarkerLayer.class, Layers.Keys.BEACONS);
	        if (markerLayer == null) {
		        return;
	        }
	        var result = markerLayer.remove(pos.getX(), pos.getY(), pos.getZ());
	        if (player instanceof ServerPlayer serverPlayer) {
		        FeedbackHelper.sendFeedback(result, serverPlayer);
	        }
	        return;
        }
        // area markers
	    if (level instanceof ServerLevel serverWorld) {
            var blockEntity = serverWorld.getBlockEntity(
                state.is(Blocks.LODESTONE) ? pos.above() : pos
            );
            if (blockEntity instanceof BannerBlockEntity banner) {
                @Language("HTML") var name = banner.getName().tryCollapseToString();
	            if (name == null) {
		            return;
	            }
	            var markerLayer = Pl3xMarkersCore.api()
			            .getWorld(level.dimension().identifier().toString())
			            .getLayer(AreaMarkerLayer.class, Layers.Keys.AREAS);
	            if (markerLayer == null) {
		            return;
	            }
	            var result = markerLayer.removePoint(
			            name, banner.getBaseColor().getTextureDiffuseColor(),
			            pos.getX(), pos.getY(), pos.getZ()
	            );
				if (player instanceof ServerPlayer serverPlayer) {
					FeedbackHelper.sendFeedback(result, serverPlayer);
				}
            }
        }
    }

}
