package bg.sofia.uni.fmi.myfitnesspal.serializer;

import com.google.gson.JsonElement;
import com.google.gson.JsonPrimitive;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;

class LocalDateAdapterTest {
    private final LocalDateAdapter.Serializer serializer = new LocalDateAdapter.Serializer();
    private final LocalDateAdapter.Deserializer deserializer = new LocalDateAdapter.Deserializer();

    @Test
    void testSerialize() {
        LocalDate date = LocalDate.of(2023, 11, 11);
        JsonElement result = serializer.serialize(date, LocalDate.class, null);

        assertTrue(result instanceof JsonPrimitive);
        assertEquals("2023-11-11", result.getAsString());
    }

    @Test
    void testDeserialize() {
        JsonPrimitive json = new JsonPrimitive("2023-11-11");
        LocalDate result = deserializer.deserialize(json, LocalDate.class, null);

        assertEquals(LocalDate.of(2023, 11, 11), result);
    }

    @Test
    void testDeserialize_InvalidFormat() {
        JsonPrimitive json = new JsonPrimitive("invalid-date");
        assertThrows(java.time.format.DateTimeParseException.class,
                () -> deserializer.deserialize(json, LocalDate.class, null));
    }
}