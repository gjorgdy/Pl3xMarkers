package eu.hexasis.helixmarkers.layers;

import eu.hexasis.helixmarkers.markers.IconBuilder;
import net.pl3x.map.core.markers.marker.Marker;
import net.pl3x.map.core.world.World;
import org.intellij.lang.annotations.Language;
import org.jetbrains.annotations.NotNull;

public class NetherPortalMarkerLayer extends SimpleIconMarkerLayer {

    public NetherPortalMarkerLayer(String icon, String key, String label, @Language("HTML") String tooltip, @NotNull World world) {
        super(icon, key, label, tooltip, world);
    }

    @Override
    protected Marker<?> createSimpleMarker(int x, int z) {

        String worldKey = getWorld().getKey();
        boolean isOverworld = worldKey.equals("minecraft:overworld");

        String destinationKey = isOverworld ? "minecraft-the_nether" : "minecraft-overworld";
        int relativeX = isOverworld ? x / 8 : x * 8;
        int relativeZ = isOverworld ? z / 8 : z * 8;
        String buttonText = isOverworld ? "Go to Nether" : "Go to Overworld";

        @Language("HTML") String html = """
            <div style='display: flex; flex-direction: column; gap: 0.5rem'>
                <b>Nether Portal</b>
                <button onclick="(function() {
                  const baseUrl = window.location.href.split('?')[0] || window.location.href;
                  const params = new URLSearchParams(window.location.search);
                  params.set('world', '%s');
                  params.set('x', '%d');
                  params.set('z', '%d');
                  window.location.href = baseUrl + '?' + params.toString();
                })()">
                    %s
                </button>
            </div>
        """;

        return IconBuilder.newIconMarker(
                        toMarkerKey(x, z),
                        iconId,
                        x, z
                )
                .centerIcon(16, 16)
                .addPopup(String.format(html, destinationKey, relativeX, relativeZ, buttonText))
                .build();
    }

}
