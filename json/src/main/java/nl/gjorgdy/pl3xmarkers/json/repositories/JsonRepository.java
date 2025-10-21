package nl.gjorgdy.pl3xmarkers.json.repositories;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import nl.gjorgdy.pl3xmarkers.json.entities.AreaMarker;
import nl.gjorgdy.pl3xmarkers.json.entities.Point;
import nl.gjorgdy.pl3xmarkers.json.serializers.PointSerializer;

import java.io.*;
import java.util.*;

public abstract class JsonRepository<T> {

	private final Gson gson;
	private final String folderPath;
	private final String filePath;
	private final Class<T[]> markerClass;

	private boolean dirty = false;

	protected Set<T> markers;

	public JsonRepository(String folderPath, String fileName, Class<T[]> markerClass) {
		this.gson = new GsonBuilder()
						.setPrettyPrinting()
						.registerTypeAdapter(Point.class, new PointSerializer())
						.create();
		this.folderPath = folderPath;
		this.filePath = folderPath + "/"+ fileName + ".json";
		this.markerClass = markerClass;
		this.markers = new HashSet<>();
		read();
	}

	final public void markDirty() {
		this.dirty = true;
	}

	private void checkFile() {
		try {
			// make sure parent directories exist
			var folder = new File(folderPath);
			var unused = folder.mkdirs();
			// create file if it doesn't exist
			var file = new File(filePath);
			if (!file.exists()) {
				file.createNewFile()
			}
		} catch (IOException e) {
			System.out.println(e.getMessage());
		}
	}

	final public void write() {
		if (!dirty) return;
		try (Writer writer = new FileWriter(filePath, false)) {
			checkFile();
			var array = markers.toArray();
//			System.out.println(gson.toJson(array));
			gson.toJson(array, writer);
			dirty = false;
		} catch (IOException e) {
			System.out.println(e.getMessage());
		}
	}

	final public void read() {
		try {
			checkFile();
			BufferedReader bufferedReader = new BufferedReader(new FileReader(filePath));
			var array = gson.fromJson(bufferedReader, markerClass);
			if (array == null) return;
			markers = new HashSet<>(
				Arrays.stream(array).peek(m -> {
					if (m instanceof AreaMarker am) {
						am.areaMarkerRepository = (AreaMarkerRepository) this;
					}
				}).toList()
			);
		} catch (IOException e) {
			System.out.println(e.getMessage());
		}
	}

}
