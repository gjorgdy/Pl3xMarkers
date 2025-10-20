package nl.gjorgdy.pl3xmarkers.fabric.compat.layers;

import net.fabricmc.fabric.api.event.lifecycle.v1.ServerLifecycleEvents;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.world.ServerWorld;
import net.pl3x.map.core.world.World;
import nl.gjorgdy.pl3xmarkers.core.Layers;
import nl.gjorgdy.pl3xmarkers.core.entities.AreaPointEntity;
import nl.gjorgdy.pl3xmarkers.core.layers.primitive.MarkerLayer;
import nl.gjorgdy.pl3xmarkers.core.markers.AreaMarkerBuilder;
import nl.gjorgdy.pl3xmarkers.core.markers.MarkerBuilder;
import nl.gjorgdy.pl3xmarkers.fabric.Pl3xMarkersFabric;
import nl.gjorgdy.pl3xmarkers.fabric.compat.OpacChunk;
import nl.gjorgdy.pl3xmarkers.fabric.compat.OpacHandler;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

public class OPACAreaMarkerLayer extends MarkerLayer {

    public OPACAreaMarkerLayer(@NotNull World world) {
        super(Layers.Keys.OPAC, Layers.Labels.OPAC, world);
    }

    @Override
    public void load() {
        ServerLifecycleEvents.ServerStarted fun = (server) -> {
            if (!Pl3xMarkersFabric.isOpacLoaded()) return;
            var chunks = OpacHandler.load(server, worldIdentifier);
            chunks.forEach(chunk -> addMarker(createAreaMarker(chunk)));
        };
        // wait till server is ready
        if (getServer().isLoading()) {
            System.out.println("Server not yet started, registering OPAC load on server started event");
            ServerLifecycleEvents.SERVER_STARTED.register(fun);
        }
        else {
            System.out.println("Server already started, running OPAC load directly");
            fun.onServerStarted(getServer());
        }
    }

    protected MarkerBuilder<?> createAreaMarker(OpacChunk chunk) {
        return AreaMarkerBuilder.newAreaMarker(
            chunk.playerName() + ":" + chunk.pos().x + ":" + chunk.pos().z,
            new ArrayList<>(List.of(
                new AreaPointEntity(chunk.pos().x * 16, chunk.pos().z * 16),
                new AreaPointEntity((chunk.pos().x + 1) * 16, chunk.pos().z * 16),
                new AreaPointEntity((chunk.pos().x + 1) * 16, (chunk.pos().z + 1) * 16),
                new AreaPointEntity(chunk.pos().x * 16, (chunk.pos().z + 1) * 16)
            ))
        )
        .fill(chunk.color())
        .stroke(chunk.color())
        .addPopup(chunk.name().isEmpty() ? chunk.playerName() + "'s claim" : chunk.name() + " (" + chunk.playerName() + ")");
    }

    @Override
    public boolean isInWorld(@NotNull World world) {
        return Pl3xMarkersFabric.isOpacLoaded();
    }

	final protected MinecraftServer getServer() {
		if (getWorld().getLevel() instanceof ServerWorld serverWorld) {
			return serverWorld.getServer();
		}
		throw new IllegalStateException("World is not a server world");
	}

}
