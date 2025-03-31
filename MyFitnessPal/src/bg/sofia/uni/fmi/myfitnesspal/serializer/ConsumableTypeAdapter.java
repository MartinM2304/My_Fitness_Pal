package bg.sofia.uni.fmi.myfitnesspal.serializer;

import bg.sofia.uni.fmi.myfitnesspal.items.Consumable;
import bg.sofia.uni.fmi.myfitnesspal.items.Food;
import bg.sofia.uni.fmi.myfitnesspal.items.Water;
import com.google.gson.*;

import java.lang.reflect.Type;
import java.time.LocalDate;
import java.util.Map;

public class ConsumableTypeAdapter implements JsonSerializer<Consumable>, JsonDeserializer<Consumable> {

    @Override
    public JsonElement serialize(Consumable src, Type typeOfSrc, JsonSerializationContext context) {
        JsonObject jsonObject = new JsonObject();

        if (src instanceof Food) {
            Food food = (Food) src;
            jsonObject.addProperty("type", "Food");
            jsonObject.addProperty("name", food.toString());
            jsonObject.add("consumptionLog", context.serialize(food.getConsumptionLog()));

            try {
                jsonObject.addProperty("description", getFieldValue(food, "description", String.class));
                jsonObject.addProperty("servingSize", getFieldValue(food, "servingSize", Integer.class));
                jsonObject.addProperty("servingsPerContainer", getFieldValue(food, "servingsPerContainer", Integer.class));
                jsonObject.addProperty("calories", getFieldValue(food, "calories", Integer.class));
                jsonObject.addProperty("carbs", getFieldValue(food, "carbs", Double.class));
                jsonObject.addProperty("fat", getFieldValue(food, "fat", Double.class));
                jsonObject.addProperty("protein", getFieldValue(food, "protein", Double.class));
            } catch (Exception e) {
                throw new JsonParseException("Error serializing Food fields", e);
            }
        } else if (src instanceof Water) {
            Water water = (Water) src;
            jsonObject.addProperty("type", "Water");
            jsonObject.add("consumptionLog", context.serialize(water.getConsumptionLog()));
        }

        return jsonObject;
    }

    @Override
    public Consumable deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
        JsonObject jsonObject = json.getAsJsonObject();
        String type = jsonObject.has("type") ? jsonObject.get("type").getAsString() : null;

        if ("Food".equals(type) || jsonObject.has("servingSize")) {
            Food.Builder builder = new Food.Builder(jsonObject.get("name").getAsString());

            if (jsonObject.has("description")) {
                builder.description(jsonObject.get("description").getAsString());
            }
            if (jsonObject.has("servingSize")) {
                builder.servingSize(jsonObject.get("servingSize").getAsInt());
            }
            if (jsonObject.has("servingsPerContainer")) {
                builder.servingsPerContainer(jsonObject.get("servingsPerContainer").getAsInt());
            }
            if (jsonObject.has("calories")) {
                builder.calories(jsonObject.get("calories").getAsInt());
            }
            if (jsonObject.has("carbs")) {
                builder.carbs(jsonObject.get("carbs").getAsDouble());
            }
            if (jsonObject.has("fat")) {
                builder.fat(jsonObject.get("fat").getAsDouble());
            }
            if (jsonObject.has("protein")) {
                builder.protein(jsonObject.get("protein").getAsDouble());
            }

            Food food = builder.build();

            if (jsonObject.has("consumptionLog")) {
                Type logType = new com.google.gson.reflect.TypeToken<Map<LocalDate, java.util.List<Integer>>>() {}.getType();
                Map<LocalDate, java.util.List<Integer>> consumptionLog = context.deserialize(jsonObject.get("consumptionLog"), logType);
                for (Map.Entry<LocalDate, java.util.List<Integer>> entry : consumptionLog.entrySet()) {
                    LocalDate date = entry.getKey();
                    for (Integer amount : entry.getValue()) {
                        food.consumpt(date, amount);
                    }
                }
            }
            return food;
        } else {
            Water water = new Water();

            if (jsonObject.has("consumptionLog")) {
                Type logType = new com.google.gson.reflect.TypeToken<Map<LocalDate, java.util.List<Integer>>>() {}.getType();
                Map<LocalDate, java.util.List<Integer>> consumptionLog = context.deserialize(jsonObject.get("consumptionLog"), logType);
                for (Map.Entry<LocalDate, java.util.List<Integer>> entry : consumptionLog.entrySet()) {
                    LocalDate date = entry.getKey();
                    for (Integer amount : entry.getValue()) {
                        water.consumpt(date, amount);
                    }
                }
            }
            return water;
        }
    }

    protected  <T> T getFieldValue(Object obj, String fieldName, Class<T> type) throws Exception {
        java.lang.reflect.Field field = obj.getClass().getDeclaredField(fieldName);
        field.setAccessible(true);
        Object value = field.get(obj);
        if (type == Integer.class && value instanceof Integer) {
            return type.cast(value);
        } else if (type == int.class && value instanceof Integer) {
            return (T) Integer.valueOf((Integer) value);
        } else if (type == Double.class && value instanceof Double) {
            return type.cast(value);
        } else if (type == double.class && value instanceof Double) {
            return (T) Double.valueOf((Double) value);
        }
        return type.cast(value);
    }
}