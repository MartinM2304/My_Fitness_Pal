package bg.sofia.uni.fmi.myfitnesspal.serializer;

import bg.sofia.uni.fmi.myfitnesspal.serializer.strategy.JsonSerializationStrategy;
import bg.sofia.uni.fmi.myfitnesspal.serializer.strategy.SerializationStrategy;
import org.junit.jupiter.api.Test;

import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

class SerializationFactoryTest {

    @Test
    void testCreateStrategyJson() {
        Map<Integer, String> foodIds = new HashMap<>();
        SerializationStrategy strategy = SerializationFactory.createStrategy("json", foodIds);

        assertTrue(strategy instanceof JsonSerializationStrategy);
    }

    @Test
    void testCreateStrategyCaseInsensitive() {
        Map<Integer, String> foodIds = new HashMap<>();
        SerializationStrategy strategy = SerializationFactory.createStrategy("JSON", foodIds);

        assertTrue(strategy instanceof JsonSerializationStrategy);
    }

    @Test
    void testCreateStrategyUnknownTypeThrowsException() {
        Map<Integer, String> foodIds = new HashMap<>();
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class,
                () -> SerializationFactory.createStrategy("xml", foodIds));

        assertEquals("Unknown serialization type: xml", exception.getMessage());
    }
}