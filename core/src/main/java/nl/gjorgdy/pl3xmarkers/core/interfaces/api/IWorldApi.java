package nl.gjorgdy.pl3xmarkers.core.interfaces.api;

import nl.gjorgdy.pl3xmarkers.core.layers.primitive.MarkerLayer;

public interface IWorldApi {

	<T extends MarkerLayer> T getLayer(Class<T> layerClass, String layerKey);

}
