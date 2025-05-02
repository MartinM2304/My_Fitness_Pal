package bg.sofia.uni.fmi.myfitnesspal.client.serializer;

import bg.sofia.uni.fmi.myfitnesspal.client.items.Food;
import bg.sofia.uni.fmi.myfitnesspal.client.items.Consumable;
import bg.sofia.uni.fmi.myfitnesspal.client.items.Meal;
import bg.sofia.uni.fmi.myfitnesspal.client.items.Water;
import bg.sofia.uni.fmi.myfitnesspal.client.items.tracker.ConsumptionEntry;
import bg.sofia.uni.fmi.myfitnesspal.client.items.tracker.FoodConsumptionEntry;
import bg.sofia.uni.fmi.myfitnesspal.client.items.tracker.WaterConsumptionEntry;
import bg.sofia.uni.fmi.myfitnesspal.client.provider.strategies.IOStrategy;
import bg.sofia.uni.fmi.myfitnesspal.client.serializer.visitor.JsonItemVisitor;
import com.google.gson.JsonArray;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;

import java.lang.reflect.Type;
import java.time.LocalDate;
import java.util.List;
import java.util.Map;

public class ConsumableTypeAdapter
        implements JsonSerializer<Consumable>, JsonDeserializer<Consumable> {
    private final Map<Integer, String> foodIds;
    private final IOStrategy ioStrategy;

    public ConsumableTypeAdapter(Map<Integer, String> foodIds,
                                 IOStrategy ioStrategy) {
        this.foodIds = foodIds;
        this.ioStrategy = ioStrategy;
    }

    @Override
    public JsonElement serialize(Consumable src, Type typeOfSrc,
                                 JsonSerializationContext context) {
        ioStrategy.display("Serializing: " + src.getClass().getSimpleName());
        JsonItemVisitor visitor = new JsonItemVisitor(foodIds);
        src.accept(visitor);
        JsonObject json = visitor.getResult();
        ioStrategy.display("After visitor: " + json);

        if (src instanceof Food food) {
            JsonElement log = context.serialize(food.getConsumptionLog());
            json.add("consumptionLog", log);
            ioStrategy.display("Added Food consumptionLog: " + log);
        } else if (src instanceof Water water) {
            JsonElement log = context.serialize(water.getConsumptionLog());
            json.add("consumptionLog", log);
            ioStrategy.display("Added Water consumptionLog: " + log);
        }

        ioStrategy.display("Final JSON: " + json);
        return json;
    }

    @Override
    public Consumable deserialize(JsonElement json, Type typeOfT,
                                  JsonDeserializationContext context)
            throws JsonParseException {
        JsonObject jsonObject = json.getAsJsonObject();
        String type =
                jsonObject.has("type") ? jsonObject.get("type").getAsString()
                        : null;

        if ("Food".equals(type)
                || (type == null && jsonObject.has("servingSize"))) {
            return deserializeFood(jsonObject, context);
        } else if ("Water".equals(type)
                || (type == null && jsonObject.has("consumptionLog")
                && !jsonObject.has("name"))) {
            return deserializeWater(jsonObject, context);
        } else if ("Meal".equals(type)
                || (type == null && jsonObject.has("foodIds"))) {
            return deserializeMeal(jsonObject);
        } else {
            throw new JsonParseException(
                    "Unable to determine Consumable type from JSON: "
                            + jsonObject);
        }
    }

    private Food deserializeFood(JsonObject json,
                                 JsonDeserializationContext context) {
        Food.Builder builder = new Food.Builder(json.get("name").getAsString());
        builder.description(json.has("description")
                        ? json.get("description").getAsString() : "")
                .servingSize(json.get("servingSize").getAsInt())
                .servingsPerContainer(
                        json.get("servingsPerContainer").getAsInt())
                .calories(json.get("calories").getAsInt())
                .carbs(json.get("carbs").getAsDouble())
                .fat(json.get("fat").getAsDouble())
                .protein(json.get("protein").getAsDouble())
                .isRecipe(json.get("isRecipe").getAsBoolean());

        Food food = builder.build();

        if (json.has("consumptionLog")) {
            Type logType =
                    new com.google.gson.reflect.TypeToken<Map<LocalDate,
                            List<ConsumptionEntry>>>() {
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
                    new com.google.gson.reflect.TypeToken<Map<LocalDate,
                            List<ConsumptionEntry>>>() {
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
            });
        }
        return meal;
    }
}
