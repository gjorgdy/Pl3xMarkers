package nl.gjorgdy.pl3xmarkers.compat.openpartiesandclaims;

import net.minecraft.server.MinecraftServer;
import net.minecraft.util.Identifier;
import nl.gjorgdy.pl3xmarkers.Pl3xMarkers;
import xaero.pac.common.server.api.OpenPACServerAPI;

import java.util.Collection;
import java.util.HashSet;
import java.util.UUID;

public class OpenPartiesAndClaims {

    public Collection<OpacChunk> load(MinecraftServer server, Identifier world) {
        var chunks = new HashSet<OpacChunk>();
        OpenPACServerAPI.get(server)
                .getServerClaimsManager()
                .getPlayerInfoStream()
                .forEach(p -> {
//                    if (isServerClaim(p.getPlayerId())) return;
                    Pl3xMarkers.LOGGER.info("Reading claim data for {} in {}", p.getPlayerUsername(), world);
                    var dimensionManager = p.getDimension(world);
                    if (dimensionManager == null) {
                        Pl3xMarkers.LOGGER.info("No chunks found");
                        return;
                    }
                    dimensionManager.getStream().forEach(claim ->
                        claim.getStream().forEach(chunk -> chunks.add(
                            new OpacChunk(chunk, p.getPlayerUsername(), p.getClaimsName(), p.getClaimsColor())
                        ))
                    );
                });
        return chunks;
    }

    private boolean isServerClaim(UUID playerId) {
        return playerId.equals(UUID.fromString("00000000-0000-0000-0000-000000000000"))
                || playerId.equals(UUID.fromString("00000000-0000-0000-0000-000000000001"));
    }

}
