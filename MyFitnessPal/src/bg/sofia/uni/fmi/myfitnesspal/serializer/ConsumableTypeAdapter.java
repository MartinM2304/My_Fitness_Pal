package bg.sofia.uni.fmi.myfitnesspal.serializer;

import bg.sofia.uni.fmi.myfitnesspal.items.*;
import bg.sofia.uni.fmi.myfitnesspal.items.tracker.ConsumptionEntry;
import bg.sofia.uni.fmi.myfitnesspal.items.tracker.FoodConsumptionEntry;
import bg.sofia.uni.fmi.myfitnesspal.items.tracker.WaterConsumptionEntry;
import bg.sofia.uni.fmi.myfitnesspal.serializer.visitor.JsonItemVisitor;
import com.google.gson.*;

import java.lang.reflect.Type;
import java.time.LocalDate;
import java.util.List;
import java.util.Map;

public class ConsumableTypeAdapter
        implements JsonSerializer<Consumable>, JsonDeserializer<Consumable> {
    private final Map<Integer, String> foodIds;

    public ConsumableTypeAdapter(Map<Integer, String> foodIds) {
        this.foodIds = foodIds;
    }

    @Override
    public JsonElement serialize(Consumable src, Type typeOfSrc,
                                 JsonSerializationContext context) {
        System.out.println("Serializing: " + src.getClass().getSimpleName());
        JsonItemVisitor visitor = new JsonItemVisitor(foodIds);
        src.accept(visitor);
        JsonObject json = visitor.getResult();
        System.out.println("After visitor: " + json);

        // Add consumptionLog for Food and Water
        if (src instanceof Food food) {
            JsonElement log = context.serialize(food.getConsumptionLog());
            json.add("consumptionLog", log);
            System.out.println("Added Food consumptionLog: " + log);
        } else if (src instanceof Water water) {
            JsonElement log = context.serialize(water.getConsumptionLog());
            json.add("consumptionLog", log);
            System.out.println("Added Water consumptionLog: " + log);
        }

        System.out.println("Final JSON: " + json);
        return json;
    }

    @Override
    public Consumable deserialize(JsonElement json, Type typeOfT,
                                  JsonDeserializationContext context)
            throws JsonParseException {
        JsonObject jsonObject = json.getAsJsonObject();
        String type =
                jsonObject.has("type") ? jsonObject.get("type").getAsString() :
                        null;

        if ("Food".equals(type) ||
                (type == null && jsonObject.has("servingSize"))) {
            return deserializeFood(jsonObject, context);
        } else if ("Water".equals(type) ||
                (type == null && jsonObject.has("consumptionLog") &&
                        !jsonObject.has("name"))) {
            return deserializeWater(jsonObject, context);
        } else if ("Meal".equals(type) ||
                (type == null && jsonObject.has("foodIds"))) {
            return deserializeMeal(jsonObject);
        } else {
            throw new JsonParseException(
                    "Unable to determine Consumable type from JSON: " +
                            jsonObject);
        }
    }

    private Food deserializeFood(JsonObject json,
                                 JsonDeserializationContext context) {
        Food.Builder builder = new Food.Builder(json.get("name").getAsString());
        builder.description(json.has("description") ?
                        json.get("description").getAsString() : "")
                .servingSize(json.get("servingSize").getAsInt())
                .servingsPerContainer(
                        json.get("servingsPerContainer").getAsInt())
                .calories(json.get("calories").getAsInt())
                .carbs(json.get("carbs").getAsDouble())
                .fat(json.get("fat").getAsDouble())
                .protein(json.get("protein").getAsDouble());
        Food food = builder.build();

        if (json.has("consumptionLog")) {
            Type logType =
                    new com.google.gson.reflect.TypeToken<Map<LocalDate, List<ConsumptionEntry>>>() {
                    }.getType();
            Map<LocalDate, List<ConsumptionEntry>> log =
                    context.deserialize(json.get("consumptionLog"), logType);
            log.forEach((date, entries) -> entries.forEach(entry -> {
                if (entry instanceof FoodConsumptionEntry fce) {
                    food.consumpt(date, fce.getServings(), fce.getMealTime());
                }
            }));
        }
        return food;
    }

    private Water deserializeWater(JsonObject json,
                                   JsonDeserializationContext context) {
        Water water = new Water();
        if (json.has("consumptionLog")) {
            Type logType =
                    new com.google.gson.reflect.TypeToken<Map<LocalDate, List<ConsumptionEntry>>>() {
                    }.getType();
            Map<LocalDate, List<ConsumptionEntry>> log =
                    context.deserialize(json.get("consumptionLog"), logType);
            log.forEach((date, entries) -> entries.forEach(entry -> {
                if (entry instanceof WaterConsumptionEntry wce) {
                    water.drink(date, wce.getQuantity());
                }
            }));
        }
        return water;
    }

    private Meal deserializeMeal(JsonObject json) {
        Meal meal = new Meal(json.get("name").getAsString());
        if (json.has("foodIds")) {
            JsonArray foodIdsArray = json.get("foodIds").getAsJsonArray();
            foodIdsArray.forEach(foodId -> {
                int id = foodId.getAsInt();
                String foodKey = foodIds.get(id);
                // Population happens in ItemSerializer
            });
        }
        return meal;
    }
}