package eu.hexasis.helixmarkers.layers;

import eu.hexasis.helixmarkers.helpers.HtmlHelper;
import eu.hexasis.helixmarkers.markers.IconMarkerBuilder;
import net.pl3x.map.core.markers.marker.Marker;
import net.pl3x.map.core.world.World;
import org.intellij.lang.annotations.Language;
import org.jetbrains.annotations.NotNull;

public class NetherPortalIconMarkerLayer extends IconMarkerLayer {

    public NetherPortalIconMarkerLayer(String icon, String key, String label, @Language("HTML") String tooltip, @NotNull World world) {
        super(icon, key, label, tooltip, world);
    }

    @Override
    protected Marker<?> createIconMarker(int x, int z) {
        // check if the portal is in the overworld or the nether
        String worldKey = getWorld().getKey();
        boolean isOverworld = worldKey.equals("minecraft:overworld");
        // define the destination and button text
        String destinationKey = isOverworld ? "minecraft-the_nether" : "minecraft-overworld";
        int relativeX = isOverworld ? x / 8 : x * 8;
        int relativeZ = isOverworld ? z / 8 : z * 8;
        String buttonText = isOverworld ? "Go to Nether" : "Go to Overworld";
        // build the marker
        return IconMarkerBuilder.newIconMarker(
                        toMarkerKey(x, z),
                        iconId,
                        x, z
                )
                .centerIcon(16, 16)
                .addPopup(HtmlHelper.TravelPopUp(tooltip, destinationKey, relativeX, relativeZ, buttonText))
                .build();
    }

}
