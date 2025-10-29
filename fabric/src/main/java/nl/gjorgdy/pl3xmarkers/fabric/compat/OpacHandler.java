package nl.gjorgdy.pl3xmarkers.fabric.compat;

import net.minecraft.server.MinecraftServer;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.ChunkPos;
import xaero.pac.common.server.api.OpenPACServerAPI;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.UUID;

public class OpacHandler {

    public static Collection<OpacChunk> load(MinecraftServer server, String worldIdentifier) {
        var chunks = new HashSet<OpacChunk>();
        OpenPACServerAPI.get(server)
                .getServerClaimsManager()
                .getPlayerInfoStream()
                .forEach(p -> {
//                    Pl3xMarkersFabric.LOGGER.info("Reading claim data for {} in {}", p.getPlayerUsername(), worldIdentifier);
                    var dimensionManager = p.getDimension(Identifier.of(worldIdentifier));
                    if (dimensionManager == null) return;
                    dimensionManager.getStream().forEach(claim ->
                        claim.getStream().forEach(chunk -> chunks.add(
                            new OpacChunk(chunk, p.getPlayerUsername(), p.getClaimsName(), p.getClaimsColor())
                        ))
                    );
                });
        return chunks;
    }

	public static Collection<OpacChunk> getClaimedChunks(MinecraftServer server, Identifier world, UUID uuid) {
		var playerInfo = OpenPACServerAPI.get(server)
							 .getServerClaimsManager()
							 .getPlayerInfo(uuid);
		var pdc = playerInfo.getDimension(world);
		if (pdc == null) return new ArrayList<>();
		return pdc.getStream().flatMap(claim ->
				claim.getStream().map(chunk ->
				  new OpacChunk(chunk, playerInfo.getPlayerUsername(), playerInfo.getClaimsName(), playerInfo.getClaimsColor()
	    ))).toList();
	}

	public static OpacChunk getChunk(MinecraftServer server, UUID uuid, int x, int z) {
		var playerInfo = OpenPACServerAPI.get(server)
								 .getServerClaimsManager()
								 .getPlayerInfo(uuid);
		return new OpacChunk(new ChunkPos(x, z), playerInfo.getPlayerUsername(), playerInfo.getClaimsName(), playerInfo.getClaimsColor());
	}

}
