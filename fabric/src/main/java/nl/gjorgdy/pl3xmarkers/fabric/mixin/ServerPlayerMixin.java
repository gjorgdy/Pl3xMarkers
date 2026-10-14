package nl.gjorgdy.pl3xmarkers.fabric.mixin;

import com.mojang.authlib.GameProfile;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import nl.gjorgdy.pl3xmarkers.core.MarkersConfig;
import nl.gjorgdy.pl3xmarkers.core.Pl3xMarkersCore;
import nl.gjorgdy.pl3xmarkers.core.interfaces.IBoundary;
import nl.gjorgdy.pl3xmarkers.core.layers.primitive.AreaMarkerLayer;
import nl.gjorgdy.pl3xmarkers.core.registries.Layers;
import nl.gjorgdy.pl3xmarkers.fabric.helpers.FeedbackHelper;
import org.jspecify.annotations.NonNull;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;

@Mixin(ServerPlayer.class)
public abstract class ServerPlayerMixin extends Player {

	@Unique
	private final ServerPlayer player = (ServerPlayer) (Object) this;
	@Unique
	private IBoundary boundary = null;
	@Unique
	private int boundaryPresenceLength = 0;

	public ServerPlayerMixin(Level world, GameProfile profile) {
		super(world, profile);
	}

	@Override
	@Shadow
	public abstract @NonNull ServerLevel level();

	@Override
	protected void applyInput() {
		super.applyInput();

		// If no area feedback is enabled, return early
		if (
				(MarkersConfig.FEEDBACK_AREA_ENTER == MarkersConfig.EnterOrLeave.None
						&& MarkersConfig.FEEDBACK_AREA_LEAVE == MarkersConfig.EnterOrLeave.None
						&& MarkersConfig.FEEDBACK_AREA_PRESENCE == MarkersConfig.Presence.None)
						|| !MarkersConfig.AREA_MARKERS_ENABLED
		) {
			return;
		}

		var pos = blockPosition();
		var worldIdentifier = level().dimension().identifier().toString();

		if (boundary != null && boundary.contains(pos.getX(), pos.getZ())) {
			boundaryPresenceLength++;
			// area presence
			if (boundaryPresenceLength > 40 && MarkersConfig.FEEDBACK_AREA_PRESENCE == MarkersConfig.Presence.ActionBar) {
				FeedbackHelper.sendOverlayMessage(
						player,
						boundary.areaMarker().getName(),
						boundary.areaMarker().getColor()
				);
			}
			// boss bar
			if (MarkersConfig.FEEDBACK_AREA_PRESENCE == MarkersConfig.Presence.BossBar) {
				FeedbackHelper.showBossBar(
						player,
						boundary.areaMarker().getName(),
						boundary.areaMarker().getColor()
				);
			}
			return;
		}
		FeedbackHelper.hideBossBar(player);
		boundaryPresenceLength = 0;

		var markerLayer = Pl3xMarkersCore.api()
				.getWorld(worldIdentifier)
				.getLayer(AreaMarkerLayer.class, Layers.Keys.AREAS);
		if (markerLayer == null) {
			return;
		}
		markerLayer
				.getContaining(pos.getX(), pos.getZ())
				.ifPresentOrElse(
				boundary ->
				{
					this.boundary = boundary;
					var text = MarkersConfig.MESSAGE_AREA_ENTER.replace("{name}", boundary.areaMarker().getName());
					switch (MarkersConfig.FEEDBACK_AREA_ENTER) {
						case ActionBar -> FeedbackHelper.sendOverlayMessage(
								player, text, boundary.areaMarker().getColor()
						);
						case Title -> FeedbackHelper.sendTitle(
								player, text, boundary.areaMarker().getColor()
						);
						case Subtitle -> FeedbackHelper.sendSubTitle(
								player, text, boundary.areaMarker().getColor()
						);
					}
				},
				() -> {
					if (boundary == null) {
						return;
					}
					var text = MarkersConfig.MESSAGE_AREA_LEAVE.replace("{name}", boundary.areaMarker().getName());
					switch (MarkersConfig.FEEDBACK_AREA_LEAVE) {
						case ActionBar -> FeedbackHelper.sendOverlayMessage(
								player, text, boundary.areaMarker().getColor()
						);
						case Title -> FeedbackHelper.sendTitle(
								player, text, boundary.areaMarker().getColor()
						);
						case Subtitle -> FeedbackHelper.sendSubTitle(
								player, text, boundary.areaMarker().getColor()
						);
					}
					boundary = null;
				}
		);
	}

}
