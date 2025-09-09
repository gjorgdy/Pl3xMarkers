package nl.gjorgdy.pl3xmarkers.layers.primitive;

import net.minecraft.util.math.BlockPos;
import net.pl3x.map.core.world.World;
import nl.gjorgdy.pl3xmarkers.Pl3xMarkers;
import nl.gjorgdy.pl3xmarkers.entities.IconMarkerEntity;
import nl.gjorgdy.pl3xmarkers.objects.MarkerIdentifier;
import org.intellij.lang.annotations.Language;
import org.jetbrains.annotations.NotNull;

public class FancyIconMarkerLayer extends IconMarkerLayer {

    public FancyIconMarkerLayer(String icon, String key, String label, @Language("HTML") String tooltip, @NotNull World world) {
        super(icon, key, label, tooltip, world);
    }

    @Override
    protected String getTooltip(IconMarkerEntity marker) {
        String tooltip = "";
        var additions = marker.getAdditions();
        if (additions != null) {
            tooltip = additions.getName();
        }
        return tooltip.isEmpty() ? super.getTooltip(marker) : tooltip;
    }

    public void setName(BlockPos pos, String name) {
        var markerIdentifier = new MarkerIdentifier(getWorld().getKey(), key, pos.getX(), pos.getZ());
        var marker = Pl3xMarkers.iconMarkerAdditionsRepository().updateName(markerIdentifier, name);
        if (marker != null) {
            super.removeMarker(toMarkerKey(pos.getX(), pos.getZ()));
            addMarker(createIconMarker(marker));
        }
    }
}