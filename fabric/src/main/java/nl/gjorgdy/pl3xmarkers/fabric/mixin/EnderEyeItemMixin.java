package nl.gjorgdy.pl3xmarkers.fabric.mixin;

import com.llamalad7.mixinextras.sugar.Local;
import net.minecraft.core.BlockPos;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.item.EnderEyeItem;
import net.minecraft.world.item.context.UseOnContext;
import nl.gjorgdy.pl3xmarkers.core.Pl3xMarkersCore;
import nl.gjorgdy.pl3xmarkers.core.layers.EndPortalMarkerLayer;
import nl.gjorgdy.pl3xmarkers.core.registries.Layers;
import nl.gjorgdy.pl3xmarkers.fabric.helpers.FeedbackHelper;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(EnderEyeItem.class)
public class EnderEyeItemMixin {

    @Inject(method = "useOn", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/level/Level;globalLevelEvent(ILnet/minecraft/core/BlockPos;I)V"))
    public void onUseOnBlock(UseOnContext context, CallbackInfoReturnable<InteractionResult> cir, @Local(name = "blockPos") BlockPos blockPos) {
        var markerLayer = Pl3xMarkersCore.api()
                .getWorld(context.getLevel().dimension().identifier().toString())
                .getLayer(EndPortalMarkerLayer.class, Layers.Keys.END_PORTALS);
        if (markerLayer == null) {
            return;
        }
        var result = markerLayer.add(blockPos.getX() + 1, blockPos.getY(), blockPos.getZ() + 1);
        FeedbackHelper.sendFeedback(result, context.getLevel(), blockPos);
    }

}
