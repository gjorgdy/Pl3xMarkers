package nl.gjorgdy.pl3xmarkers.fabric.compat.layers;

import net.fabricmc.fabric.api.event.lifecycle.v1.ServerLifecycleEvents;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.world.ServerWorld;
import net.pl3x.map.core.world.World;
import nl.gjorgdy.pl3xmarkers.core.layers.primitive.MarkerLayer;
import nl.gjorgdy.pl3xmarkers.core.markers.AreaMarkerBuilder;
import nl.gjorgdy.pl3xmarkers.core.markers.MarkerBuilder;
import nl.gjorgdy.pl3xmarkers.core.registries.Layers;
import nl.gjorgdy.pl3xmarkers.fabric.Pl3xMarkersFabric;
import nl.gjorgdy.pl3xmarkers.fabric.compat.OpacChunk;
import nl.gjorgdy.pl3xmarkers.fabric.compat.OpacHandler;
import nl.gjorgdy.pl3xmarkers.fabric.compat.OpacListener;
import org.jetbrains.annotations.NotNull;
import xaero.pac.common.server.api.OpenPACServerAPI;
import xaero.pac.common.server.claims.ServerClaimsManager;

public class OPACAreaMarkerLayer extends MarkerLayer {

    public OPACAreaMarkerLayer(@NotNull World world) {
        super(Layers.Keys.OPAC, Layers.Labels.OPAC, world, 99);
    }

    @Override
    public void load() {
        ServerLifecycleEvents.ServerStarted fun = (server) -> {
            var chunks = OpacHandler.load(server, worldIdentifier);
            chunks.forEach(this::addChunk);
        };
		// or directly if server already started
		if (Pl3xMarkersFabric.isOpacLoaded(getServer())) {
			fun.onServerStarted(getServer());
		// run on server start
		} else {
			ServerLifecycleEvents.SERVER_STARTED.register(fun);
		}
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
