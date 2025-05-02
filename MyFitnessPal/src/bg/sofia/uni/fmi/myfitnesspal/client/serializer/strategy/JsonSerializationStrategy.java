package bg.sofia.uni.fmi.myfitnesspal.client.serializer.strategy;

import bg.sofia.uni.fmi.myfitnesspal.client.items.Consumable;
import bg.sofia.uni.fmi.myfitnesspal.client.items.tracker.ConsumptionEntry;
import bg.sofia.uni.fmi.myfitnesspal.client.provider.strategies.IOStrategy;
import bg.sofia.uni.fmi.myfitnesspal.client.serializer.ConsumableTypeAdapter;
import bg.sofia.uni.fmi.myfitnesspal.client.serializer.ConsumptionEntryTypeAdapter;
import bg.sofia.uni.fmi.myfitnesspal.client.serializer.LocalDateAdapter;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;


import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.lang.reflect.Type;
import java.time.LocalDate;
import java.util.Map;

public class JsonSerializationStrategy implements SerializationStrategy {
    private final Gson gson;

    public JsonSerializationStrategy(Map<Integer, String> foodIds,
                                     IOStrategy ioStrategy) {
        this.gson = new GsonBuilder()
                .setPrettyPrinting()
                .registerTypeAdapter(Consumable.class,
                        new ConsumableTypeAdapter(foodIds, ioStrategy))
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
