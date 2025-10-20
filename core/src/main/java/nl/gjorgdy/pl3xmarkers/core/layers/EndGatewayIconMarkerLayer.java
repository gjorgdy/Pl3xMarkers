package nl.gjorgdy.pl3xmarkers.core.layers;

import nl.gjorgdy.pl3xmarkers.core.Icons;
import nl.gjorgdy.pl3xmarkers.core.Layers;
import nl.gjorgdy.pl3xmarkers.core.layers.primitive.IconMarkerLayer;
import net.pl3x.map.core.world.World;
import org.jetbrains.annotations.NotNull;

public class EndGatewayIconMarkerLayer extends IconMarkerLayer {

    public EndGatewayIconMarkerLayer(@NotNull World world) {
        super(Icons.Keys.END_GATEWAY, Layers.Keys.END_GATEWAYS, Layers.Labels.END_GATEWAYS, Layers.Tooltips.END_GATEWAYS, world);
    }

    @Override
    public boolean isInWorld(@NotNull World world) {
        return isEnd(world);
    }

}