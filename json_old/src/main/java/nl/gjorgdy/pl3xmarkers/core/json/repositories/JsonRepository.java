package nl.gjorgdy.pl3xmarkers.core.json.repositories;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import nl.gjorgdy.pl3xmarkers.core.json.entities.IconMarker;
import nl.gjorgdy.pl3xmarkers.core.json.entities.Point;
import nl.gjorgdy.pl3xmarkers.core.json.interfaces.IJsonRepositoryData;
import nl.gjorgdy.pl3xmarkers.core.json.serializers.PointSerializer;

import java.io.*;
import java.util.concurrent.atomic.AtomicBoolean;

public abstract class JsonRepository<T extends IJsonRepositoryData> {

	private final Gson gson;
	private final String folderPath;
	private final String filePath;
	private final TypeToken<?> markerClass;

	private final AtomicBoolean dirty = new AtomicBoolean(false);

	protected T data;

	public JsonRepository(String folderPath, String fileName, TypeToken<?> typeToken, T defaultData) {
		this.gson = new GsonBuilder()
						.setPrettyPrinting()
						.registerTypeAdapter(Point.class, new PointSerializer())
						.registerTypeAdapter(IconMarker.class, new PointSerializer())
						.create();
		this.folderPath = folderPath;
		this.filePath = folderPath + "/"+ fileName + ".json";
		this.data = defaultData;
		this.markerClass = typeToken;
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
//			var array = markers.toArray();
			if (data == null || data.isEmpty()) return;
			data.strip();
			gson.toJson(data, writer);
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
			var jsonObject = gson.fromJson(bufferedReader, markerClass);
			if (jsonObject instanceof IJsonRepositoryData jsonData && !jsonData.isEmpty()) {
				jsonData.setContext(this);
				//noinspection unchecked
				data = (T) jsonData;
			}
		} catch (IOException e) {
			System.out.println(e.getMessage());
		}
	}

}
