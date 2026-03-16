package nl.gjorgdy.pl3xmarkers.fabric.mixin;

import net.minecraft.entity.LightningEntity;
import net.minecraft.server.world.ServerEntityManager;
import net.minecraft.world.entity.EntityLike;
import nl.gjorgdy.pl3xmarkers.core.Pl3xMarkersCore;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(ServerEntityManager.class)
public class ServerEntityManagerMixin<T extends EntityLike> {

	@Inject(method = "addEntity(Lnet/minecraft/world/entity/EntityLike;Z)Z", at = @At("HEAD"))
	public void onEntityAdd(T entity, boolean existing, CallbackInfoReturnable<Boolean> cir) {
		if (entity instanceof LightningEntity lightningEntity) {
			Pl3xMarkersCore.api().showLightningIconMarker(
					lightningEntity.getEntityWorld().getRegistryKey().getValue().toString(),
					lightningEntity.getBlockX(),
					lightningEntity.getBlockZ()
			);
		}
	}
}
