package nl.gjorgdy.pl3xmarkers.core.interfaces.api;

import nl.gjorgdy.pl3xmarkers.core.objects.LayerFactory;

public interface IApi {

	IWorldApi getWorld(String worldIdentifier);

	void registerMarkerLayer(LayerFactory factory);

	void registerIconImage(String path, String filename, String filetype);

}
