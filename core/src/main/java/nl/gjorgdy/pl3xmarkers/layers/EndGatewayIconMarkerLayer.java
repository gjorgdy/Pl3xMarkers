package nl.gjorgdy.pl3xmarkers.layers;

import nl.gjorgdy.pl3xmarkers.Icons;
import nl.gjorgdy.pl3xmarkers.Layers;
import nl.gjorgdy.pl3xmarkers.layers.primitive.IconMarkerLayer;
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