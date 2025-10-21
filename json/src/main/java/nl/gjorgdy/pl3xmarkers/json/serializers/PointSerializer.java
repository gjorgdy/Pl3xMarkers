package nl.gjorgdy.pl3xmarkers.json.serializers;

import com.google.gson.*;
import nl.gjorgdy.pl3xmarkers.json.entities.Point;

import java.lang.reflect.Type;

public class PointSerializer implements JsonSerializer<Point>, JsonDeserializer<Point> {

	@Override
	public JsonElement serialize(Point src, Type typeOfSrc, JsonSerializationContext context) {
		var obj = new JsonArray();
		obj.add(src.getX());
		obj.add(src.getZ());
		return obj;
	}

	@Override
	public Point deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
		var arr = json.getAsJsonArray();
		int x = arr.get(0).getAsInt();
		int z = arr.get(1).getAsInt();
		return new Point(x, z);
	}

}
