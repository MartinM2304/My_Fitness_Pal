package bg.sofia.uni.fmi.myfitnesspal.serializer;

import bg.sofia.uni.fmi.myfitnesspal.items.Consumable;
import bg.sofia.uni.fmi.myfitnesspal.items.Food;
import bg.sofia.uni.fmi.myfitnesspal.items.Water;
import bg.sofia.uni.fmi.myfitnesspal.items.tracker.ConsumptionEntry;
import bg.sofia.uni.fmi.myfitnesspal.items.tracker.FoodConsumptionEntry;
import bg.sofia.uni.fmi.myfitnesspal.items.MealTime;
import bg.sofia.uni.fmi.myfitnesspal.items.tracker.WaterConsumptionEntry;
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
    private Map<Integer, String> foodIds;

    @BeforeEach
    void setUp() {
        foodIds = new HashMap<>();
        adapter = new ConsumableTypeAdapter(foodIds);
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
        food.consumpt(date, 2, MealTime.BREAKFAST);
        Map<LocalDate, List<ConsumptionEntry>> consumptionLog = food.getConsumptionLog();
        JsonElement logJson = new JsonArray();
        when(serializationContextMock.serialize(consumptionLog)).thenReturn(logJson);
        foodIds.put(1, food.toString());

        JsonElement result = adapter.serialize(food, Consumable.class, serializationContextMock);

        JsonObject jsonObject = result.getAsJsonObject();
        assertEquals("Food", jsonObject.get("type").getAsString());
        assertEquals(1, jsonObject.get("foodId").getAsInt());
        assertEquals("Apple", jsonObject.get("name").getAsString());
        assertEquals("Red fruit", jsonObject.get("description").getAsString());
        assertEquals(100, jsonObject.get("servingSize").getAsInt());
        assertEquals(1, jsonObject.get("servingsPerContainer").getAsInt());
        assertEquals(52, jsonObject.get("calories").getAsInt());
        assertEquals(14.0, jsonObject.get("carbs").getAsDouble(), 0.001);
        assertEquals(0.2, jsonObject.get("fat").getAsDouble(), 0.001);
        assertEquals(0.3, jsonObject.get("protein").getAsDouble(), 0.001);
        assertEquals(logJson, jsonObject.get("consumptionLog"));
    }

    @Test
    void testSerialize_Water_Success() {
        Water water = new Water();
        LocalDate date = LocalDate.of(2023, 11, 11);
        water.drink(date, 500);
        Map<LocalDate, List<ConsumptionEntry>> consumptionLog = water.getConsumptionLog();
        JsonElement logJson = new JsonArray();
        when(serializationContextMock.serialize(consumptionLog)).thenReturn(logJson);

        JsonElement result = adapter.serialize(water, Consumable.class, serializationContextMock);

        JsonObject jsonObject = result.getAsJsonObject();
        assertEquals("Water", jsonObject.get("type").getAsString());
        assertEquals(logJson, jsonObject.get("consumptionLog"));
        assertFalse(jsonObject.has("servingSize"));
    }

    @Test
    void testDeserialize_FoodWithConsumptionLog() {
        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("type", "Food");
        jsonObject.addProperty("foodId", 1);
        jsonObject.addProperty("name", "Banana");
        jsonObject.addProperty("description", "Yellow fruit");
        jsonObject.addProperty("servingSize", 120);
        jsonObject.addProperty("servingsPerContainer", 1);
        jsonObject.addProperty("calories", 89);
        jsonObject.addProperty("carbs", 23.0);
        jsonObject.addProperty("fat", 0.3);
        jsonObject.addProperty("protein", 1.1);
        Map<LocalDate, List<FoodConsumptionEntry>> consumptionLog = new HashMap<>();
        consumptionLog.put(LocalDate.of(2023, 11, 11), List.of(new FoodConsumptionEntry(2, MealTime.BREAKFAST)));
        when(deserializationContextMock.deserialize(any(JsonElement.class), any(Type.class))).thenReturn(consumptionLog);
        foodIds.put(1, "Banana (120g; 89 kcal; 23.0g, 0.3g, 1.10g)");

        Consumable result = adapter.deserialize(jsonObject, Consumable.class, deserializationContextMock);

        assertTrue(result instanceof Food);
        Food food = (Food) result;
        assertEquals("Banana", food.getName());
        assertEquals("Banana (120g; 89 kcal; 23.0g, 0.3g, 1.10g)", food.toString());
    }

    @Test
    void testDeserialize_WaterWithConsumptionLog() {
        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("type", "Water");
        Map<LocalDate, List<WaterConsumptionEntry>> consumptionLog = new HashMap<>();
        consumptionLog.put(LocalDate.of(2023, 11, 11), List.of(new WaterConsumptionEntry(500)));
        when(deserializationContextMock.deserialize(any(JsonElement.class), any(Type.class))).thenReturn(consumptionLog);

        Consumable result = adapter.deserialize(jsonObject, Consumable.class, deserializationContextMock);

        assertTrue(result instanceof Water);
        Water water = (Water) result;
        assertEquals(consumptionLog, water.getConsumptionLog());
    }

    @Test
    void testDeserialize_InvalidJson() {
        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("unknown", "value");

        assertThrows(JsonParseException.class,
                () -> adapter.deserialize(jsonObject, Consumable.class, deserializationContextMock));
    }
}