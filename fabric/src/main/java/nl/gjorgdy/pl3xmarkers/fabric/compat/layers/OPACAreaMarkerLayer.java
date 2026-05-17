package nl.gjorgdy.pl3xmarkers.fabric.compat.layers;

import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerLevel;
import net.pl3x.map.core.world.World;
import nl.gjorgdy.pl3xmarkers.core.Pl3xMarkersCore;
import nl.gjorgdy.pl3xmarkers.core.layers.primitive.MarkerLayer;
import nl.gjorgdy.pl3xmarkers.core.markers.AreaMarkerBuilder;
import nl.gjorgdy.pl3xmarkers.core.markers.MarkerBuilder;
import nl.gjorgdy.pl3xmarkers.core.registries.Layers;
import nl.gjorgdy.pl3xmarkers.fabric.FabricMarkersConfig;
import nl.gjorgdy.pl3xmarkers.fabric.compat.OpacChunk;
import nl.gjorgdy.pl3xmarkers.fabric.compat.OpacClaim;
import nl.gjorgdy.pl3xmarkers.fabric.compat.OpacHandler;
import org.jetbrains.annotations.NotNull;

import java.util.HashMap;

public class OPACAreaMarkerLayer extends MarkerLayer {

	private HashMap<String, OpacClaim> claims;

	public OPACAreaMarkerLayer(@NotNull World world) {
        super(Layers.Keys.OPAC, Layers.Labels.OPAC, world, FabricMarkersConfig.OPAC_MARKERS_PRIORITY);
    }

    @Override
    public void load() {
	    claims = new HashMap<>();
		Pl3xMarkersCore.runParallel(() -> {
			var server = getServer();
			while (!OpacHandler.isOpacLoaded(server)) {
				try {
					//noinspection BusyWait
					Thread.sleep(1000);
				} catch (InterruptedException e) {
					return;
				}
			}
			OpacHandler.load(server, worldIdentifier)
				.forEach(this::addChunk);
			claims.forEach((_, claim) -> claim.chunks.forEach(chunk -> addMarker(createAreaMarker(chunk))));
		});
		// register listener for claim changes
		OpacHandler.registerListener(getServer(), this);
    }

	private OpacClaim getOrCreateClaim(String name, int color) {
		String key = OpacClaim.createKey(name, color);
		return claims.computeIfAbsent(key, _ -> new OpacClaim(name, color));
	}

	public void addChunk(OpacChunk chunk) {
		var claim = getOrCreateClaim(chunk.name(), chunk.color());
		claim.addChunk(chunk);
	}

    private MarkerBuilder<?> createAreaMarker(OpacChunk chunk) {
        return AreaMarkerBuilder.newAreaMarker(chunk.getKey(), chunk.getCorners())
			.fill(chunk.color())
			.stroke(chunk.color())
			.addPopup(chunk.getName());
    }

	public final MinecraftServer getServer() {
		if (getWorld().getLevel() instanceof ServerLevel serverWorld) {
			return serverWorld.getServer();
		}
		throw new IllegalStateException("World is not a server world");
	}

}
