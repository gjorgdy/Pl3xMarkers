package nl.gjorgdy.pl3xmarkers.core.interfaces.api;

import nl.gjorgdy.pl3xmarkers.core.layers.primitive.MarkerLayer;
import org.jspecify.annotations.Nullable;

public interface IWorldApi {

	@Nullable
	<T extends MarkerLayer> T getLayer(Class<T> layerClass, String layerKey);

}
