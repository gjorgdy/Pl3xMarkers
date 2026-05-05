package nl.gjorgdy.pl3xmarkers.core.json.repositories;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import nl.gjorgdy.pl3xmarkers.core.interfaces.IMarkerRepository;
import nl.gjorgdy.pl3xmarkers.core.json.entities.Marker;
import nl.gjorgdy.pl3xmarkers.core.json.entities.Point;
import nl.gjorgdy.pl3xmarkers.core.json.entities.SignMarker;
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
				.registerTypeAdapter(SignMarker.class, new PointSerializer())
				.create();
		folderPath = worldRepository.getStorage().getConfigPath() + "/" + worldRepository.worldIdentifier;
		filePath = folderPath + "/" + layerKey + ".json";
		markerClass = TypeToken.getParameterized(HashSet.class, clazz);
		data = new HashSet<>();
		read();
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

	private boolean invalidFile() throws IOException {
		// make sure parent directories exist
		var folder = new File(folderPath);
		var madeFolders = folder.mkdirs();
		// Create file if it doesn't exist
		var file = new File(filePath);
		if (file.exists()) {
			return false;
		}
		var madeFile = file.createNewFile();
		return !madeFolders || !madeFile;
	}

	final public void write() {
		if (!dirty.get()) {
			return;
		}
		try (Writer writer = new FileWriter(filePath, false)) {
			if (invalidFile()) {
				return;
			}
			if (data == null || data.isEmpty()) {
				return;
			}
			gson.toJson(data, writer);
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
			if (invalidFile()) {
				return;
			}
			BufferedReader bufferedReader = new BufferedReader(new FileReader(filePath));
			//noinspection unchecked
			data = (HashSet<T>) gson.fromJson(bufferedReader, markerClass);
			assert data instanceof HashSet<T>;
			data.forEach(marker -> marker.SetContext(this));
		} catch (IOException e) {
			System.out.println(e.getMessage());
		}
	}

}
