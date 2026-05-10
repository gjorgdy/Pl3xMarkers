package nl.gjorgdy.pl3xmarkers.core.interfaces;

public interface IStorage {

	IWorldRepository getWorldRepository(String worldIdentifier);

	void close();
}
