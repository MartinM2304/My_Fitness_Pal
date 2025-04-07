package bg.sofia.uni.fmi.myfitnesspal.serializer.strategy;

import bg.sofia.uni.fmi.myfitnesspal.items.Consumable;
import bg.sofia.uni.fmi.myfitnesspal.items.tracker.ConsumptionEntry;
import bg.sofia.uni.fmi.myfitnesspal.serializer.ConsumableTypeAdapter;
import bg.sofia.uni.fmi.myfitnesspal.serializer.ConsumptionEntryTypeAdapter;
import bg.sofia.uni.fmi.myfitnesspal.serializer.LocalDateAdapter;
import com.google.gson.ExclusionStrategy;
import com.google.gson.FieldAttributes;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;

import java.io.*;
import java.lang.reflect.Type;
import java.time.LocalDate;
import java.util.Map;

public class JsonSerializationStrategy implements SerializationStrategy {
    private final Gson gson;

    public JsonSerializationStrategy(Map<Integer, String> foodIds) {
        this.gson = new GsonBuilder()
                .setPrettyPrinting()
                .registerTypeAdapter(Consumable.class,
                        new ConsumableTypeAdapter(foodIds))
                .registerTypeHierarchyAdapter(ConsumptionEntry.class,
                        new ConsumptionEntryTypeAdapter())
                .registerTypeAdapter(LocalDate.class,
                        new LocalDateAdapter.Serializer())
                .registerTypeAdapter(LocalDate.class,
                        new LocalDateAdapter.Deserializer())
                .create();
    }

    @Override
    public void serialize(Map<String, Consumable> items, String destination) {
        try (FileWriter writer = new FileWriter(destination)) {
            Type mapType = new TypeToken<Map<String, Consumable>>() {
            }.getType();
            gson.toJson(items, mapType, writer);
        } catch (IOException e) {
            throw new RuntimeException("Failed to serialize to JSON", e);
        }
    }

    @Override
    public Map<String, Consumable> deserialize(String source) {
        try (FileReader reader = new FileReader(source)) {
            Type mapType = new TypeToken<Map<String, Consumable>>() {
            }.getType();
            Map<String, Consumable> items = gson.fromJson(reader, mapType);
            return items != null ? items : new java.util.HashMap<>();
        } catch (IOException e) {
            throw new RuntimeException("Failed to deserialize from JSON", e);
        }
    }
}