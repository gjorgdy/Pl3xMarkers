package eu.hexasis.helixmarkers.layers;

import eu.hexasis.helixmarkers.Icons;
import eu.hexasis.helixmarkers.Layers;
import eu.hexasis.helixmarkers.helpers.HtmlHelper;
import eu.hexasis.helixmarkers.layers.primitive.IconMarkerLayer;
import eu.hexasis.helixmarkers.markers.IconMarkerBuilder;
import net.pl3x.map.core.markers.marker.Marker;
import net.pl3x.map.core.world.World;
import org.jetbrains.annotations.NotNull;

public class EndPortalIconMarkerLayer extends IconMarkerLayer {

    public EndPortalIconMarkerLayer(@NotNull World world) {
        super(Icons.Keys.END_PORTAL, Layers.Keys.END_PORTALS, Layers.Labels.END_PORTALS, Layers.Tooltips.END_PORTALS, world);
    }

    @Override
    protected Marker<?> createIconMarker(int x, int z) {
        return IconMarkerBuilder.newIconMarker(
                        toMarkerKey(x, z),
                        iconId,
                        x, z
                )
                .centerIcon(16, 16)
                .addPopup(HtmlHelper.TravelPopUp(tooltip, "minecraft-the_end", 100, 0, "Go to The End"))
                .build();
    }

}
