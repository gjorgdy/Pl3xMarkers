package nl.gjorgdy.pl3xmarkers.fabric.compat;

import net.minecraft.util.Identifier;
import nl.gjorgdy.pl3xmarkers.fabric.compat.layers.OPACAreaMarkerLayer;
import org.jetbrains.annotations.NotNull;
import xaero.pac.common.claims.player.api.IPlayerChunkClaimAPI;
import xaero.pac.common.claims.tracker.api.IClaimsManagerListenerAPI;

public class OpacListener implements IClaimsManagerListenerAPI {

	private final OPACAreaMarkerLayer markerLayer;

	public OpacListener(OPACAreaMarkerLayer opacAreaMarkerLayer) {
		this.markerLayer = opacAreaMarkerLayer;
	}

	@Override
	public void onWholeRegionChange(@NotNull Identifier world, int x, int z) {}

	@Override
	public void onChunkChange(@NotNull Identifier world, int x, int z, IPlayerChunkClaimAPI p) {
		if (!world.equals(Identifier.of(markerLayer.worldIdentifier))) return;
		if (p == null) {
			// chunk unclaimed
			markerLayer.removeMarker("OpacChunk:" + x + ":" + z);
		} else {
			// chunk claimed
			markerLayer.addChunk(OpacHandler.getChunk(markerLayer.getServer(), p.getPlayerId(), x, z));
		}
	}

	@Override
	public void onDimensionChange(Identifier world) {}

}
