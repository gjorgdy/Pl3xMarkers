package nl.gjorgdy.pl3xmarkers.layers;

import net.pl3x.map.core.world.World;
import nl.gjorgdy.pl3xmarkers.Layers;
import nl.gjorgdy.pl3xmarkers.Pl3xMarkers;
import nl.gjorgdy.pl3xmarkers.compat.openpartiesandclaims.OpacChunk;
import nl.gjorgdy.pl3xmarkers.compat.openpartiesandclaims.OpenPartiesAndClaims;
import nl.gjorgdy.pl3xmarkers.entities.AreaPointEntity;
import nl.gjorgdy.pl3xmarkers.layers.primitive.MarkerLayer;
import nl.gjorgdy.pl3xmarkers.markers.AreaMarkerBuilder;
import nl.gjorgdy.pl3xmarkers.markers.MarkerBuilder;
import org.intellij.lang.annotations.Language;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

public class OPACAreaMarkerLayer extends MarkerLayer {

    public OPACAreaMarkerLayer(@NotNull World world) {
        super(Layers.Keys.OPAC, Layers.Labels.OPAC, world);
    }

    @Override
    public void load() {
        if (!Pl3xMarkers.isOpacLoaded()) return;
        var chunks = new OpenPartiesAndClaims().load(getServer(), worldIdentifier);
        chunks.forEach(chunk -> addMarker(createAreaMarker(chunk)));
    }

    protected MarkerBuilder<?> createAreaMarker(OpacChunk chunk) {
        var player = getServer().getPlayerManager().getPlayer(chunk.playerId());
        @Language("HTML")
        String playerName = player == null ? "Unknown" : player.getName().getLiteralString();
        return AreaMarkerBuilder.newAreaMarker(
            chunk.playerId().toString() + ":" + chunk.pos().x + ":" + chunk.pos().z,
            new ArrayList<>(List.of(
                new AreaPointEntity(chunk.pos().x * 16, chunk.pos().z * 16),
                new AreaPointEntity((chunk.pos().x + 1) * 16, chunk.pos().z * 16),
                new AreaPointEntity((chunk.pos().x + 1) * 16, (chunk.pos().z + 1) * 16),
                new AreaPointEntity(chunk.pos().x * 16, (chunk.pos().z + 1) * 16)
            )),
            false
        )
        .fill(-5231066)
        .stroke(-5231066)
        .addPopup(playerName);
    }

    @Override
    public boolean isInWorld(@NotNull World world) {
        return Pl3xMarkers.isOpacLoaded();
    }

}
