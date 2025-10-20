package nl.gjorgdy.pl3xmarkers.core;

import nl.gjorgdy.pl3xmarkers.core.layers.primitive.MarkerLayer;
import nl.gjorgdy.pl3xmarkers.core.objects.IconImageAddress;
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
import java.util.function.Function;

public class Pl3xMapHandler implements EventListener {

    public void registerMarkerLayer(Function<World, MarkerLayer> function) {
        Pl3xMap.api().getWorldRegistry().forEach(world -> {
            MarkerLayer swl = function.apply(world);
            if (!swl.isInWorld(world)) return;
            world.getLayerRegistry().register(swl);
            swl.load();
        });
    }

    public void registerIconImage(String path, String filename, String filetype) {
        try {
            registerIconImage(new IconImageAddress(path, filename, filetype));
        } catch (IOException e) {
            System.out.println("Failed to register icon: " + path);
        }
    }

    @EventHandler
    @SuppressWarnings("unused") // event is used by pl3xmap
    public void onWorldLoad(WorldLoadedEvent event) {
        Layers.getLayerFactories().forEach(function -> {
            MarkerLayer swl = function.apply(event.getWorld());
			System.out.println("Registering layer " + swl.getClass().getName() + " for world " + event.getWorld().getKey());
            if (!swl.isInWorld(event.getWorld())) {
				System.out.println("not in world, skipping");
				return;
			}
            event.getWorld().getLayerRegistry().register(swl);
            swl.load();
        });
    }

    @EventHandler
    @SuppressWarnings("unused") // event is used by pl3xmap
    public void onEnable(Pl3xMapEnabledEvent event) {
        Icons.ALL.forEach(address -> {
            try {
                registerIconImage(address);
            } catch (IOException e) {
                System.out.println("Failed to register icon: " + address);
//                Pl3xMarkersCore.LOGGER.error("Failed to register icon", e);
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