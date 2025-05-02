package bg.sofia.uni.fmi.myfitnesspal.client.serializer.strategy;

import bg.sofia.uni.fmi.myfitnesspal.client.items.Consumable;
import java.util.Map;

public interface SerializationStrategy {
    void serialize(Map<String, Consumable> items, String destination);
    Map<String, Consumable> deserialize(String source);
}
