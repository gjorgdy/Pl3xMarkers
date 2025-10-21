package nl.gjorgdy.pl3xmarkers.json.repositories;

import com.google.gson.Gson;

import java.io.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public abstract class JsonRepository<T> {

	private final Gson gson = new Gson();
	private final String filePath;
	private final Class<T[]> markerClass;

	protected List<T> markers;

	public JsonRepository(String filePath, Class<T[]> markerClass) {
		this.filePath = filePath;
		this.markerClass = markerClass;
		this.markers = new ArrayList<>();
		read();
	}

	private void checkFile() {
		try {
			var file = new File(filePath);
//			boolean madeDirs = file.mkdirs();
			if (!file.exists() && !file.createNewFile()) return;
		} catch (IOException e) {
			System.out.println(e.getMessage());
		}
	}

	final public void write() {
		try {
			checkFile();
			var array = markers.toArray();
			gson.toJson(array, new FileWriter(filePath));
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
			markers = new ArrayList<>(
				Arrays.stream(array).toList()
			);
		} catch (IOException e) {
			System.out.println(e.getMessage());
		}
	}

}
