package nl.gjorgdy.pl3xmarkers.fabric.compat.layers;

import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerLevel;
import net.pl3x.map.core.world.World;
import nl.gjorgdy.pl3xmarkers.core.Pl3xMarkersCore;
import nl.gjorgdy.pl3xmarkers.core.layers.primitive.MarkerLayer;
import nl.gjorgdy.pl3xmarkers.core.markers.AreaMarkerBuilder;
import nl.gjorgdy.pl3xmarkers.core.registries.Layers;
import nl.gjorgdy.pl3xmarkers.fabric.FabricMarkersConfig;
import nl.gjorgdy.pl3xmarkers.fabric.compat.OpacChunk;
import nl.gjorgdy.pl3xmarkers.fabric.compat.OpacClaim;
import nl.gjorgdy.pl3xmarkers.fabric.compat.OpacHandler;
import org.intellij.lang.annotations.Language;
import org.jetbrains.annotations.NotNull;

import java.util.HashMap;
import java.util.concurrent.atomic.AtomicInteger;

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
			claims.forEach((_, claim) -> renderClaim(claim));
		});
		// register listener for claim changes
		OpacHandler.registerListener(getServer(), this);
    }

	private OpacClaim getOrCreateClaim(String playerName, @Language("HTML") String name, int color) {
		return claims.computeIfAbsent(playerName, _ -> new OpacClaim(name, color));
	}

	public void addChunk(OpacChunk chunk) {
		addChunk(chunk, false);
	}

	public void addChunk(OpacChunk chunk, boolean render) {
		var claim = getOrCreateClaim(chunk.playerName(), chunk.getName(), chunk.color());
		claim.addChunk(chunk);
		if (render) {
			renderClaim(claim);
		}
	}

	public void removeChunk(int x, int z, boolean render) {
		for (var e : claims.entrySet()) {
			var removed = e.getValue().removeChunk(x, z);
			if (removed && render) {
				renderClaim(e.getValue());
				return;
			}
		}
	}

	private void renderClaim(OpacClaim claim) {
		// remove old areas if exist
		getMarkers().removeIf(m -> m.getKey().startsWith(claim.key + ":"));
		var counter = new AtomicInteger(0);
		claim.getPolygons().forEach(polygon -> {
			var markerBuilder = AreaMarkerBuilder.newAreaMarker(claim.key + ":" + counter.addAndGet(1), polygon)
					.fill(claim.color)
					.stroke(claim.color);
			if (FabricMarkersConfig.OPAC_MARKERS_ALWAYS_SHOW_NAME) {
				markerBuilder.addPermanentTooltip(claim.name);
			} else {
				markerBuilder.addPopup(claim.name);
			}
			addMarker(markerBuilder.build());
		});
	}

	public final MinecraftServer getServer() {
		if (getWorld().getLevel() instanceof ServerLevel serverWorld) {
			return serverWorld.getServer();
		}
		throw new IllegalStateException("World is not a server world");
	}

}
