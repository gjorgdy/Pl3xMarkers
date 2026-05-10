package nl.gjorgdy.pl3xmarkers.core.interfaces;

import nl.gjorgdy.pl3xmarkers.core.interfaces.entities.IMarker;

import java.util.Collection;
import java.util.function.Consumer;

public interface IMarkerRepository<T extends IMarker> {

	/**
	 * Return a copy of all markers
	 *
	 * @return a copy of the set of all markers
	 */
	@SuppressWarnings("Unused")
	Collection<T> copy();

	/**
	 * Performs the given action for each marker in the {@code IMarkerRepository}
	 * until all elements have been processed or the action throws an
	 * exception. Actions are performed in the order of iteration, if that
	 * order is specified. Exceptions thrown by the action are relayed to the
	 * caller.
	 *
	 * @param action The action to be performed for each element
	 * @throws NullPointerException if the specified action is null
	 */
	@SuppressWarnings("Unused")
	void foreach(Consumer<T> action);

}
