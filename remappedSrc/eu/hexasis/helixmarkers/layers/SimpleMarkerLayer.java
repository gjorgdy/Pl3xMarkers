package eu.hexasis.helixmarkers.layers;

import net.minecraft.util.Identifier;
import net.pl3x.map.core.world.World;
import org.jetbrains.annotations.NotNull;

import java.util.function.BiConsumer;

public class SimpleMarkerLayer extends MarkerLayer {

    private final BiConsumer<LayerAccessor, Identifier> loadConsumer;

    public SimpleMarkerLayer(String key, String label, @NotNull World world, BiConsumer<LayerAccessor, Identifier> loadConsumer) {
        super(key, label, world);
        this.loadConsumer = loadConsumer;
    }

    @Override
    public void load() {
        loadConsumer.accept(new LayerAccessor(this), worldIdentifier);
    }

}
