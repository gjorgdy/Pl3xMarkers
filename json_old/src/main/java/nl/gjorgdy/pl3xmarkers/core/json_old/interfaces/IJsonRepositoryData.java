package nl.gjorgdy.pl3xmarkers.core.json_old.interfaces;

import nl.gjorgdy.pl3xmarkers.core.json_old.repositories.JsonRepository;

public interface IJsonRepositoryData {

	void strip();

	boolean isEmpty();

	void setContext(JsonRepository<?> jsonRepository);

}
