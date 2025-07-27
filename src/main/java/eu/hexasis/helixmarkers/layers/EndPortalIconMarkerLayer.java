package eu.hexasis.helixmarkers.layers;

import eu.hexasis.helixmarkers.helpers.HtmlHelper;
import eu.hexasis.helixmarkers.markers.IconMarkerBuilder;
import net.pl3x.map.core.markers.marker.Marker;
import net.pl3x.map.core.world.World;
import org.intellij.lang.annotations.Language;
import org.jetbrains.annotations.NotNull;

public class EndPortalIconMarkerLayer extends IconMarkerLayer {

    public EndPortalIconMarkerLayer(String icon, String key, String label, @Language("HTML") String tooltip, @NotNull World world) {
        super(icon, key, label, tooltip, world);
    }

    @Override
    protected Marker<?> createIconMarker(int x, int z) {
        return IconMarkerBuilder.newIconMarker(
                        toMarkerKey(x, z),
                        iconId,
                        x, z
                )
                .centerIcon(16, 16)
                .addPopup(HtmlHelper.TravelPopUp(label, "minecraft-the_end", 100, 0, "Go to The End"))
                .build();
    }

}
