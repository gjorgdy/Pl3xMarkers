package nl.gjorgdy.pl3xmarkers.json.repositories;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import nl.gjorgdy.pl3xmarkers.json.entities.AreaMarker;
import nl.gjorgdy.pl3xmarkers.json.entities.Point;
import nl.gjorgdy.pl3xmarkers.json.serializers.PointSerializer;

import java.io.*;
import java.util.*;
import java.util.concurrent.atomic.AtomicBoolean;

public abstract class JsonRepository<T> {

	private final Gson gson;
	private final String folderPath;
	private final String filePath;
	private final Class<T[]> markerClass;

	private final AtomicBoolean dirty = new AtomicBoolean(false);

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

	/**
	 * Marks the repository as dirty, indicating that it needs to be written to disk.
	 */
	final public void markDirty() {
		this.dirty.set(true);
	}

	private boolean invalidFile() throws IOException {
		// make sure parent directories exist
		var folder = new File(folderPath);
		var madeFolders = folder.mkdirs();
		// create file if it doesn't exist
		var file = new File(filePath);
		if (file.exists()) return false;
		var madeFile = file.createNewFile();
		return !madeFolders || !madeFile;
	}

	final public void write() {
		if (!dirty.get()) return;
		try (Writer writer = new FileWriter(filePath, false)) {
			if (invalidFile()) return;
			var array = markers.toArray();
			gson.toJson(array, writer);
			dirty.set(false);
		} catch (IOException e) {
			System.out.println(e.getMessage());
		}
	}

	final public void read() {
		// only read if not dirty
		if (dirty.get()) return;
		try {
			if (invalidFile()) return;
			BufferedReader bufferedReader = new BufferedReader(new FileReader(filePath));
			var array = gson.fromJson(bufferedReader, markerClass);
			if (array == null) return;
			markers = new HashSet<>(
				Arrays.stream(array).peek(m -> {
					if (m instanceof AreaMarker am) {
						am.repository = (AreaMarkerRepository) this;
					}
				}).toList()
			);
		} catch (IOException e) {
			System.out.println(e.getMessage());
		}
	}

}
