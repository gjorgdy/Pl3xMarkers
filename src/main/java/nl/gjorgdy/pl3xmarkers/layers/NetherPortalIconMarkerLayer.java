package nl.gjorgdy.pl3xmarkers.layers;

import nl.gjorgdy.pl3xmarkers.Icons;
import nl.gjorgdy.pl3xmarkers.Layers;
import nl.gjorgdy.pl3xmarkers.helpers.HtmlHelper;
import nl.gjorgdy.pl3xmarkers.layers.primitive.IconMarkerLayer;
import nl.gjorgdy.pl3xmarkers.markers.IconMarkerBuilder;
import net.pl3x.map.core.markers.marker.Marker;
import net.pl3x.map.core.world.World;
import org.jetbrains.annotations.NotNull;

public class NetherPortalIconMarkerLayer extends IconMarkerLayer {

    public NetherPortalIconMarkerLayer(@NotNull World world) {
        super(Icons.Keys.NETHER_PORTAL, Layers.Keys.NETHER_PORTALS, Layers.Labels.NETHER_PORTALS, Layers.Tooltips.NETHER_PORTALS, world);
    }

    @Override
    public boolean isInWorld(@NotNull World world) {
        return isOverworld(world) || isNether(world);
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
