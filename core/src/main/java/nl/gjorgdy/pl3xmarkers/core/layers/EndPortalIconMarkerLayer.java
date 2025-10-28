package nl.gjorgdy.pl3xmarkers.core.layers;

import nl.gjorgdy.pl3xmarkers.core.MarkersConfig;
import nl.gjorgdy.pl3xmarkers.core.registries.Icons;
import nl.gjorgdy.pl3xmarkers.core.registries.Layers;
import nl.gjorgdy.pl3xmarkers.core.helpers.HtmlHelper;
import nl.gjorgdy.pl3xmarkers.core.layers.primitive.IconMarkerLayer;
import nl.gjorgdy.pl3xmarkers.core.markers.IconMarkerBuilder;
import net.pl3x.map.core.markers.marker.Marker;
import net.pl3x.map.core.world.World;
import org.jetbrains.annotations.NotNull;

public class EndPortalIconMarkerLayer extends IconMarkerLayer {

    public EndPortalIconMarkerLayer(@NotNull World world) {
        super(Icons.Keys.END_PORTAL, Layers.Keys.END_PORTALS, Layers.Labels.END_PORTALS, Layers.Tooltips.END_PORTALS, world, MarkersConfig.END_PORTAL_MARKERS_PRIORITY);
    }

    @Override
    protected Marker<?> createIconMarker(int x, int z) {
        return IconMarkerBuilder.newIconMarker(
                        toMarkerKey(x, z),
                        iconId,
                        x, z
                )
                .centerIcon(16, 16)
                .addPopup(HtmlHelper.TravelPopUp(tooltip, destinationKey(getWorld().getKey()), 100, 0, "Go to The End"))
                .build();
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