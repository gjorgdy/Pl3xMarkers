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

public class EndPortalMarkerLayer extends SimpleMarkerLayer {

    public EndPortalMarkerLayer(@NotNull World world) {
        super(Icons.Keys.END_PORTAL, Layers.Keys.END_PORTALS, Layers.Labels.END_PORTALS, Layers.Tooltips.END_PORTALS, world, MarkersConfig.END_PORTAL_MARKERS_PRIORITY);
    }

    @Override
    protected String createPopup(ISimpleMarker markerEntity) {
        return HtmlHelper.TravelPopUp(
                createTooltip(markerEntity),
                destinationKey(getWorld().getKey()),
                100, 0,
                buttonText(getWorld().getKey())
        );
    }

    private String buttonText(String worldKey) {
        return WorldHelpers.isEnd(worldKey) ? "Go to Overworld" : "Go to The End";
    }

    private String destinationKey(String worldKey) {
        return switch (worldKey) {
            case "minecraft:the_end" -> "minecraft-overworld"; // fabric
            case "minecraft:overworld" -> "minecraft-the_end"; // fabric
            case "world" -> "world_the_end"; // paper
            case "world_the_end" -> "world"; // paper
            default -> dynamicDestinationKey(worldKey);
        };
    }

    private String dynamicDestinationKey(String worldKey) {
        return (WorldHelpers.isEnd(worldKey) ? worldKey.replace("_the_end", "") : worldKey + "_the_end").replace(":", "-");
    }

}
