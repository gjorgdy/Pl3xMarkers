package nl.gjorgdy.pl3xmarkers.fabric.mixin;

import com.mojang.authlib.GameProfile;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.text.Text;
import net.minecraft.world.World;
import nl.gjorgdy.pl3xmarkers.core.Pl3xMarkersCore;
import nl.gjorgdy.pl3xmarkers.core.objects.Boundary;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;

@Mixin(ServerPlayerEntity.class)
public abstract class ServerPlayerEntityMixin extends PlayerEntity {

	@Unique
	private Boundary boundary = null;

	@Shadow
	public abstract ServerWorld getEntityWorld();

	public ServerPlayerEntityMixin(World world, GameProfile profile) {
		super(world, profile);
	}

	@Override
	protected void tickMovementInput() {
		super.tickMovementInput();

		var pos = this.getBlockPos();
		var worldKey = getEntityWorld().getRegistryKey().getValue().toString();

		if (this.boundary != null && this.boundary.contains(pos.getX(), pos.getZ())) {
			return;
		}

		Pl3xMarkersCore.api().getAreaBoundary(worldKey, pos.getX(), pos.getZ()).ifPresentOrElse(boundary ->
				{
					this.boundary = boundary;
					this.sendMessage(
							Text.literal("[+] " + boundary.areaMarker().getName())
									.withColor(boundary.areaMarker().getColor()),
							true
					);
				}, () -> {
					if (this.boundary == null) {
						return;
					}
					this.sendMessage(
							Text.literal("[-] " + this.boundary.areaMarker().getName())
									.withColor(boundary.areaMarker().getColor()),
							true
					);
					this.boundary = null;
				}
		);
	}

}
