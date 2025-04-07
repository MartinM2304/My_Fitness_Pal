package bg.sofia.uni.fmi.myfitnesspal.serializer;

import bg.sofia.uni.fmi.myfitnesspal.serializer.strategy.JsonSerializationStrategy;
import bg.sofia.uni.fmi.myfitnesspal.serializer.strategy.SerializationStrategy;
import java.util.Map;

public class SerializationFactory {
    public static SerializationStrategy createStrategy(String type, Map<Integer, String> foodIds) {
        return switch (type.toLowerCase()) {
            case "json" -> new JsonSerializationStrategy(foodIds);
            // case "csv" -> new CsvSerializationStrategy();  // Implementation needed
            // case "xml" -> new XmlSerializationStrategy();  // Implementation needed
            // case "database" -> new DatabaseSerializationStrategy();  // Implementation needed
            default -> throw new IllegalArgumentException("Unknown serialization type: " + type);
        };
    }
}