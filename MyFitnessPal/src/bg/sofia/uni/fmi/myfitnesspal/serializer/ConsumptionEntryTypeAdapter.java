package bg.sofia.uni.fmi.myfitnesspal.serializer;

import bg.sofia.uni.fmi.myfitnesspal.items.MealTime;
import bg.sofia.uni.fmi.myfitnesspal.items.tracker.ConsumptionEntry;
import bg.sofia.uni.fmi.myfitnesspal.items.tracker.FoodConsumptionEntry;
import bg.sofia.uni.fmi.myfitnesspal.items.tracker.WaterConsumptionEntry;
import com.google.gson.*;

import java.lang.reflect.Type;

public class ConsumptionEntryTypeAdapter
        implements JsonSerializer<ConsumptionEntry>,
        JsonDeserializer<ConsumptionEntry> {

    @Override
    public JsonElement serialize(ConsumptionEntry src, Type typeOfSrc,
                                 JsonSerializationContext context) {
        JsonObject jsonObject = new JsonObject();

        jsonObject.addProperty("quantity", src.getQuantity());

        if (src instanceof FoodConsumptionEntry) {
            jsonObject.addProperty("type", "FoodConsumption");
            FoodConsumptionEntry fce = (FoodConsumptionEntry) src;
            jsonObject.addProperty("servings", fce.getServings());
            jsonObject.addProperty("mealTime", fce.getMealTime().name());
        } else if (src instanceof WaterConsumptionEntry) {
            jsonObject.addProperty("type", "WaterConsumption");
        }

        return jsonObject;
    }

    @Override
    public ConsumptionEntry deserialize(JsonElement json, Type typeOfT,
                                        JsonDeserializationContext context)
            throws JsonParseException {
        JsonObject jsonObject = json.getAsJsonObject();
        String type =
                jsonObject.has("type") ? jsonObject.get("type").getAsString() :
                        null;

        if ("FoodConsumption".equals(type)) {
            return deserializeFoodConsumption(jsonObject);
        } else if ("WaterConsumption".equals(type)) {
            int quantity = jsonObject.get("quantity").getAsInt();
            return new WaterConsumptionEntry(quantity);
        } else if (jsonObject.has("servings") && jsonObject.has("mealTime")) {
            return deserializeFoodConsumption(jsonObject);
        } else if (jsonObject.has("quantity")) {
            int quantity = jsonObject.get("quantity").getAsInt();
            return new WaterConsumptionEntry(quantity);
        } else {
            throw new JsonParseException(
                    "Unknown ConsumptionEntry type: " + jsonObject);
        }
    }

    private FoodConsumptionEntry deserializeFoodConsumption(
            JsonObject jsonObject) {
        int servings = jsonObject.get("servings").getAsInt();

        if (!jsonObject.has("mealTime")) {
            throw new JsonParseException(
                    "Missing mealTime field for FoodConsumptionEntry");
        }

        String mealTimeStr = jsonObject.get("mealTime").getAsString();
        MealTime mealTime;

        try {
            mealTime = MealTime.valueOf(mealTimeStr);
        } catch (IllegalArgumentException e) {
            mealTime = MealTime.fromString(mealTimeStr);
        }

        return new FoodConsumptionEntry(servings, mealTime);
    }
}