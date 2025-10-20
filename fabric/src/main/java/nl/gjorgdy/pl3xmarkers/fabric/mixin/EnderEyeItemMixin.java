package nl.gjorgdy.pl3xmarkers.fabric.mixin;

import com.llamalad7.mixinextras.sugar.Local;
import nl.gjorgdy.pl3xmarkers.core.Pl3xMarkersCore;
import net.minecraft.item.EnderEyeItem;
import net.minecraft.item.ItemUsageContext;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.BlockPos;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(EnderEyeItem.class)
public class EnderEyeItemMixin {

    @Inject(method = "useOnBlock", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/World;syncGlobalEvent(ILnet/minecraft/util/math/BlockPos;I)V"))
    public void onUseOnBlock(ItemUsageContext context, CallbackInfoReturnable<ActionResult> cir, @Local(ordinal = 1) BlockPos blockPos2) {
        Identifier identifier = context.getWorld().getRegistryKey().getValue();
        Pl3xMarkersCore.api().addEndPortalIconMarker(identifier.toString(), blockPos2.getX() + 1, blockPos2.getZ() + 1);
    }

}
