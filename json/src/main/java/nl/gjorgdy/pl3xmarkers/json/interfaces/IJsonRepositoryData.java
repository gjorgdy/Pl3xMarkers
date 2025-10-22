package nl.gjorgdy.pl3xmarkers.json.interfaces;

import nl.gjorgdy.pl3xmarkers.json.repositories.JsonRepository;

public interface IJsonRepositoryData {

	void strip();

	boolean isEmpty();

	void setContext(JsonRepository<?> jsonRepository);

}
