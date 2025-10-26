package nl.gjorgdy.pl3xmarkers.core.json.serializers;

import com.google.gson.*;
import nl.gjorgdy.pl3xmarkers.core.interfaces.entities.IPoint;
import nl.gjorgdy.pl3xmarkers.core.json.entities.IconMarker;
import nl.gjorgdy.pl3xmarkers.core.json.entities.Point;

import java.lang.reflect.Type;

public class PointSerializer implements JsonSerializer<IPoint>, JsonDeserializer<IPoint> {

	@Override
	public JsonElement serialize(IPoint src, Type typeOfSrc, JsonSerializationContext context) {
		var obj = new JsonArray();
		obj.add(src.getX());
		obj.add(src.getZ());
		return obj;
	}

	@Override
	public IPoint deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
		var arr = json.getAsJsonArray();
		int x = arr.get(0).getAsInt();
		int z = arr.get(1).getAsInt();
		if (typeOfT.equals(Point.class)) {
			return new Point(x, z);
		} else if (typeOfT.equals(IconMarker.class)) {
			return new IconMarker(x, z);
		}
		throw new JsonParseException("Unsupported IPoint type: " + typeOfT.getTypeName());
	}

}
