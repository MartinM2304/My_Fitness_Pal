package bg.sofia.uni.fmi.myfitnesspal.items;

import bg.sofia.uni.fmi.myfitnesspal.items.tracker.ConsumptionEntry;
import bg.sofia.uni.fmi.myfitnesspal.items.tracker.WaterConsumptionEntry;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

import java.lang.reflect.Type;
import java.time.LocalDate;
import java.util.List;
import java.util.Map;

public class Water extends Consumable {

    public void drink(LocalDate date, int quantity) {
        if (date == null) {
            throw new IllegalArgumentException("date is null");
        }
        if (quantity <= 0) {
            throw new IllegalArgumentException("quantity must be positive");
        }
        consumpt(date, quantity);
    }

    @Override
    protected ConsumptionEntry createConsumptionEntry(Object[] args) {
        if (args.length < 1 || !(args[0] instanceof Integer)) {
            throw new IllegalArgumentException("Water consumption requires quantity");
        }
        int quantity = (Integer) args[0];
        return new WaterConsumptionEntry(quantity);
    }

    @Override
    public String toString() {
        return "Water";
    }

    @Override
    public JsonElement serialize(com.google.gson.JsonSerializationContext context) {
        JsonObject json = new JsonObject();
        json.addProperty("type", "Water");
        json.add("consumptionLog", context.serialize(consumptionLog));
        return json;
    }

    @Override
    public void deserialize(JsonObject json, com.google.gson.JsonDeserializationContext context) {
        if (!json.has("consumptionLog")) return;
        Type logType = new com.google.gson.reflect.TypeToken<Map<LocalDate, List<ConsumptionEntry>>>() {}.getType();
        Map<LocalDate, List<ConsumptionEntry>> log = context.deserialize(json.get("consumptionLog"), logType);
        for (Map.Entry<LocalDate, List<ConsumptionEntry>> entry : log.entrySet()) {
            for (ConsumptionEntry ce : entry.getValue()) {
                if (ce instanceof WaterConsumptionEntry wce) {
                    drink(entry.getKey(), wce.getQuantity());
                }
            }
        }
    }
}