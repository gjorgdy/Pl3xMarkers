package nl.gjorgdy.pl3xmarkers.fabric.compat.layers;

import net.minecraft.server.MinecraftServer;
import net.minecraft.server.world.ServerWorld;
import net.pl3x.map.core.world.World;
import nl.gjorgdy.pl3xmarkers.core.Pl3xMarkersCore;
import nl.gjorgdy.pl3xmarkers.core.layers.primitive.MarkerLayer;
import nl.gjorgdy.pl3xmarkers.core.markers.AreaMarkerBuilder;
import nl.gjorgdy.pl3xmarkers.core.markers.MarkerBuilder;
import nl.gjorgdy.pl3xmarkers.core.registries.Layers;
import nl.gjorgdy.pl3xmarkers.fabric.FabricMarkersConfig;
import nl.gjorgdy.pl3xmarkers.fabric.Pl3xMarkersFabric;
import nl.gjorgdy.pl3xmarkers.fabric.compat.OpacChunk;
import nl.gjorgdy.pl3xmarkers.fabric.compat.OpacHandler;
import nl.gjorgdy.pl3xmarkers.fabric.compat.OpacListener;
import org.jetbrains.annotations.NotNull;
import xaero.pac.common.server.api.OpenPACServerAPI;

public class OPACAreaMarkerLayer extends MarkerLayer {

    public OPACAreaMarkerLayer(@NotNull World world) {
        super(Layers.Keys.OPAC, Layers.Labels.OPAC, world, FabricMarkersConfig.OPAC_MARKERS_PRIORITY);
    }

    @Override
    public void load() {
		Pl3xMarkersCore.runParallel(() -> {
			var server = getServer();
			while (!Pl3xMarkersFabric.isOpacLoaded(server)) {
				try {
					Thread.sleep(1000);
				} catch (InterruptedException e) {
					return;
				}
			}
			OpacHandler.load(server, worldIdentifier)
				.forEach(this::addChunk);
		});
		// register listener for claim changes
		OpenPACServerAPI.get(getServer())
				.getServerClaimsManager()
				.getTracker().register(new OpacListener(this));
    }

	public void addChunk(OpacChunk chunk) {
		addMarker(createAreaMarker(chunk));
	}

    private MarkerBuilder<?> createAreaMarker(OpacChunk chunk) {
        return AreaMarkerBuilder.newAreaMarker(chunk.getKey(), chunk.getCorners())
			.fill(chunk.color())
			.stroke(chunk.color())
			.addPopup(chunk.getName());
    }

	public final MinecraftServer getServer() {
		if (getWorld().getLevel() instanceof ServerWorld serverWorld) {
			return serverWorld.getServer();
		}
		throw new IllegalStateException("World is not a server world");
	}

}
