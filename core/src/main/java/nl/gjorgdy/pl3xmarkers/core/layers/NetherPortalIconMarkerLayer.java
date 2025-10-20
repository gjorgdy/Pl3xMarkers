package nl.gjorgdy.pl3xmarkers.core.layers;

import nl.gjorgdy.pl3xmarkers.core.Icons;
import nl.gjorgdy.pl3xmarkers.core.Layers;
import nl.gjorgdy.pl3xmarkers.core.helpers.HtmlHelper;
import nl.gjorgdy.pl3xmarkers.core.helpers.WorldHelpers;
import nl.gjorgdy.pl3xmarkers.core.layers.primitive.IconMarkerLayer;
import nl.gjorgdy.pl3xmarkers.core.markers.IconMarkerBuilder;
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
        boolean isOverworld = WorldHelpers.isOverworld(worldKey);
        // define the destination and button text
        int relativeX = isOverworld ? x / 8 : x * 8;
        int relativeZ = isOverworld ? z / 8 : z * 8;
        // build the marker
        return IconMarkerBuilder.newIconMarker(
                        toMarkerKey(x, z),
                        iconId,
                        x, z
                )
                .centerIcon(16, 16)
                .addPopup(HtmlHelper.TravelPopUp(tooltip, destinationKey(worldKey), relativeX, relativeZ, buttonText(worldKey)))
                .build();
    }

    private String buttonText(String worldKey) {
        return WorldHelpers.isOverworld(worldKey) ? "Go to Nether" : "Go to Overworld";
    }

    private String destinationKey(String worldKey) {
        return switch (worldKey) {
            case "minecraft:the_nether" -> "minecraft-overworld"; // fabric
            case "minecraft:overworld" -> "minecraft-the_nether"; // fabric
            case "world" -> "world_nether"; // paper
            case "world_nether" -> "world"; // paper
            default -> dynamicDestinationKey(worldKey);
        };
    }

    private String dynamicDestinationKey(String worldKey) {
        String destination;
        if (WorldHelpers.isOverworld(worldKey)) {
            destination = worldKey + "_nether";
        } else {
            destination = worldKey.replace("_nether", "");
        }
        return destination.replace(":", "-");
    }

}
