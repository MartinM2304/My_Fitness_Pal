package bg.sofia.uni.fmi.myfitnesspal.serializer;

import bg.sofia.uni.fmi.myfitnesspal.items.MealTime;
import bg.sofia.uni.fmi.myfitnesspal.items.tracker.ConsumptionEntry;
import bg.sofia.uni.fmi.myfitnesspal.items.tracker.FoodConsumptionEntry;
import bg.sofia.uni.fmi.myfitnesspal.items.tracker.WaterConsumptionEntry;
import com.google.gson.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.lang.reflect.Type;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;

class ConsumptionEntryTypeAdapterTest {

    private ConsumptionEntryTypeAdapter adapter;
    private JsonSerializationContext serializationContextMock;
    private JsonDeserializationContext deserializationContextMock;

    @BeforeEach
    void setUp() {
        adapter = new ConsumptionEntryTypeAdapter();
        serializationContextMock = mock(JsonSerializationContext.class);
        deserializationContextMock = mock(JsonDeserializationContext.class);
    }

    @Test
    void testSerialize_FoodConsumptionEntry() {
        FoodConsumptionEntry fce = new FoodConsumptionEntry(2, MealTime.BREAKFAST);

        JsonElement result = adapter.serialize(fce, ConsumptionEntry.class, serializationContextMock);

        JsonObject jsonObject = result.getAsJsonObject();
        assertEquals("FoodConsumption", jsonObject.get("type").getAsString());
        assertEquals(2, jsonObject.get("quantity").getAsInt());
        assertEquals(2, jsonObject.get("servings").getAsInt());
        assertEquals("BREAKFAST", jsonObject.get("mealTime").getAsString());
    }

    @Test
    void testSerialize_WaterConsumptionEntry() {
        WaterConsumptionEntry wce = new WaterConsumptionEntry(500);

        JsonElement result = adapter.serialize(wce, ConsumptionEntry.class, serializationContextMock);

        JsonObject jsonObject = result.getAsJsonObject();
        assertEquals("WaterConsumption", jsonObject.get("type").getAsString());
        assertEquals(500, jsonObject.get("quantity").getAsInt());
    }

    @Test
    void testDeserialize_FoodConsumptionEntry() {
        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("type", "FoodConsumption");
        jsonObject.addProperty("quantity", 2);
        jsonObject.addProperty("servings", 2);
        jsonObject.addProperty("mealTime", "BREAKFAST");

        ConsumptionEntry result = adapter.deserialize(jsonObject, ConsumptionEntry.class, deserializationContextMock);

        assertTrue(result instanceof FoodConsumptionEntry);
        FoodConsumptionEntry fce = (FoodConsumptionEntry) result;
        assertEquals(2, fce.getServings());
        assertEquals(MealTime.BREAKFAST, fce.getMealTime());
    }

    @Test
    void testDeserialize_WaterConsumptionEntry() {
        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("type", "WaterConsumption");
        jsonObject.addProperty("quantity", 500);

        ConsumptionEntry result = adapter.deserialize(jsonObject, ConsumptionEntry.class, deserializationContextMock);

        assertTrue(result instanceof WaterConsumptionEntry);
        WaterConsumptionEntry wce = (WaterConsumptionEntry) result;
        assertEquals(500, wce.getQuantity());
    }

    @Test
    void testDeserialize_NoTypeFoodConsumption() {
        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("servings", 3);
        jsonObject.addProperty("mealTime", "LUNCH");

        ConsumptionEntry result = adapter.deserialize(jsonObject, ConsumptionEntry.class, deserializationContextMock);

        assertTrue(result instanceof FoodConsumptionEntry);
        FoodConsumptionEntry fce = (FoodConsumptionEntry) result;
        assertEquals(3, fce.getServings());
        assertEquals(MealTime.LUNCH, fce.getMealTime());
    }

    @Test
    void testDeserialize_NoTypeWaterConsumption() {
        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("quantity", 1000);

        ConsumptionEntry result = adapter.deserialize(jsonObject, ConsumptionEntry.class, deserializationContextMock);

        assertTrue(result instanceof WaterConsumptionEntry);
        WaterConsumptionEntry wce = (WaterConsumptionEntry) result;
        assertEquals(1000, wce.getQuantity());
    }

    @Test
    void testDeserialize_InvalidMealTimeUsesFromString() {
        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("type", "FoodConsumption");
        jsonObject.addProperty("servings", 1);
        jsonObject.addProperty("mealTime", "Lunch"); // Assuming fromString handles this

        ConsumptionEntry result = adapter.deserialize(jsonObject, ConsumptionEntry.class, deserializationContextMock);

        assertTrue(result instanceof FoodConsumptionEntry);
        FoodConsumptionEntry fce = (FoodConsumptionEntry) result;
        assertEquals(1, fce.getServings());
        assertEquals(MealTime.LUNCH, fce.getMealTime());
    }

    @Test
    void testDeserialize_MissingMealTimeThrows() {
        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("type", "FoodConsumption");
        jsonObject.addProperty("servings", 1);

        assertThrows(JsonParseException.class,
                () -> adapter.deserialize(jsonObject, ConsumptionEntry.class, deserializationContextMock));
    }

    @Test
    void testDeserialize_UnknownTypeThrows() {
        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("type", "Unknown");

        assertThrows(JsonParseException.class,
                () -> adapter.deserialize(jsonObject, ConsumptionEntry.class, deserializationContextMock));
    }
}