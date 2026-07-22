package nl.gjorgdy.pl3xmarkers.core.layers;

import net.pl3x.map.core.world.World;
import nl.gjorgdy.pl3xmarkers.core.MarkersConfig;
import nl.gjorgdy.pl3xmarkers.core.helpers.HtmlHelper;
import nl.gjorgdy.pl3xmarkers.core.helpers.WorldHelpers;
import nl.gjorgdy.pl3xmarkers.core.interfaces.entities.ISimpleMarker;
import nl.gjorgdy.pl3xmarkers.core.layers.primitive.SimpleMarkerLayer;
import nl.gjorgdy.pl3xmarkers.core.objects.InteractionResult;
import nl.gjorgdy.pl3xmarkers.core.registries.Icons;
import nl.gjorgdy.pl3xmarkers.core.registries.Layers;
import org.intellij.lang.annotations.Language;
import org.jetbrains.annotations.NotNull;
import org.jspecify.annotations.Nullable;

public class NetherPortalMarkerLayer extends SimpleMarkerLayer {

    public NetherPortalMarkerLayer(@NotNull World world) {
        super(Icons.Keys.NETHER_PORTAL, Layers.Keys.NETHER_PORTALS, Layers.Labels.NETHER_PORTALS, Layers.Tooltips.NETHER_PORTALS, world, MarkersConfig.NETHER_PORTAL_MARKERS_PRIORITY);
    }

    @Override
    public InteractionResult setName(int x, int y, int z, String newName) {
        if (!MarkersConfig.NETHER_PORTAL_MARKERS_RENAME) {
            return InteractionResult.skip();
        }
        return super.setName(x, y, z, newName);
    }

    @Override
    protected String createPopup(ISimpleMarker object) {
        var pos = object.getPosition();
        String worldKey = getWorld().getKey();
        boolean isOverworld = WorldHelpers.isOverworld(worldKey);
        // Define the destination
        int relativeX = isOverworld ? pos.x() / 8 : pos.x() * 8;
        int relativeZ = isOverworld ? pos.z() / 8 : pos.z() * 8;
        // Build the pop-up
        return HtmlHelper.TravelPopUp(
                createTooltip(object),
                destinationKey(worldKey),
                relativeX, relativeZ,
                buttonText(worldKey)
        );
    }

    @Override
    protected @Nullable String createPermanentBottomTooltip(ISimpleMarker object) {
        if (MarkersConfig.NETHER_PORTAL_MARKERS_ALWAYS_SHOW_NAME && object.getName() != null) {
            return createTooltip(object);
        }
        return null;
    }

    @Override
    protected String createTooltip(ISimpleMarker markerEntity) {
        @Language("HTML") var name = markerEntity.getName();
        return name != null ? name : tooltip;
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
