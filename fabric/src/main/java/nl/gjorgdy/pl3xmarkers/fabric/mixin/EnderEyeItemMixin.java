package nl.gjorgdy.pl3xmarkers.fabric.mixin;

import com.llamalad7.mixinextras.sugar.Local;
import net.minecraft.core.BlockPos;
import net.minecraft.resources.Identifier;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.item.EnderEyeItem;
import net.minecraft.world.item.context.UseOnContext;
import nl.gjorgdy.pl3xmarkers.core.Pl3xMarkersCore;
import nl.gjorgdy.pl3xmarkers.fabric.helpers.FeedbackHelper;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(EnderEyeItem.class)
public class EnderEyeItemMixin {

    @Inject(method = "useOn", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/level/Level;globalLevelEvent(ILnet/minecraft/core/BlockPos;I)V"))
    public void onUseOnBlock(UseOnContext context, CallbackInfoReturnable<InteractionResult> cir, @Local(ordinal = 1) BlockPos blockPos2) {
        Identifier identifier = context.getLevel().dimension().identifier();
        var result = Pl3xMarkersCore.api().addEndPortalIconMarker(identifier.toString(), blockPos2.getX() + 1, blockPos2.getZ() + 1);
		FeedbackHelper.sendFeedback(result, context.getLevel(), blockPos2);
    }

}
