package nl.gjorgdy.pl3xmarkers.fabric.mixin;

import net.minecraft.world.entity.LightningBolt;
import net.minecraft.world.level.entity.EntityAccess;
import net.minecraft.world.level.entity.PersistentEntitySectionManager;
import nl.gjorgdy.pl3xmarkers.core.Pl3xMarkersCore;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(PersistentEntitySectionManager.class)
public class PersistentEntitySectionManagerMixin<T extends EntityAccess> {

	@Inject(method = "addEntity(Lnet/minecraft/world/level/entity/EntityAccess;Z)Z", at = @At("HEAD"))
	public void onEntityAdd(T entity, boolean loaded, CallbackInfoReturnable<Boolean> cir) {
		if (entity instanceof LightningBolt lightningEntity) {
			Pl3xMarkersCore.api().showLightningIconMarker(
					lightningEntity.level().dimension().identifier().toString(),
					lightningEntity.getBlockX(),
					lightningEntity.getBlockZ()
			);
		}
	}
}
