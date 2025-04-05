package bg.sofia.uni.fmi.myfitnesspal.serializer;

import bg.sofia.uni.fmi.myfitnesspal.items.*;
import bg.sofia.uni.fmi.myfitnesspal.items.tracker.ConsumptionEntry;
import bg.sofia.uni.fmi.myfitnesspal.items.tracker.FoodConsumptionEntry;
import bg.sofia.uni.fmi.myfitnesspal.items.tracker.WaterConsumptionEntry;
import com.google.gson.*;

import java.lang.reflect.Type;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ConsumableTypeAdapter implements JsonSerializer<Consumable>, JsonDeserializer<Consumable> {

    @Override
    public JsonElement serialize(Consumable src, Type typeOfSrc, JsonSerializationContext context) {
        JsonObject jsonObject = new JsonObject();

        if (src instanceof Food food) {
            jsonObject.addProperty("type", "Food");
            jsonObject.addProperty("name", food.getName());
            jsonObject.addProperty("description", food.getDescription());
            jsonObject.addProperty("servingSize", food.getServingSize());
            jsonObject.addProperty("servingsPerContainer", food.getServingsPerContainer());
            jsonObject.addProperty("calories", food.getCalories());
            jsonObject.addProperty("carbs", food.getCarbs());
            jsonObject.addProperty("fat", food.getFat());
            jsonObject.addProperty("protein", food.getProtein());

            JsonObject logObject = new JsonObject();
            for (Map.Entry<LocalDate, List<ConsumptionEntry>> entry : food.getConsumptionLog().entrySet()) {
                JsonArray entriesArray = new JsonArray();
                for (ConsumptionEntry ce : entry.getValue()) {
                    if (ce instanceof FoodConsumptionEntry fce) {
                        JsonObject entryObject = new JsonObject();
                        entryObject.addProperty("servings", fce.getServings());
                        entryObject.addProperty("mealTime", fce.getMealTime().toString());
                        entriesArray.add(entryObject);
                    }
                }
                logObject.add(entry.getKey().toString(), entriesArray);
            }
            jsonObject.add("consumptionLog", logObject);

        } else if (src instanceof Water water) {
            jsonObject.addProperty("type", "Water");

            JsonObject logObject = new JsonObject();
            for (Map.Entry<LocalDate, List<ConsumptionEntry>> entry : water.getConsumptionLog().entrySet()) {
                JsonArray entriesArray = new JsonArray();
                for (ConsumptionEntry ce : entry.getValue()) {
                    if (ce instanceof WaterConsumptionEntry wce) {
                        JsonObject entryObject = new JsonObject();
                        entryObject.addProperty("quantity", wce.getQuantity());
                        entriesArray.add(entryObject);
                    }
                }
                logObject.add(entry.getKey().toString(), entriesArray);
            }
            jsonObject.add("consumptionLog", logObject);
        }

        return jsonObject;
    }

    @Override
    public Consumable deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
        JsonObject jsonObject = json.getAsJsonObject();
        String type = jsonObject.has("type") ? jsonObject.get("type").getAsString() : null;

        if ("Food".equals(type) || jsonObject.has("servingSize")) {
            Food.Builder builder = new Food.Builder(jsonObject.get("name").getAsString());
            builder.description(jsonObject.has("description") ? jsonObject.get("description").getAsString() : "");
            builder.servingSize(jsonObject.get("servingSize").getAsInt());
            builder.servingsPerContainer(jsonObject.get("servingsPerContainer").getAsInt());
            builder.calories(jsonObject.get("calories").getAsInt());
            builder.carbs(jsonObject.get("carbs").getAsDouble());
            builder.fat(jsonObject.get("fat").getAsDouble());
            builder.protein(jsonObject.get("protein").getAsDouble());
            Food food = builder.build();

            if (jsonObject.has("consumptionLog")) {
                JsonObject logObject = jsonObject.get("consumptionLog").getAsJsonObject();
                for (Map.Entry<String, JsonElement> entry : logObject.entrySet()) {
                    LocalDate date = LocalDate.parse(entry.getKey());
                    JsonArray entriesArray = entry.getValue().getAsJsonArray();
                    for (JsonElement element : entriesArray) {
                        JsonObject entryObject = element.getAsJsonObject();
                        int servings = entryObject.get("servings").getAsInt();
                        MealTime mealTime = MealTime.fromString(entryObject.get("mealTime").getAsString());
                        food.consumpt(date, servings, mealTime);
                    }
                }
            }
            return food;
        } else {
            Water water = new Water();

            if (jsonObject.has("consumptionLog")) {
                JsonObject logObject = jsonObject.get("consumptionLog").getAsJsonObject();
                for (Map.Entry<String, JsonElement> entry : logObject.entrySet()) {
                    LocalDate date = LocalDate.parse(entry.getKey());
                    JsonArray entriesArray = entry.getValue().getAsJsonArray();
                    for (JsonElement element : entriesArray) {
                        JsonObject entryObject = element.getAsJsonObject();
                        int quantity = entryObject.get("quantity").getAsInt();
                        water.drink(date, quantity);
                    }
                }
            }
            return water;
        }
    }
}