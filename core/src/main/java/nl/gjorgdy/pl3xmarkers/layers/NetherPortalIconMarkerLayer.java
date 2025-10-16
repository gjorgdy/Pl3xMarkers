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
        boolean isOverworld = isOverworld(worldKey);
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
        return isOverworld(worldKey) ? "Go to Nether" : "Go to Overworld";
    }

    private String destinationKey(String worldKey) {
        return switch (worldKey) {
            case "minecraft:the_nether" -> "minecraft-overworld"; // fabric
            case "minecraft:overworld" -> "minecraft-the_nether"; // fabric
            case "world" -> "world_nether"; // paper
            case "world_nether" -> "world"; // paper
            default -> throw new IllegalStateException("Unexpected value: " + worldKey);
        };
    }

    private boolean isOverworld(String worldKey) {
        return worldKey.equals("minecraft:overworld") || worldKey.equals("world");
    }

}
