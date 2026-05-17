package nl.gjorgdy.pl3xmarkers.core.json.repositories;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import nl.gjorgdy.pl3xmarkers.core.deprecated.interfaces.IStorage;
import nl.gjorgdy.pl3xmarkers.core.interfaces.IMarkerRepository;
import nl.gjorgdy.pl3xmarkers.core.json.entities.Marker;
import nl.gjorgdy.pl3xmarkers.core.json.entities.Point;
import nl.gjorgdy.pl3xmarkers.core.json.serializers.PointSerializer;

import java.io.*;
import java.util.Collection;
import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.function.Consumer;

public abstract class MarkerRepository<T extends Marker> implements IMarkerRepository<T> {

	public final String worldIdentifier;
	public final String layerKey;
	private final Gson gson;
	private final String folderPath;
	private final String filePath;
	private final TypeToken<?> markerClass;
	private final AtomicBoolean dirty = new AtomicBoolean(false);
	protected HashSet<T> data;

	public MarkerRepository(WorldRepository worldRepository, String layerKey, Class<T> clazz) {
		worldIdentifier = worldRepository.worldIdentifier;
		this.layerKey = layerKey;
		gson = new GsonBuilder()
				.setPrettyPrinting()
				.registerTypeAdapter(Point.class, new PointSerializer())
				.create();
		folderPath = worldRepository.getStorage().getConfigPath() + "/" + formatWorldIdentifier(
				worldRepository.worldIdentifier);
		filePath = folderPath + "/" + layerKey + ".json";
		markerClass = TypeToken.getParameterized(HashSet.class, clazz);
		data = new HashSet<>();
		read();
	}

	@Deprecated
	abstract public void migrate(IStorage oldJsonStorage);

	public String formatWorldIdentifier(String worldIdentifier) {
		if (worldIdentifier.contains(":")) {
			return worldIdentifier.split(":")[1];
		}
		return worldIdentifier;
	}

	@Override
	public Collection<T> copy() {
		return Set.copyOf(data);
	}

	@Override
	public void foreach(Consumer<T> action) {
		data.forEach(action);
	}

	/**
	 * Marks the repository as dirty, indicating that it needs to be written to disk.
	 */
	final public void markDirty() {
		dirty.set(true);
	}

	private boolean fileExists() {
		var file = new File(filePath);
		return file.exists();
	}

	private boolean invalidFile() throws IOException {
		// Ensure the folder exists and is a directory
		var folder = new File(folderPath);
		if ((!folder.exists() && !folder.mkdirs()) || !folder.isDirectory()) {
			return true; // could not create folders -> invalid
		}
		// Ensure the file exists and is a file
		var file = new File(filePath);
		if (!file.exists()) {
			// make sure parent exists (defensive)
			var parent = file.getParentFile();
			if (parent != null && !parent.exists()) {
				if (!parent.mkdirs()) {
					return true;
				}
			}
			return !file.createNewFile();
		}
		return !file.isFile();
	}

	final public void write() {
		if (!dirty.get()) {
			return;
		}
		try {
			if (invalidFile() || data == null || data.isEmpty()) {
				return;
			}
			try (Writer writer = new FileWriter(filePath, false)) {
				gson.toJson(data, writer);
			}
			dirty.set(false);
		} catch (IOException e) {
			System.out.println(e.getMessage());
		}
	}

	final public void read() {
		// only read if not dirty
		if (dirty.get()) {
			return;
		}
		try {
			if (!fileExists()) {
				return;
			}
			BufferedReader bufferedReader = new BufferedReader(new FileReader(filePath));
			//noinspection unchecked
			HashSet<T> data = (HashSet<T>) gson.fromJson(bufferedReader, markerClass);
			if (data == null) {
				return;
			}
			assert data instanceof HashSet<T>;
			data.forEach(marker -> marker.SetContext(this));
			this.data = data;
			bufferedReader.close();
		} catch (IOException e) {
			System.out.println(e.getMessage() + "[" + filePath + "]");
		}
	}

}
