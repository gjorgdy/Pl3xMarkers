package nl.gjorgdy.pl3xmarkers.fabric.mixin;

import com.mojang.authlib.GameProfile;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import nl.gjorgdy.pl3xmarkers.core.MarkersConfig;
import nl.gjorgdy.pl3xmarkers.core.Pl3xMarkersCore;
import nl.gjorgdy.pl3xmarkers.core.objects.Boundary;
import org.jspecify.annotations.NonNull;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;

@Mixin(ServerPlayer.class)
public abstract class ServerPlayerMixin extends Player {

	@Unique
	private Boundary boundary = null;

	public ServerPlayerMixin(Level world, GameProfile profile) {
		super(world, profile);
	}

	@Override
	@Shadow
	public abstract @NonNull ServerLevel level();

	@Override
	protected void applyInput() {
		super.applyInput();

		if (!MarkersConfig.FEEDBACK_AREA_ENTER_ENABLED) {
			return;
		}

		var pos = blockPosition();
		var worldKey = level().dimension().identifier().toString();

		if (boundary != null && boundary.contains(pos.getX(), pos.getZ())) {
			return;
		}

		Pl3xMarkersCore.api().getAreaBoundary(worldKey, pos.getX(), pos.getZ()).ifPresentOrElse(
				boundary ->
				{
					this.boundary = boundary;
					sendOverlayMessage(Component.literal("[+] " + boundary.areaMarker().getName())
							                   .withColor(boundary.areaMarker().getColor())
					);
				},
				() -> {
					if (boundary == null) {
						return;
					}
					sendOverlayMessage(Component.literal("[-] " + boundary.areaMarker().getName())
							                   .withColor(boundary.areaMarker().getColor())
					);
					boundary = null;
				}
		);
	}

}
