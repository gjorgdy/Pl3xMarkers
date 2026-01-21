package nl.gjorgdy.pl3xmarkers.core.interfaces;

import nl.gjorgdy.pl3xmarkers.core.interfaces.entities.ISignMarker;
import org.intellij.lang.annotations.Language;

import java.util.Collection;

public interface ISignMarkerRepository<T extends ISignMarker> {

	@SuppressWarnings("Unused")
	Collection<T> getMarkers(String worldIdentifier, String layerKey);

	boolean editMarker(String worldIdentifier, String layerKey, int x, int z, @Language("HTML") String[] text);

	@SuppressWarnings("Unused")
	T createMarker(String worldIdentifier, String layerKey, int x, int z, @Language("HTML") String[] text);

	@SuppressWarnings("Unused")
	boolean removeMarker(String worldIdentifier, String layerKey, int x, int z);

}
