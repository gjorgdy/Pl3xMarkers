package nl.gjorgdy.pl3xmarkers.core.objects;

import net.pl3x.map.core.world.World;
import nl.gjorgdy.pl3xmarkers.core.layers.primitive.MarkerLayer;

import java.util.function.Function;
import java.util.function.Predicate;

public class LayerFactory {

	private final Function<World, MarkerLayer> builder;
	private final Predicate<World> predicate;

	public LayerFactory(Function<World, MarkerLayer> builder, Predicate<World> predicate) {
		this.builder = builder;
		this.predicate = predicate;
	}

	public MarkerLayer create(World world) {
		return builder.apply(world);
	}

	public boolean disabledFor(World world) {
		return !predicate.test(world);
	}

}
