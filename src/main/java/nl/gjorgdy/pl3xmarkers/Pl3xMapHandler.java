package nl.gjorgdy.pl3xmarkers;

import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.server.world.ServerWorld;
import nl.gjorgdy.pl3xmarkers.compat.openpartiesandclaims.OpenPartiesAndClaims;
import nl.gjorgdy.pl3xmarkers.layers.primitive.MarkerLayer;
import nl.gjorgdy.pl3xmarkers.objects.IconImageAddress;
import net.pl3x.map.core.Pl3xMap;
import net.pl3x.map.core.event.EventHandler;
import net.pl3x.map.core.event.EventListener;
import net.pl3x.map.core.event.server.Pl3xMapEnabledEvent;
import net.pl3x.map.core.event.world.WorldLoadedEvent;
import net.pl3x.map.core.image.IconImage;
import net.pl3x.map.core.registry.IconRegistry;
import net.pl3x.map.core.world.World;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;

public class Pl3xMapHandler implements EventListener {

    private final List<Function<World, MarkerLayer>> worldLayerFunctions = new ArrayList<>();
    private final List<IconImageAddress> iconAddresses = new ArrayList<>();

    public void registerMarkerLayer(Function<World, MarkerLayer> function) {
        worldLayerFunctions.add(function);
    }

    public void registerIconImage(String path, String filename, String filetype) {
        iconAddresses.add(
            new IconImageAddress(path, filename, filetype)
        );
    }

    @EventHandler
    @SuppressWarnings("unused") // event is used by pl3xmap
    public void onWorldLoad(WorldLoadedEvent event) {
        worldLayerFunctions.forEach(function -> {
            MarkerLayer swl = function.apply(event.getWorld());
            if (!swl.isInWorld(event.getWorld())) return;
            event.getWorld().getLayerRegistry().register(swl);
            swl.load();
        });
    }

    @EventHandler
    @SuppressWarnings("unused") // event is used by pl3xmap
    public void onEnable(Pl3xMapEnabledEvent event) {
        iconAddresses.forEach(address -> {
            try {
                registerIconImage(address);
            } catch (IOException e) {
                Pl3xMarkers.LOGGER.error("Failed to register icon", e);
            }
        });
    }

    private void registerIconImage(IconImageAddress address) throws IOException {
        // get registry
        IconRegistry iconRegistry = Pl3xMap.api().getIconRegistry();
        if (iconRegistry.has(address.fileName())) return;
        // get file
        String path = address.path() + address.fileName() + "." + address.fileType();
        InputStream inputStream = Pl3xMapHandler.class.getResourceAsStream(path);
        if (inputStream == null) throw new IOException("Resource not found: " + path);
        // read file
        BufferedImage image = ImageIO.read(inputStream);
        // register
        iconRegistry.register(new IconImage(address.fileName(), image, address.fileType()));
    }

}