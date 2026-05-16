package nl.gjorgdy.pl3xmarkers.core.layers;

import net.pl3x.map.core.world.World;
import nl.gjorgdy.pl3xmarkers.core.MarkersConfig;
import nl.gjorgdy.pl3xmarkers.core.helpers.HtmlHelper;
import nl.gjorgdy.pl3xmarkers.core.helpers.WorldHelpers;
import nl.gjorgdy.pl3xmarkers.core.interfaces.entities.ISimpleMarker;
import nl.gjorgdy.pl3xmarkers.core.layers.primitive.SimpleMarkerLayer;
import nl.gjorgdy.pl3xmarkers.core.registries.Icons;
import nl.gjorgdy.pl3xmarkers.core.registries.Layers;
import org.jetbrains.annotations.NotNull;

public class NetherPortalMarkerLayer extends SimpleMarkerLayer {

    public NetherPortalMarkerLayer(@NotNull World world) {
        super(Icons.Keys.NETHER_PORTAL, Layers.Keys.NETHER_PORTALS, Layers.Labels.NETHER_PORTALS, Layers.Tooltips.NETHER_PORTALS, world, MarkersConfig.NETHER_PORTAL_MARKERS_PRIORITY);
    }

    @Override
    protected String createPopup(ISimpleMarker markerEntity) {
        var pos = markerEntity.getPosition();
        String worldKey = getWorld().getKey();
        boolean isOverworld = WorldHelpers.isOverworld(worldKey);
        // Define the destination
        int relativeX = isOverworld ? pos.x() / 8 : pos.x() * 8;
        int relativeZ = isOverworld ? pos.z() / 8 : pos.z() * 8;
        // Build the pop-up
        return HtmlHelper.TravelPopUp(
                createTooltip(markerEntity),
                destinationKey(worldKey),
                relativeX, relativeZ,
                buttonText(worldKey)
        );
    }

    @Override
    protected String createPermanentTooltip(ISimpleMarker markerEntity) {
        if (markerEntity.getName() != null) {
            return createTooltip(markerEntity);
        }
        return null;
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
