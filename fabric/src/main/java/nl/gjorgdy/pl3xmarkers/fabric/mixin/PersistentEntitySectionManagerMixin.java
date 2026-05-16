package nl.gjorgdy.pl3xmarkers.fabric.mixin;

import net.minecraft.world.entity.LightningBolt;
import net.minecraft.world.level.entity.EntityAccess;
import net.minecraft.world.level.entity.PersistentEntitySectionManager;
import nl.gjorgdy.pl3xmarkers.core.Pl3xMarkersCore;
import nl.gjorgdy.pl3xmarkers.core.layers.LightningMarkerLayer;
import nl.gjorgdy.pl3xmarkers.core.registries.Layers;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(PersistentEntitySectionManager.class)
public class PersistentEntitySectionManagerMixin<T extends EntityAccess> {

	@Inject(method = "addEntity(Lnet/minecraft/world/level/entity/EntityAccess;Z)Z", at = @At("HEAD"))
	public void onEntityAdd(T entity, boolean loaded, CallbackInfoReturnable<Boolean> cir) {
		if (entity instanceof LightningBolt lightning) {
			var markerLayer = Pl3xMarkersCore.api()
					.getWorld(lightning.level().dimension().identifier().toString())
					.getLayer(LightningMarkerLayer.class, Layers.Keys.LIGHTNING);
			if (markerLayer == null) {
				return;
			}
			markerLayer.show(lightning.getBlockX(), lightning.getBlockY(), lightning.getBlockZ());
		}
	}
}
