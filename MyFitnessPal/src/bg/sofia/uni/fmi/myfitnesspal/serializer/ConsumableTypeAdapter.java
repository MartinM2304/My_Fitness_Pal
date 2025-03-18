package bg.sofia.uni.fmi.myfitnesspal.serializer;

import bg.sofia.uni.fmi.myfitnesspal.items.Consumable;
import com.google.gson.*;

import java.lang.reflect.Type;

public class ConsumableTypeAdapter implements JsonSerializer<Consumable>, JsonDeserializer<Consumable> {

    @Override
    public JsonElement serialize(Consumable src, Type typeOfSrc, JsonSerializationContext context) {
        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("type", src.getClass().getSimpleName());
        jsonObject.add("properties", context.serialize(src, src.getClass()));
        return jsonObject;
    }

    @Override
    public Consumable deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
        JsonObject jsonObject = json.getAsJsonObject();
        String type = jsonObject.get("type").getAsString();
        JsonElement properties = jsonObject.get("properties");

        try {
            return context.deserialize(properties, Class.forName("bg.sofia.uni.fmi.myfitnesspal.items." + type));
        } catch (ClassNotFoundException e) {
            throw new JsonParseException("Unknown element type: " + type, e);
        }
    }
}