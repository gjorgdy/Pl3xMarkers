package nl.gjorgdy.pl3xmarkers.core.layers;

import net.pl3x.map.core.world.World;
import nl.gjorgdy.pl3xmarkers.core.MarkersConfig;
import nl.gjorgdy.pl3xmarkers.core.helpers.HtmlHelper;
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
                "Go to The End"
        );
    }

    private String destinationKey(String worldKey) {
        return switch (worldKey) {
            case "minecraft:overworld" -> "minecraft-the_end"; // fabric
            case "world" -> "world_the_end"; // paper
            default -> dynamicDestinationKey(worldKey);
        };
    }

    private String dynamicDestinationKey(String worldKey) {
        return (worldKey + "_the_end").replace(":", "-");
    }

}
