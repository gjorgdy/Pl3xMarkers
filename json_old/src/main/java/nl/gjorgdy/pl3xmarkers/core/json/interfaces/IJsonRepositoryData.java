package nl.gjorgdy.pl3xmarkers.core.json.interfaces;

import nl.gjorgdy.pl3xmarkers.core.json.repositories.JsonRepository;

public interface IJsonRepositoryData {

	void strip();

	boolean isEmpty();

	void setContext(JsonRepository<?> jsonRepository);

}
