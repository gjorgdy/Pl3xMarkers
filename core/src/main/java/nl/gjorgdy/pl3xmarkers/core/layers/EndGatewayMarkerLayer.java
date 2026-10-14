package nl.gjorgdy.pl3xmarkers.core.layers;

import net.pl3x.map.core.world.World;
import nl.gjorgdy.pl3xmarkers.core.MarkersConfig;
import nl.gjorgdy.pl3xmarkers.core.interfaces.entities.ISimpleMarker;
import nl.gjorgdy.pl3xmarkers.core.layers.primitive.SimpleMarkerLayer;
import nl.gjorgdy.pl3xmarkers.core.objects.InteractionResult;
import nl.gjorgdy.pl3xmarkers.core.registries.Icons;
import nl.gjorgdy.pl3xmarkers.core.registries.Layers;
import org.intellij.lang.annotations.Language;
import org.jetbrains.annotations.NotNull;
import org.jspecify.annotations.Nullable;

public class EndGatewayMarkerLayer extends SimpleMarkerLayer {

    public EndGatewayMarkerLayer(@NotNull World world) {
        super(Icons.Keys.END_GATEWAY, Layers.Keys.END_GATEWAYS, Layers.Labels.END_GATEWAYS, Layers.Tooltips.END_GATEWAYS, world, MarkersConfig.END_GATEWAY_MARKERS_PRIORITY);
    }

    @Override
    public void loadMarker(ISimpleMarker markerEntity) {
        if (MarkersConfig.END_GATEWAY_MARKERS_ONLY_SHOW_NAMED) {
            if (markerEntity.getName() == null || markerEntity.getName().isEmpty()) {
                return;
            }
        }
        super.loadMarker(markerEntity);
    }

    @Override
    public InteractionResult setName(int x, int y, int z, String newName) {
        if (!MarkersConfig.END_GATEWAY_MARKERS_RENAME) {
            return InteractionResult.skip();
        }
        return super.setName(x, y, z, newName);
    }

    @Override
    protected @Nullable String createPermanentBottomTooltip(ISimpleMarker object) {
        if (MarkersConfig.END_GATEWAY_MARKERS_ALWAYS_SHOW_NAME && object.getName() != null) {
            return createTooltip(object);
        }
        return null;
    }

    @Override
    protected String createTooltip(ISimpleMarker markerEntity) {
        @Language("HTML") var name = markerEntity.getName();
        return name != null ? name : tooltip;
    }
}