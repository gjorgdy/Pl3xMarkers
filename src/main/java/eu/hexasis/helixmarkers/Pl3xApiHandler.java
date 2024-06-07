package eu.hexasis.helixmarkers;

import eu.hexasis.helixmarkers.layers.SimpleWorldLayer;
import eu.hexasis.helixmarkers.objects.IconAddress;
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

public class Pl3xApiHandler {

    private final List<Function<World, SimpleWorldLayer>> worldLayerFunctions = new ArrayList<>();
    private final List<IconAddress> iconAddresses = new ArrayList<>();

    public void registerSimpleWorldLayer(Function<World, SimpleWorldLayer> function) {
        worldLayerFunctions.add(function);
    }

    public void registerIconAddress(IconAddress iconAddress) {
        iconAddresses.add(iconAddress);
    }

    public Pl3xEventListener getEventListener() {
        return new Pl3xEventListener();
    }

    public class Pl3xEventListener implements EventListener {

        private Pl3xEventListener() {}

        @EventHandler
        public void onWorldLoad(WorldLoadedEvent event) {
            worldLayerFunctions.forEach(function -> {
                SimpleWorldLayer swl = function.apply(event.getWorld());
                event.getWorld().getLayerRegistry().register(swl);
            });
        }

        @EventHandler
        public void onEnable(Pl3xMapEnabledEvent event) {
            iconAddresses.forEach(address -> {
                try {
                    registerIcon(address);
                } catch (IOException e) {
                    HelixMarkers.LOGGER.error("Failed to register icon", e);
                }
            });
        }

    }

    private void registerIcon(IconAddress address) throws IOException {
        // get registry
        IconRegistry iconRegistry = Pl3xMap.api().getIconRegistry();
        if (iconRegistry.has(address.fileName())) return;
        // get file
        String path = address.path() + address.fileName() + "." + address.fileType();
        InputStream inputStream = Pl3xApiHandler.class.getResourceAsStream(path);
        if (inputStream == null) throw new IOException("Resource not found: " + path);
        // read file
        BufferedImage image = ImageIO.read(inputStream);
        // register
        iconRegistry.register(new IconImage(address.fileName(), image, address.fileType()));
    }

}
