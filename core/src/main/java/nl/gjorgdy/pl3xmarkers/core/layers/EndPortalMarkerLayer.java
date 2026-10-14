package nl.gjorgdy.pl3xmarkers.core.layers;

import net.pl3x.map.core.world.World;
import nl.gjorgdy.pl3xmarkers.core.MarkersConfig;
import nl.gjorgdy.pl3xmarkers.core.helpers.HtmlHelper;
import nl.gjorgdy.pl3xmarkers.core.interfaces.entities.ISimpleMarker;
import nl.gjorgdy.pl3xmarkers.core.layers.primitive.SimpleMarkerLayer;
import nl.gjorgdy.pl3xmarkers.core.objects.InteractionResult;
import nl.gjorgdy.pl3xmarkers.core.registries.Icons;
import nl.gjorgdy.pl3xmarkers.core.registries.Layers;
import org.intellij.lang.annotations.Language;
import org.jetbrains.annotations.NotNull;
import org.jspecify.annotations.Nullable;

public class EndPortalMarkerLayer extends SimpleMarkerLayer {

    public EndPortalMarkerLayer(@NotNull World world) {
        super(Icons.Keys.END_PORTAL, Layers.Keys.END_PORTALS, Layers.Labels.END_PORTALS, Layers.Tooltips.END_PORTALS, world, MarkersConfig.END_PORTAL_MARKERS_PRIORITY);
    }

    @Override
    public void loadMarker(ISimpleMarker markerEntity) {
        if (MarkersConfig.END_PORTAL_MARKERS_ONLY_SHOW_NAMED) {
            if (markerEntity.getName() == null || markerEntity.getName().isEmpty()) {
                return;
            }
        }
        super.loadMarker(markerEntity);
    }

    @Override
    public InteractionResult setName(int x, int y, int z, String newName) {
        if (!MarkersConfig.END_PORTAL_MARKERS_RENAME) {
            return InteractionResult.skip();
        }
        return super.setName(x, y, z, newName);
    }

    @Override
    protected String createPopup(ISimpleMarker object) {
        return HtmlHelper.TravelPopUp(
                createTooltip(object),
                destinationKey(getWorld().getKey()),
                100, 0,
                "Go to The End"
        );
    }

    @Override
    protected @Nullable String createPermanentBottomTooltip(ISimpleMarker object) {
        if (MarkersConfig.END_PORTAL_MARKERS_ALWAYS_SHOW_NAME && object.getName() != null) {
            return createTooltip(object);
        }
        return null;
    }

    @Override
    protected String createTooltip(ISimpleMarker markerEntity) {
        @Language("HTML") var name = markerEntity.getName();
        return name != null ? name : tooltip;
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