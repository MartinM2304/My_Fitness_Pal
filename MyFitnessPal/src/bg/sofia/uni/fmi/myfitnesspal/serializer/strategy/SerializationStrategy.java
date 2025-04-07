package bg.sofia.uni.fmi.myfitnesspal.serializer.strategy;

import bg.sofia.uni.fmi.myfitnesspal.items.Consumable;
import bg.sofia.uni.fmi.myfitnesspal.items.Item;

import java.util.List;
import java.util.Map;

public interface SerializationStrategy {
    void serialize(Map<String, Consumable> items, String destination);
    Map<String, Consumable> deserialize(String source);
}
