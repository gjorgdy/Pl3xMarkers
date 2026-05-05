package nl.gjorgdy.pl3xmarkers.core.interfaces;

import nl.gjorgdy.pl3xmarkers.core.interfaces.entities.ISimpleMarker;

public interface ISimpleMarkerRepository<T extends ISimpleMarker> extends IMarkerRepository<T> {

	@SuppressWarnings("Unused")
	T create(int x, int y, int z);

	@SuppressWarnings("Unused")
	T get(int x, int y, int z);

	@SuppressWarnings("Unused")
	T getOrCreate(int x, int y, int z);

	@SuppressWarnings("Unused")
	boolean remove(int x, int y, int z);

}
