package bg.sofia.uni.fmi.myfitnesspal.client.serializer;

import bg.sofia.uni.fmi.myfitnesspal.client.provider.strategies.IOStrategy;
import bg.sofia.uni.fmi.myfitnesspal.client.serializer.strategy.JsonSerializationStrategy;
import bg.sofia.uni.fmi.myfitnesspal.client.serializer.strategy.SerializationStrategy;

import java.util.Map;

public class SerializationFactory {
    public static SerializationStrategy createStrategy(
            String type, Map<Integer, String> foodIds, IOStrategy ioStrategy) {
        return switch (type.toLowerCase()) {
            case "json" -> new JsonSerializationStrategy(foodIds, ioStrategy);
            // case "csv" ->
            // new CsvSerializationStrategy();  // Implementation needed
            // case "xml" ->
            // new XmlSerializationStrategy();  // Implementation needed
            // case "database" ->
            // new DatabaseSerializationStrategy();  // Implementation needed
            default -> throw new IllegalArgumentException(
                    "Unknown serialization type: "
                            + type);
        };
    }
}
