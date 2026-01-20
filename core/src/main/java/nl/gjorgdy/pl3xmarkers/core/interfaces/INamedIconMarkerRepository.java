package nl.gjorgdy.pl3xmarkers.core.interfaces;

import nl.gjorgdy.pl3xmarkers.core.interfaces.entities.INamedIconMarker;
import org.intellij.lang.annotations.Language;

import java.util.Collection;

public interface INamedIconMarkerRepository<T extends INamedIconMarker> {

	@SuppressWarnings("Unused")
	Collection<T> getNamedIconMarkers(String worldIdentifier, String layerKey);

	boolean renameIconMarker(String worldIdentifier, String layerKey, int x, int z, @Language("HTML") String newName);

	@SuppressWarnings("Unused")
	T createNamedIconMarker(String worldIdentifier, String layerKey, int x, int z, @Language("HTML") String name);

	@SuppressWarnings("Unused")
	boolean removeNamedIconMarker(String worldIdentifier, String layerKey, int x, int z);

}
