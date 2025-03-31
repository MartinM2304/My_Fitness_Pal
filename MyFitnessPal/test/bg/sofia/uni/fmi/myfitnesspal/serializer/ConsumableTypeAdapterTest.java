package bg.sofia.uni.fmi.myfitnesspal.serializer;

import bg.sofia.uni.fmi.myfitnesspal.items.Consumable;
import bg.sofia.uni.fmi.myfitnesspal.items.Food;
import bg.sofia.uni.fmi.myfitnesspal.items.Water;
import com.google.gson.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.lang.reflect.Type;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class ConsumableTypeAdapterTest {

    private ConsumableTypeAdapter adapter;
    private JsonSerializationContext serializationContextMock;
    private JsonDeserializationContext deserializationContextMock;

    @BeforeEach
    void setUp() {
        adapter = new ConsumableTypeAdapter();
        serializationContextMock = mock(JsonSerializationContext.class);
        deserializationContextMock = mock(JsonDeserializationContext.class);
    }

    @Test
    void testSerialize_Food_Success() {
        Food food = new Food.Builder("Apple")
                .description("Red fruit")
                .servingSize(100)
                .servingsPerContainer(1)
                .calories(52)
                .carbs(14.0)
                .fat(0.2)
                .protein(0.3)
                .build();
        LocalDate date = LocalDate.of(2023, 11, 11);
        food.consumpt(date, 150);
        Map<LocalDate, List<Integer>> consumptionLog = food.getConsumptionLog();
        when(serializationContextMock.serialize(consumptionLog)).thenReturn(new JsonArray());

        JsonElement result = adapter.serialize(food, Consumable.class, serializationContextMock);

        JsonObject jsonObject = result.getAsJsonObject();
        assertEquals("Food", jsonObject.get("type").getAsString());
        assertEquals("Apple (100g; 52 kcal; 14.0g, 0.2g, 0.3g)", jsonObject.get("name").getAsString());
        assertEquals("Red fruit", jsonObject.get("description").getAsString());
        assertEquals(100, jsonObject.get("servingSize").getAsInt());
        assertEquals(1, jsonObject.get("servingsPerContainer").getAsInt());
        assertEquals(52, jsonObject.get("calories").getAsInt());
        assertEquals(14.0, jsonObject.get("carbs").getAsDouble(), 0.001);
        assertEquals(0.2, jsonObject.get("fat").getAsDouble(), 0.001);
        assertEquals(0.3, jsonObject.get("protein").getAsDouble(), 0.001);
        assertTrue(jsonObject.has("consumptionLog"));
    }

    @Test
    void testSerialize_Water_Success() {
        Water water = new Water();
        LocalDate date = LocalDate.of(2023, 11, 11);
        water.consumpt(date, 500);
        Map<LocalDate, List<Integer>> consumptionLog = water.getConsumptionLog();
        when(serializationContextMock.serialize(consumptionLog)).thenReturn(new JsonArray());

        JsonElement result = adapter.serialize(water, Consumable.class, serializationContextMock);

        JsonObject jsonObject = result.getAsJsonObject();
        assertEquals("Water", jsonObject.get("type").getAsString());
        assertTrue(jsonObject.has("consumptionLog"));
        assertFalse(jsonObject.has("servingSize"));
    }

    @Test
    void testSerialize_Food_ReflectionFailure() {
        Food food = spy(new Food.Builder("Apple").build());
        doThrow(new RuntimeException("Field access error")).when(food).getClass();

        assertThrows(JsonParseException.class, () -> adapter.serialize(food, Consumable.class, serializationContextMock));
    }

    @Test
    void testDeserialize_FoodWithConsumptionLog() {
        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("type", "Food");
        jsonObject.addProperty("name", "Banana (120g; 89 kcal; 23.0g, 0.3g, 1.1g)");
        jsonObject.addProperty("description", "Yellow fruit");
        jsonObject.addProperty("servingSize", 120);
        jsonObject.addProperty("servingsPerContainer", 1);
        jsonObject.addProperty("calories", 89);
        jsonObject.addProperty("carbs", 23.0);
        jsonObject.addProperty("fat", 0.3);
        jsonObject.addProperty("protein", 1.1);
        Map<LocalDate, List<Integer>> consumptionLog = new HashMap<>();
        consumptionLog.put(LocalDate.of(2023, 11, 11), List.of(240));
        when(deserializationContextMock.deserialize(any(), any(Type.class))).thenReturn(consumptionLog);

        Consumable result = adapter.deserialize(jsonObject, Consumable.class, deserializationContextMock);

        assertTrue(result instanceof Food);
        Food food = (Food) result;
        assertEquals("Banana (120g; 89 kcal; 23.0g, 0.3g, 1.1g)", food.toString());
        assertEquals(240, food.getConsumptionForDateSum(LocalDate.of(2023, 11, 11)));
    }

    @Test
    void testDeserialize_WaterWithConsumptionLog() {
        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("type", "Water");
        Map<LocalDate, List<Integer>> consumptionLog = new HashMap<>();
        consumptionLog.put(LocalDate.of(2023, 11, 11), List.of(500));
        when(deserializationContextMock.deserialize(any(), any(Type.class))).thenReturn(consumptionLog);

        Consumable result = adapter.deserialize(jsonObject, Consumable.class, deserializationContextMock);

        assertTrue(result instanceof Water);
        Water water = (Water) result;
        assertEquals(500, water.getConsumptionForDateSum(LocalDate.of(2023, 11, 11)));
    }

    @Test
    void testDeserialize_FoodNoOptionalFields() {
        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("type", "Food");
        jsonObject.addProperty("name", "Orange (130g; 47 kcal; 12.0g, 0.1g, 0.9g)");

        Consumable result = adapter.deserialize(jsonObject, Consumable.class, deserializationContextMock);

        assertTrue(result instanceof Food);
        Food food = (Food) result;
        assertEquals("Orange (130g; 47 kcal; 12.0g, 0.1g, 0.9g)", food.toString());
    }

    @Test
    void testDeserialize_FoodNoTypeButServingSize() {
        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("name", "Grape (50g; 69 kcal; 18.0g, 0.2g, 0.7g)");
        jsonObject.addProperty("servingSize", 50);

        Consumable result = adapter.deserialize(jsonObject, Consumable.class, deserializationContextMock);

        assertTrue(result instanceof Food);
        Food food = (Food) result;
        assertEquals("Grape (50g; 69 kcal; 18.0g, 0.2g, 0.7g)", food.toString());
    }

    @Test
    void testDeserialize_WaterNoConsumptionLog() {
        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("type", "Water");

        Consumable result = adapter.deserialize(jsonObject, Consumable.class, deserializationContextMock);

        assertTrue(result instanceof Water);
        Water water = (Water) result;
        assertEquals(0, water.getConsumptionForDateSum(LocalDate.of(2023, 11, 11)));
    }
}